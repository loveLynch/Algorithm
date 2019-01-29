package com.lynch.search.balancetree;

/**
 * Created by lynch on 2018/10/8. <br>
 **/
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class Tests {
    // test 2-3 trees
    @Test
    public void ExpectedTree() {
        TwoThreeTree t = new TwoThreeTree();
        /* The Tree
         *
         * 					  (13)
         * 					/ 	   \
         * 			   (5,10)	    (18)
         * 			  /  |  \	    /  |  \
         * 		 (1,3)  (7) (12)  (16) (19) (24)
         *
         */

        String expectedString = "10";
        boolean expectedBoolean = false;
        t.insert(5);
        t.insert(7);
        t.insert(10);
        assertEquals(t.search(10), expectedString);
        assertEquals(t.insert(10), expectedBoolean);

        expectedString = "9 10";
        t.insert(9);
        assertEquals(t.search(9), expectedString);

        expectedString = "18 19";
        t.insert(13);
        t.insert(18);
        t.insert(16);
        t.insert(19);
        assertEquals(t.search(19), expectedString);

        expectedString = "24";
        t.insert(24);
        assertEquals(t.search(24), expectedString);

    }
    //test split left nodes
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
    //test split right nodes
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
    //test split middle nodes
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
    //test  duplicate nodes
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

