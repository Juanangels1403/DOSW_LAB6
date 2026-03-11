package edu.eci.dosw.tdd;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test
    void testMain() {
        assertDoesNotThrow(() -> App.main(new String[]{}));
    }
}