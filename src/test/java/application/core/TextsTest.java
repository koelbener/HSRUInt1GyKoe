package application.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TextsTest {

    @Test
    public void formatWithTwoParameters() {
        // Setup fixtures
        String given = "my test string with a parameter {} and a number {} in the middle";
        String param1 = "TestString";
        Long param2 = new Long(2);
        String expected = "my test string with a parameter " + param1 + " and a number " + param2 + " in the middle";
        // Excercise SUT
        String actual = Texts.format(given, param1, param2);
        // Verify outcome
        assertEquals(expected, actual);

    }
}
