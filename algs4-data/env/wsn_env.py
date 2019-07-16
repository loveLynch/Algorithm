#!/usr/bin/python3
# -*- coding: utf-8 -*-
# @Time    : 2019-07-08 19:34
# @Author  : lynch
import math
import matplotlib.pyplot as plt
import numpy as np
import random
import xlrd

from math import pi, sin, cos, radians


# from turtle import Turtle


class SensorNode(object):
    def __init__(self, id, energy, check_radius, x, y):
        """初始化节点属性"""
        self.id = id
        self.energy = energy
        self.check_radius = check_radius
        self.x = x
        self.y = y

    def get_node_info(self):
        """
        获取节点信息
        :return:
        """
        nodeInfo = []
        nodeInfo.append(self.id)
        nodeInfo.append(self.energy)
        nodeInfo.append(self.x)
        nodeInfo.append(self.y)

        return nodeInfo

    def get_node_location(self):
        """
        获取节点位置
        :return:
        """
        nodeLocation = []
        nodeLocation.append(self.x)
        nodeLocation.append(self.y)

        return nodeLocation

    def get_node_energy(self):
        """
        获取节点能量
        :return:
        """
        return self.energy


class KalmanFilter(object):
    def kalman_xy(x, P, measurement, R,
                  motion=np.matrix('0. 0. 0. 0.').T,
                  Q=np.matrix(np.eye(4))):
        """
        Parameters:
        x: initial state 4-tuple of location and velocity: (x0, x1, x0_dot, x1_dot)
        P: initial uncertainty convariance matrix
        measurement: observed position
        R: measurement noise
        motion: external motion added to state vector x
        Q: motion noise (same shape as P)
        """
        return KalmanFilter.kalman(x, P, measurement, R, motion, Q,
                                   F=np.matrix('''
                          1. 0. 0.1 0.;
                          0. 1. 0. 0.1;
                          0. 0. 1. 0.;
                          0. 0. 0. 1.
                          '''),
                                   H=np.matrix('''
                          1. 0. 0. 0.;
                          0. 1. 0. 0.'''))

    def kalman(x, P, measurement, R, motion, Q, F, H):
        '''
        Parameters:
        x: initial state
        P: initial uncertainty convariance matrix
        measurement: observed position (same shape as H*x)
        R: measurement noise (same shape as H)
        motion: external motion added to state vector x
        Q: motion noise (same shape as P)
        F: next state function: x_prime = F*x
        H: measurement function: position = H*x

        Return: the updated and predicted new values for (x, P)

        See also http://en.wikipedia.org/wiki/Kalman_filter

        This version of kalman can be applied to many different situations by
        appropriately defining F and H
        '''
        # UPDATE x, P based on measurement m
        # distance between measured and current position-belief
        y = np.matrix(measurement).T - H * x
        S = H * P * H.T + R  # residual convariance
        K = P * H.T * S.I  # Kalman gain
        x = x + K * y
        I = np.matrix(np.eye(F.shape[0]))  # identity matrix
        P = (I - K * H) * P

        # PREDICT x, P based on motion
        x = F * x + motion
        P = F * P * F.T + Q

        return x, P


def GeneratePointInTriangle(point_num, pointA, pointB, pointC):
    """
    三角形内部随机点分布，返回随机点位置坐标集合
    :param point_num:
    :param pointA:
    :param pointB:
    :param pointC:
    :return:
    """
    x = []
    y = []
    for i in range(1, point_num + 1):
        u = random.uniform(0.0, 1.0)
        v = random.uniform(0.0, 1.0 - u)
        pointP = u * pointC + v * pointB + (1.0 - u - v) * pointA;
        x.append(round(pointP[0], 2))
        y.append(round(pointP[1], 2))
    return x, y


def dist(v_A, v_B):
    """
    判断两个节点之间的一维距离
    :param v_A: A 二维向量
    :param v_B: B 二维向量
    :return: 一维距离
    """

    return round(np.sqrt(np.power((v_A[0] - v_B[0]), 2) + np.power((v_A[1] - v_B[1]), 2)), 3)


def azimuthAngle(v_A, v_B):
    """
    计算两个坐标间的方位角
    :param v_A:
    :param v_B:
    :return:
    """
    angle = 0.0
    x1 = v_A[0]
    y1 = v_A[1]
    x2 = v_B[0]
    y2 = v_B[1]
    dx = x2 - x1
    dy = y2 - y1
    if x2 == x1:
        angle = math.pi / 2.0
        if y2 == y1:
            angle = 0.0
        elif y2 < y1:
            angle = 3.0 * math.pi / 2.0
    elif x2 > x1 and y2 > y1:
        angle = math.atan(dx / dy)
    elif x2 > x1 and y2 < y1:
        angle = math.pi / 2 + math.atan(-dy / dx)
    elif x2 < x1 and y2 < y1:
        angle = math.pi + math.atan(dx / dy)
    elif x2 < x1 and y2 > y1:
        angle = 3.0 * math.pi / 2.0 + math.atan(dy / -dx)
    return (angle * 180 / math.pi)


class Env(object):
    def __init__(self):
        """
        初始化环境属性
        """
        self.node_num = 36  # 内部节点撒点数
        self.check_radius = 30  # 节点探测半径
        self.boundary_length = 120  # 环境边长
        self.initial_energy = 400  # 节点初始能量
        self.move_speed = 20  # 节点移动速度
        self.node_dict, self.edge_node_dict = self.getNodeInfo()  # 初始化内部移动节点和边缘静态节点信息

        # 目标轨迹设计相关参数,模拟运动

        self.angle = 40
        self.vel = 20
        self.h0 = 60  # 目标节点默认入侵为最左侧，只需设计节点的纵坐标
        self.interval_time = 0.1  # 设置目标坐标保存间隔时间
        self.target_dict = self.set_target_trace()  # 初始化本次仿真的目标移动轨迹，模拟抛物线运动，设置步长，周期性保持目标节点位置坐标
        self.times = len(self.target_dict)
        self.kf_prediction_dict = self.get_kf_prediction()

        # 状态
        self.prediction = [0 for i in range(len(self.node_dict))]  # 卡尔曼滤波预测目标位置
        self.contribution = [0 for i in range(len(self.node_dict))]  # 某时刻节点的贡献度
        self.state = self.prediction + self.contribution  # 系统状态预测目标+贡献度
        self.n_features = len(self.state)  # 状态维数
        self.state_run_space = ['i' for i in range(36)]  # 'i':idle , 'c':Check 'w':Work 's':Sleep
        # 定义能量消耗 J
        self.Sleep = 0.1
        self.Idle = 0.3
        self.Ckeck = 0.5
        self.Work = 0.8
        # 设置调度阈值，归一化处理
        self.con_threshold = random.uniform(0, 1)

        self.action_space = [0 for i in range(3)]  # 增加、减小、不变
        self.n_actions = len(self.action_space)  # 动作空间大小
        self.t_targetId = 1

        #     绘制初始部署图
        plt.xlabel('node deployment and target trajectory')
        plt.xlim(-1, 121)
        plt.ylim(-1, 121)
        plt.show()
        self.plt_trace_error()

    def getNodeInfo(self):
        """
        读取节点初始信息
        :return:
        """
        node_dict = {}  # 内部节点信息{sensorId:SensorNode}
        edge_node_dict = {}  # 边缘节点信息{sensorId:SensorNode}

        # # 内部节点方案一：内部节点随机均匀分布
        # # 设置矩形区域内两个三角形的四个顶点
        # pointA = np.array([0, 0])
        # pointB = np.array([0, self.boundary_length])
        # pointC = np.array([self.boundary_length, 0])
        # pointD = np.array([self.boundary_length, self.boundary_length])
        # x1_set, y1_set = GeneratePointInTriangle(int(self.node_num / 2), pointA, pointB, pointC)
        # x2_set, y2_set = GeneratePointInTriangle(int(self.node_num / 2), pointD, pointB, pointC)
        # # 左下角部分随机点
        # nodeId = 1
        # for x1, y1 in zip(x1_set, y1_set):
        #     node_dict.update({nodeId: SensorNode(nodeId, self.initial_energy, self.check_radius, x1, y1)})
        #     nodeId = nodeId + 1
        # # 右下角部分随机点
        # nodeId = self.node_num / 2 + 1
        # for x2, y2 in zip(x2_set, y2_set):
        #     node_dict.update({nodeId: SensorNode(nodeId, self.initial_energy, self.check_radius, x2, y2)})
        #     nodeId = nodeId + 1
        #
        # # 在区域内画出内部节点
        # plt.scatter(x1_set, y1_set, c='b', marker='o')
        # plt.scatter(x2_set, y2_set, c='b', marker='o')

        # 边缘节点，整个系统中边缘节点位置不变，只更新能量，节点属性从xlsx文件中读取
        # 打开文件
        data = xlrd.open_workbook(r'nodeInfo.xlsx')
        # 获取表格数目
        nums = len(data.sheets())
        for i in range(nums):
            # 根据sheet顺序打开sheet
            work = data.sheets()[i]
            # 根据sheet名称获取
            sheet1 = data.sheet_by_name('Sheet1')  # 内部节点
            sheet2 = data.sheet_by_name('Sheet2')  # 边缘节点
            # 获取sheet（工作表）行（row）、列（col）数
            nrows1 = sheet1.nrows  # 行
            ncols1 = sheet1.ncols  # 列
            nrows2 = sheet2.nrows  # 行
            ncols2 = sheet2.ncols  # 列
        # 内部节点方案二：读取xlsx存储内部节点位置
        x_mat = []
        y_mat = []
        for i in range(nrows1):
            node_dict.update({sheet1.row_values(i)[0]: SensorNode(sheet1.row_values(i)[0], sheet1.row_values(i)[1],
                                                                  self.check_radius, sheet1.row_values(i)[2],
                                                                  sheet1.row_values(i)[3])})
            x_mat.append(sheet1.row_values(i)[2])
            y_mat.append(sheet1.row_values(i)[3])
        plt.scatter(x_mat, y_mat, c='b', marker='o')

        # 存储边缘节点位置
        edge_x_mat = []
        edge_y_mat = []
        for i in range(nrows2):
            # print(sheet2.row_values(i))
            edge_node_dict.update({sheet2.row_values(i)[0]: SensorNode(sheet2.row_values(i)[0], sheet2.row_values(i)[1],
                                                                       self.check_radius, sheet2.row_values(i)[2],
                                                                       sheet2.row_values(i)[3])})
            edge_x_mat.append(sheet2.row_values(i)[2])
            edge_y_mat.append(sheet2.row_values(i)[3])
        plt.scatter(edge_x_mat, edge_y_mat, c='r', marker='o')
        # for x, y in zip(x_mat, y_mat):
        #     plt.text(x, y, (x, y), ha="center", va="bottom", fontsize=10)
        return node_dict, edge_node_dict

    def set_target_trace(self):
        """
        设计生成目标节点运动轨迹
        :return:
        """
        target_dict = {}  # 存储目标轨迹坐标
        # 以输入的方式输入目标的初始参数
        # angle = eval(input('Enter the launch angle(in degrees):'))
        #
        # vel = eval(input('Enter the initial velocity(in meters/sec):'))
        # h0 = eval(input('Enter the initial height(in meters):'))
        # time = eval(input('Enter the time interval:'))
        # # 设置目标的起始位置
        # xpos = 0
        # ypos = h0
        # theta = radians(angle)  # 将输入的角度值转换为弧度值
        # xvel = vel * cos(theta)  # 目标节点的初始速度在x轴上的分量
        # yvel = vel * sin(theta)  # 目标节点的初始速度在y轴上的分量
        # 设置目标的起始位置
        xpos = 0
        ypos = self.h0
        theta = radians(self.angle)  # 将输入的角度值转换为弧度值
        xvel = self.vel * cos(theta)  # 目标节点的初始速度在x轴上的分量
        yvel = self.vel * sin(theta)  # 目标节点的初始速度在y轴上的分量
        time = self.interval_time
        # 创建Turtle对象，刚创建的小乌龟对象，位于坐标原点（0,0），朝向x轴正方向
        # t = Turtle()
        # t.color('red')  # 设置画笔的颜色
        # t.pensize(2)  # 线条粗细
        # t.speed(2)  # 调整速度
        # t.hideturtle()  # 隐藏小乌龟
        #
        # # 绘制x轴和y轴
        # t.forward(350)  # 绘制x轴
        # t.goto(0, 0)  # 回到坐标原点，准备绘制y轴
        # t.goto(0, 200)  # 绘制y轴

        print('the position:({0:.3f},{1:0.3f})'.format(xpos, ypos))
        xScale = 25  # x坐标放大倍数
        yScale = 30  # y坐标放大倍数
        # 画笔移到铅球的起始位置，准备绘制铅球的运行轨迹
        # t.goto(xpos * xScale, ypos * yScale)

        target_x_set = []
        target_y_set = []
        # 通过while循环绘制铅球的运行轨迹，每隔time秒，取一个点，将所有取到的点连起来
        target_x_set.append(round(xpos, 3))
        target_y_set.append(round(ypos, 3))
        targetId = 1
        target_dict.update({targetId: [round(xpos, 3), round(ypos, 3)]})
        while (0 <= ypos <= self.boundary_length) and (0 <= xpos <= self.boundary_length):
            targetId = targetId + 1
            xpos = xpos + time * xvel
            ypos = ypos + time * yvel
            # 方案一：产生一个新的偏向角度(-90,90)之间，按步长更新
            theta = radians(random.uniform(-90, 90))
            xvel = self.vel * cos(theta)
            yvel = self.vel * sin(theta)
            # 方案二：模拟抛物运动
            # yvel1 = yvel - time * 5
            # ypos = ypos + time * (yvel + yvel1) / 2.0
            # yvel = yvel1
            print('the position:({0:.3f},{1:0.3f})'.format(xpos, ypos))
            target_x_set.append(round(xpos, 3))
            target_y_set.append(round(ypos, 3))
            target_dict.update({targetId: [round(xpos, 3), round(ypos, 3)]})
            # plt.scatter(target_x_set, target_y_set)
            # t.goto(xpos * xScale, ypos * yScale)
            plt.plot(target_x_set, target_y_set, c='r')

        return target_dict
        # return target_x_set, target_y_set

    def get_contribution(self, t_target_location):
        """
        计算节点sensorId的贡献度
        :param t_target: t时刻目标节点的坐标
        """
        # 获取每个步长时刻目标节点的位置
        nodeInfo = self.node_dict
        contribution_dict = {}
        contribution_list = []
        for i in nodeInfo:
            sensor = nodeInfo.get(i)
            sensor_energy = sensor.get_node_energy()
            sensor_location = sensor.get_node_location()
            # 计算节点sensor的贡献度,w=0.4
            # 数据归一化，能量最大为40J,距离选择1～100*sqrt(2)
            distance = dist(sensor_location, t_target_location)
            if distance < 1.0:
                distance = 1.0
            sensor_contribution = 0.3 * (sensor_energy / 40) + 0.7 * (1.0 / distance)
            contribution_dict.update({i: round(sensor_contribution, 3)})
            contribution_list.append(round(sensor_contribution, 3))

        # return contribution_dict
        return contribution_list

    def get_sensor_contribution(self, sensor, t_target_location):
        """
        计算某个节点sensorId的贡献度
        :param t_target: t时刻目标节点的坐标
        """
        sensor_energy = sensor.get_node_energy()
        sensor_location = sensor.get_node_location()
        # 计算节点sensor的贡献度,w=0.4
        # 数据归一化，能量最大为40J,距离选择1～100*sqrt(2)
        distance = dist(sensor_location, t_target_location)
        if distance < 1.0:
            distance = 1.0
        sensor_contribution = 0.5 * (sensor_energy / 40) + 0.5 * (1.0 / distance)

        return sensor_contribution

    def get_kf_prediction(self):
        """
        卡尔曼滤波预测目标位置
        :return:
        """
        kalman_predict_dict = {}
        x = np.matrix('0. 60. 0. 0.').T  # initial state 4-tuple of location and velocity
        P = np.matrix(np.eye(4)) * 1000  # initial uncertainty
        observed_x = []
        observed_y = []
        for target in self.target_dict:
            observed_x.append(self.target_dict.get(target)[0])
            observed_y.append(self.target_dict.get(target)[1])

        result = []
        R = 0.01 ** 2
        for meas in zip(observed_x, observed_y):
            x, P = KalmanFilter.kalman_xy(x, P, meas, R)
            result.append((x[:2]).tolist())
        kalman_x, kalman_y = zip(*result)
        # plt.plot(kalman_x, kalman_y, 'g*')

        targetId = 1
        for p_target_x, p_target_y in zip(kalman_x, kalman_y):
            kalman_predict_dict.update({targetId: [round(p_target_x[0], 3), round(p_target_y[0], 3)]})
            targetId = targetId + 1
        # return kalman_x, kalman_y
        return kalman_predict_dict

    def get_prediction(self, t_prediction_location):
        """
        设置节点预测噪声
        :param t_prediction_location:
        :return:
        """
        prediction_feedback_dict = {}
        prediction_feedback_list = []
        nodeInfo = self.node_dict
        mu = 0
        sigma = 0.12
        for i in nodeInfo:
            sensor = nodeInfo.get(i)
            sensor_location = sensor.get_node_location()
            distance = round(dist(sensor_location, t_prediction_location) + random.gauss(mu, sigma), 2)
            prediction_feedback_list.append(distance)
            prediction_feedback_dict.update({i: distance})
        # return prediction_feedback_dict
        return prediction_feedback_list

    def plot_trace_node(self, t_traceId):
        """
        绘制边缘节点与目标路径
        :rtype: object
        """
        target_x = []
        target_y = []
        for target in self.target_dict:
            target_x.append(self.target_dict.get(target)[0])
            target_y.append(self.target_dict.get(target)[1])
        plt.plot(target_x, target_y, 'r-')
        edge_x = []
        edge_y = []
        for edge_node in self.edge_node_dict:
            edge_x.append(self.edge_node_dict.get(edge_node).get_node_location()[0])
            edge_y.append(self.edge_node_dict.get(edge_node).get_node_location()[1])
        plt.scatter(edge_x, edge_y, c='r')
        # 绘制节点移动后的部署图
        update_node_x = []
        update_node_y = []
        for update_node in self.node_dict:
            update_node_x.append(self.node_dict.get(update_node).get_node_location()[0])
            update_node_y.append(self.node_dict.get(update_node).get_node_location()[1])
        plt.scatter(update_node_x, update_node_y, c='g')
        plt.gca().set_title(str(t_traceId))
        plt.xlabel('node move')
        plt.xlim(-1, 121)
        plt.ylim(-1, 121)
        plt.show()

    def trace_error(self):
        """
        预测与实际每时刻的误差
        :return:
        """
        trace_error = []
        for measure, prediction in zip(self.target_dict, self.get_kf_prediction()):
            trace_error.append(dist(self.target_dict.get(measure), self.get_kf_prediction().get(prediction)))
        return trace_error

    def plt_trace_error(self):
        """
        绘制误差图
        """
        plt.xlabel('t_target')
        plt.ylabel('position_error')
        trace_error = self.trace_error()
        plt.plot(range(len(trace_error)), trace_error)
        plt.show()
        print(trace_error)

    def node_is_crossing(self, move_sensor):
        """
        节点移动是否会造成越界，不在监控区域
        :param move_sensor:
        :return:
        """
        if 0 <= move_sensor.get_node_location()[0] <= self.boundary_length - self.check_radius / 2 or 0 <= \
                move_sensor.get_node_location()[
                    1] <= self.boundary_length - self.check_radius / 2:
            is_crossing = 1
        else:
            is_crossing = -1
        return is_crossing

    def reset(self):
        self.prediction = self.get_prediction(self.kf_prediction_dict.get(1))
        self.contribution = self.get_contribution(self.target_dict.get(1))
        self.state = np.array(self.prediction + self.contribution)
        return self.state

    def update_maze(self, targetId):
        self.prediction = self.get_prediction(self.kf_prediction_dict.get(targetId))
        self.contribution = self.get_contribution(self.target_dict.get(targetId))
        self.state = np.array(self.prediction + self.contribution)
        return self.state

    def step(self, action):
        node_run_state = self.state_run_space
        check_node_dict = {}
        idle_node_dict = {}
        sleep_node_dict = {}
        done = False

        for i, this_state in enumerate(node_run_state):
            check_node_dict.update({i: 0})
            idle_node_dict.update({i: 0})
            sleep_node_dict.update({i: 0})

        # print('目标轨迹第' + str(t_traceId) + '个散点')
        # 将上一个已经work的节点恢复成check状态
        index = 0
        while index < 36:
            if node_run_state[index] == 'w':
                node_run_state[index] = 'c'
            # 更新节点的idle次数，并判断是否让节点进入sleep状态
            if idle_node_dict.get(index) == 5 and node_run_state[index] == 'i':
                node_run_state[index] = 's'
                idle_node_dict.update({index: 0})
            idle_node_dict.update({index: idle_node_dict.get(index) + 1})
            # 更新节点的sleep次数，并判断是否让节点进入idle状态
            if node_run_state[index] == 's':
                sleep_node_dict.update({index: sleep_node_dict.get(index) + 1})
                if sleep_node_dict.get(index) == 5:
                    node_run_state[index] = 'i'
                    sleep_node_dict.update({index: 0})
            index = index + 1
        # t时刻目标节点的位置，并调动可探测的目标的周边节点
        target_location = self.target_dict.get(self.t_targetId)  # t时刻目标节点的位置
        work_node_list = []
        for nodeId in self.node_dict:
            sensor = self.node_dict.get(nodeId)
            check_index = int(nodeId) - 1
            if dist(sensor.get_node_location(), target_location) <= self.check_radius:
                node_run_state[check_index] = 'c'
                check_node_dict.update({check_index: check_node_dict.get(check_index) + 1})
                if self.get_sensor_contribution(sensor, target_location) >= self.con_threshold:
                    node_run_state[check_index] = 'w'
                    work_node_list.append(nodeId)
                    # 需移动的节点
                    move_sensor = self.node_dict.get((check_index + 1))
                    # 需移动的角度
                    move_angle = azimuthAngle(move_sensor.get_node_location(), target_location)
                    # 移动后节点的新坐标
                    move_sensor_x = move_sensor.get_node_location()[
                                        0] + self.move_speed * math.cos(move_angle) * self.interval_time
                    move_sensor_y = move_sensor.get_node_location()[
                                        1] + self.move_speed * math.sin(move_angle) * self.interval_time
                    # 节点移动消耗的能量
                    move_sensor_energy = move_sensor.get_node_energy() - dist(move_sensor.get_node_location(),
                                                                              [move_sensor_x,
                                                                               move_sensor_y]) * self.Work
                    # 更新节点信息
                    self.node_dict.update(
                        {(check_index + 1): SensorNode(check_index + 1, move_sensor_energy, self.check_radius,
                                                       move_sensor_x, move_sensor_y)})

        # 如果节点进入check状态，进行探测次数累加
        # 更新节点的check次数，并判断是否让节点进入idle状态
        check_idle_index = 0
        while check_idle_index < 36:
            if check_node_dict.get(check_idle_index) == 5:
                node_run_state[check_idle_index] = 'i'
                check_node_dict.update({check_idle_index: 0})

            elif node_run_state[check_idle_index] == 'c':
                check_node_dict.update({check_idle_index: check_node_dict.get(check_idle_index) + 1})

            check_idle_index = check_idle_index + 1
        # reward function

        w1 = 0.45
        w2 = 0.45
        w3 = 0.1
        sum_energy = 0
        sum_con = 0
        sum_filed = 0
        reward = 0
        for this_id, work_node_id in enumerate(work_node_list):
            this_work_sensor = self.node_dict.get(work_node_id)

            sum_energy += this_work_sensor.get_node_energy()
            sum_con += self.get_sensor_contribution(this_work_sensor, target_location)
            sum_filed += self.node_is_crossing(this_work_sensor)
        if action == 0:
            reward = reward - (w1 * sum_energy + w2 * sum_con + w3 * sum_filed)
            self.con_threshold += 0.01

        elif action == 1:
            self.con_threshold -= 0.01
            reward = reward + (w1 * sum_energy + w2 * sum_con + w3 * sum_filed)
        else:
            reward = 0
            self.con_threshold = self.con_threshold
        self.t_targetId += 1
        # if action == 0:
        #     reward = -1
        # elif action == 1:
        #     reward = 1
        # else:
        #     reward = 0

        if self.t_targetId == self.target_dict.__len__():
            # print(self.state_run_space)
            self.t_targetId = 1
            self.state = self.update_maze(self.t_targetId)
            # for node_id,this_node in self.node_dict.items():
            #     print(this_node.get_node_info())
            done = True

        return self.state, reward, done


def step1(self):
    moveIndex = 0
    node_change_dict = {}
    node_energy_sum = []
    for kf_target, target in zip(self.kf_prediction_dict, self.target_dict):
        self.prediction = self.get_prediction(self.kf_prediction_dict.get(kf_target))
        self.contribution = self.get_contribution(self.target_dict.get(target))
        self.state = self.prediction + self.contribution
        print('第' + str(kf_target) + '步长')
        # print(self.state) #dqn状态
        while (moveIndex < 36):
            if self.state[moveIndex] < self.check_radius and self.state[moveIndex + 36] > self.con_threshold:
                self.action_space[moveIndex] = 1
                print('节点' + str(moveIndex + 1) + '移动')
                sensor = self.node_dict.get((moveIndex + 1))
                sensor_x = 2 * sensor.get_node_location()[0] + abs(
                    math.cos(azimuthAngle(sensor.get_node_location(), self.kf_prediction_dict.get(kf_target + 1))))
                sensor_y = 2 * sensor.get_node_location()[1] + abs(
                    math.sin(azimuthAngle(sensor.get_node_location(), self.kf_prediction_dict.get(kf_target + 1))))

                node_change_dict.update(
                    {(moveIndex + 1): SensorNode((moveIndex + 1), sensor.get_node_energy() - self.Work, 10,
                                                 sensor_x, sensor_y)})

            moveIndex = moveIndex + 1
        moveIndex = 0
        # 节点状态变化
        while (moveIndex < 36):
            if self.state[moveIndex] < self.check_radius:
                self.state_run_space[moveIndex] = 'c'
            moveIndex = moveIndex + 1

        print(self.state_run_space)
        self.node_dict.update(node_change_dict)
        reward = 0
        w1 = 0.5
        w2 = 0.5
        for move_node in node_change_dict:
            move_sensor = node_change_dict.get(move_node)
            consumption_energy = self.Work
            position_error = dist(self.kf_prediction_dict.get(kf_target), self.target_dict.get(target))

            reward = w1 * consumption_energy + w2 * position_error * (dist(move_sensor.get_node_location(),
                                                                           self.kf_prediction_dict.get(
                                                                               kf_target)) / 100 * math.sqrt(2))
            print(reward)
        # 新位置分布图
        # new_x_set = []
        # new_y_set = []
        # energy_sum = 0
        # for node in self.node_dict:
        #     new_x_set.append(self.node_dict.get(node).get_node_location()[0])
        #     new_y_set.append(self.node_dict.get(node).get_node_location()[1])
        #     energy_sum += self.node_dict.get(node).get_node_energy()
        #
        # node_energy_sum.append(energy_sum)
        # energy_sum = 0
        # plt.scatter(new_x_set, new_y_set, c='g')
        # plt.show()
        node_change_dict = {}
        # print(self.action_space) # dqn 行动
        moveIndex = 0  # 移动下标恢复起点
        self.action_space = [0 for i in range(36)]  # 是否移动恢复初始
    for energy in node_energy_sum:
        print(round(energy, 1))

# 测试

# env = Env()
# print(env.reset())
# nodeInfo = env.node_dict
# edgeNodeInfo = env.edge_node_dict
#
# k_dict = env.kf_prediction_dict
# print()
# for i in k_dict:
#     print(str(k_dict.get(i)[0]) + ' ' + str(k_dict.get(i)[1]))
# t_dict = env.set_target_trace()
# con_dict = env.get_contribution(t_dict.get(1))
# print(con_dict)
# print(con_dict.get(1))

# plt.xlim(-1, 121)
# plt.ylim(-1, 121)
# plt.show()
#
# state = env.reset()
# print(state[0][0])
# env.step()
# for pr in env.get_kf_prediction():
#     print(env.get_kf_prediction().get(pr))
# env.trace_error()
#
# print()
# print(nodeInfo.get(1).get_node_location())
# print(nodeInfo.get(2).get_node_location())
# node1_location = nodeInfo.get(1).get_node_location()
# node2_location = nodeInfo.get(2).get_node_location()
# node12_location = edgeNodeInfo.get(12).get_node_location()
# print(dist(node1_location, node2_location))
# print(dist(node1_location, node12_location))
