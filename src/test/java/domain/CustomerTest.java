package domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CustomerTest {
    @Test
    public void testCreateCustomer() {
        Customer cu1 = new Customer("Keller", "Heinz");
        cu1.setAdress("Zelgweg 12", 8000, "ZŸrich");

        assertEquals("Keller", cu1.getName());
        assertEquals("Heinz", cu1.getSurname());

        assertEquals("Zelgweg 12", cu1.getStreet());
        assertEquals(8000, cu1.getZip());
        assertEquals("ZŸrich", cu1.getCity());
    }

}
