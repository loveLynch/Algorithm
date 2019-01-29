package com.lynch.search.twothreetree;

/**
 * Created by lynch on 2018/10/6. <br>
 **/

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TwoThreeTreeTests {

    @Test
    public void ExpectedTree() {
        TwoThreeTree t = new TwoThreeTree();
        String expectedString = "7 10";
        boolean expectedBoolean = false;

        t.insert(10);
        t.insert(7);
        t.insert(15);
        t.insert(1);
        t.insert(9);
        assertEquals(t.search(7), expectedString);
        assertEquals(t.insert(10), expectedBoolean);

        expectedString = "16 21";
        t.insert(20);
        t.insert(16);
        t.insert(21);
        t.insert(23);
        assertEquals(t.search(16), expectedString);

        expectedString = "2 7";
        t.insert(2);
        t.insert(3);
        assertEquals(t.search(2), expectedString);

        /* The Tree at this point is expected to look something like
         *
         * 					  (10)
         * 					/ 	   \
         * 			   (2,7)	    (16,21)
         * 			  /  |  \	    /  |  \
         * 		 	(1) (3) (9)  (15) (20) (23)
         *
         */

        expectedString = "10 21";
        t.insert(24);
        t.insert(25);
        assertEquals(t.search(21), expectedString);

    }

    @Test
    public void singleNodeTree() {
        TwoThreeTree t = new TwoThreeTree();
        int val = 9;
        t.insert(val);
        String expected = "9";

        assertEquals(expected, t.search(val));
        val = 8;
        assertEquals(expected, t.search(val));
        val = 10;
        assertEquals(expected, t.search(val));

        val = 15;
        t.insert(val);
        expected = "9 15";
        val = 9;
        assertEquals(expected, t.search(val));
        val = 8;
        assertEquals(expected, t.search(val));
        val = 10;
        assertEquals(expected, t.search(val));
        val = 15;
        assertEquals(expected, t.search(val));
        val = 18;
        assertEquals(expected, t.search(val));

        t = new TwoThreeTree();
        val = 15;
        t.insert(val);
        val = 9;
        t.insert(val);
        val = 9;
        assertEquals(expected, t.search(val));
        val = 8;
        assertEquals(expected, t.search(val));
        val = 10;
        assertEquals(expected, t.search(val));
        val = 15;
        assertEquals(expected, t.search(val));
        val = 18;
        assertEquals(expected, t.search(val));

    }

    @Test
    public void oneSplitLeft() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(9);
        t.insert(15);
        t.insert(1);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "15";
        assertEquals(expected, t.search(15));
        assertEquals(expected, t.search(17));
        assertEquals(expected, t.search(11));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));
    }

    @Test
    public void oneSplitRight() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(1);
        t.insert(9);
        t.insert(15);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "15";
        assertEquals(expected, t.search(15));
        assertEquals(expected, t.search(17));
        assertEquals(expected, t.search(11));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));
    }

    @Test
    public void oneSplitMiddle() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(1);
        t.insert(15);
        t.insert(9);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "15";
        assertEquals(expected, t.search(15));
        assertEquals(expected, t.search(17));
        assertEquals(expected, t.search(11));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));
    }


    @Test
    public void testDuplicates() {
        TwoThreeTree t = new TwoThreeTree();
        t.insert(1);
        t.insert(9);
        t.insert(15);
        t.insert(13);
        t.insert(20);
        t.insert(7);
        t.insert(4);
        t.insert(1);
        t.insert(9);
        t.insert(15);
        t.insert(1);
        t.insert(9);
        t.insert(15);
        t.insert(13);
        t.insert(20);
        t.insert(7);
        t.insert(4);
        t.insert(13);
        t.insert(20);
        t.insert(7);
        t.insert(4);

        String expected = "9";
        assertEquals(expected, t.search(9));
        expected = "4";
        assertEquals(expected, t.search(4));
        expected = "15";
        assertEquals(expected, t.search(15));

        expected = "13";
        assertEquals(expected, t.search(12));
        assertEquals(expected, t.search(13));
        assertEquals(expected, t.search(14));
        expected = "20";
        assertEquals(expected, t.search(19));
        assertEquals(expected, t.search(20));
        assertEquals(expected, t.search(21));

        expected = "1";
        assertEquals(expected, t.search(1));
        assertEquals(expected, t.search(0));
        assertEquals(expected, t.search(3));

        expected = "7";
        assertEquals(expected, t.search(6));
        assertEquals(expected, t.search(7));
        assertEquals(expected, t.search(8));

    }

}