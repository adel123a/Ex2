import org.junit.jupiter.api.Test;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Ex2SheetTest {

    //Test for `get(int x, int y)`
    @Test
    void test1() {
        Ex2Sheet sheet = new Ex2Sheet(10, 10);
        sheet.set(1, 1, "World");
        assertEquals("World", sheet.get(1, 1).getData());
    }


    @Test
        // Test for `width`
    void test2() {
        Ex2Sheet sheet = new Ex2Sheet(3, 10);
        assertEquals(3, sheet.width());
    }

    @Test
        // Test for `height`
    void test3() {
        Ex2Sheet sheet = new Ex2Sheet(5, 20);
        assertEquals(20, sheet.height());
    }

    @Test
        // Test for `isIn(int xx, int yy)`
    void test4() {
        Ex2Sheet sheet = new Ex2Sheet(30, 30);
        assertTrue(sheet.isIn(6, 6));
    }

    @Test
        // Test for `depth`
    void test5() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "=B1+C1");
        sheet.set(1, 0, "2");
        sheet.set(2, 0, "3");
        int[][] depth = sheet.depth();
        assertEquals(1, depth[0][0]); // Cell A1 depends on B1 and C1
        assertEquals(0, depth[1][0]); // Cell B1 has no dependencies
    }

    @Test
        // Test for `eval(int x, int y)`
    void test6()
    {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "=1 + 2 * 6");
        assertEquals("13.0", sheet.eval(0, 0));
    }

    @Test
        // Test for `isParentheses`
    void test9() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        assertTrue(sheet.isParentheses("(1*8)"));
        assertFalse(sheet.isParentheses("(1*2"));
    }


    @Test
        // Test for `indOfMainOp`
    void test10() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        int idx = sheet.indOfMainOp("1+2*3");
        assertEquals(1, idx); // Index of the main operator+
    }
    @Test
    void test11() {
        Ex2Sheet sheet = new Ex2Sheet(5, 5);
        sheet.set(0, 0, "=B1+C1"); // A1=B1+C1
        sheet.set(1, 1, "2"); // B1=2
        sheet.set(2, 1, "3"); // C1=3
        String replacedFormula = sheet.replaceReference(sheet.get(0, 0).getData());
        assertEquals("2 + 3", replacedFormula);
    }
}
