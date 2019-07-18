import random
import math

'''
RFID过程模拟
'''


class Tag(object):
    def __init__(self, id, random):
        self.id = id
        self.random = random

    def get_tag_id(self):
        return self.id

    def get_random(self):
        return self.random


# Tag总数量
tag_sum = 10
# Q值
q = 5

the_max = math.pow(2, q)
tag_dict = {}
for i in range(10):
    the_random = random.randint(0, the_max - 1)
    tag_dict.update({i: Tag(i, the_random)})
tag_info = []
for i, tag in tag_dict.items():
    tag_info.append('tag' + str(i) + ' ' + str(tag.get_random()))
print(tag_info)
print("select")
ep = 0
# RFID识别流程
while (ep < the_max):
    if ep == 0:
        print("-----query-----")
    else:
        print('-----queryReq' + str(ep) + '-----')
    count = 0
    rn16_tag_id = []
    for i, tag in tag_dict.items():
        if tag.get_random() - 1 == 0:
            count += 1
            rn16_tag_id.append(tag.get_tag_id())
        tag_dict.update({i: Tag(i, tag.get_random() - 1)})
    # 校验成功，识别一个tag
    if count == 1:
        print('rn16-ack(head,r16)')
        print('crc right')
        print('epc')
        print('tag-id-' + str(rn16_tag_id[0]))
    # 校验失败，发生碰撞
    elif count > 1:
        print('rn16-ack(head,r16)')
        print('crc error')
    ep += 1
