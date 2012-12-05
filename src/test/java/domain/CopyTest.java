package domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CopyTest {
    @Test
    public void testBook() {
        Book t = new Book("Design Pattern");
        Copy c1 = new Copy(t);
        assertEquals(Copy.nextInventoryNumber - 1, c1.getInventoryNumber());
        Copy c2 = new Copy(t);
        assertEquals(Copy.nextInventoryNumber - 1, c2.getInventoryNumber());
        assertEquals(Condition.NEW, c2.getCondition());

        c1.setCondition(Condition.DAMAGED);

        assertEquals(Condition.DAMAGED, c1.getCondition());
    }

}
