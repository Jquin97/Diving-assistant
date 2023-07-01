package divingcalculations;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class DiveFormulasTest {

    private DiveFormulas diveFormulas = new DiveFormulas();

    @Test
    void calculateMOD() {
        int result = diveFormulas.calculateMOD(1.4f,32);
        assertEquals(result, 33);
    }

    @Test
    void calculateBM() {
        int result = diveFormulas.calculateBM(1.5f,56);
        assertEquals(result, 22);
    }

    @Test
    void calculateEAD() {
        int result = diveFormulas.calculateEAD(58,22);
        assertEquals(result, 57);
    }

    @Test
    void calculatePP() {
        float result = diveFormulas.calculatePP(40,23);
        assertEquals(result, 1.15f);
    }
}