import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SCellTest {
    @Test
    void test1() {
        SCell textCell = new SCell("Test");
        assertEquals("Test", textCell.getData());
        assertEquals(Ex2Utils.TEXT, textCell.getType());
    }

    @Test
    void test2() {
        SCell numberCell = new SCell("58");
        assertEquals("58", numberCell.getData());
        assertEquals(Ex2Utils.NUMBER, numberCell.getType());
    }

    @Test
    void test3() {
        SCell formulaCell = new SCell("=A3+C8");
        assertEquals("=A3+C8", formulaCell.getData());
        assertEquals(Ex2Utils.FORM, formulaCell.getType());
    }

    @Test
    void test6() {
        SCell textCell = new SCell("Hello");
        textCell.setData("123");
        assertEquals("123", textCell.getData());
    }
    @Test
    void test7() {
        SCell formulaCell = new SCell("=A1+B2");
        formulaCell.setOrder(2);
        assertEquals(2, formulaCell.getOrder());
    }
}

