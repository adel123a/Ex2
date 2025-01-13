import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CellEntryTest {

    @Test
    void test1() {
        CellEntry cell = new CellEntry("A5");
        assertTrue(cell.isValid());
        assertEquals(0, cell.getX());
        assertEquals(5, cell.getY());
    }

    @Test
    void test2() {
        CellEntry cell = new CellEntry("sijud");
        assertFalse(cell.isValid());
    }
    @Test
    void test3() {
        CellEntry cell = new CellEntry("Z100");
        assertFalse(cell.isValid());
    }
    @Test
    void test4() {
        CellEntry cell = new CellEntry("G99");
        assertTrue(cell.isValid());
    }
    @Test
    void test5() {
        CellEntry cell = new CellEntry("$99");
        assertFalse(cell.isValid());
    }
}