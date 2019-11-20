package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Michael Chang
 */
public class MovingRotorTest {

    /**
     * Testing time limit.
     */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /**
     * Check that rotor has an alphabet whose size is that of
     * FROMALPHA and TOALPHA and that maps each character of
     * FROMALPHA to the corresponding character of FROMALPHA, and
     * vice-versa. TESTID is used in error messages.
     */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    /**
     * Set the rotor to the one with given NAME and permutation as
     * specified by the NAME entry in ROTORS, with given NOTCHES.
     */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkMovingRotor() {
        Permutation s = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Rotor rotor1 = new MovingRotor("I", s, "Q");
        rotor1.set(5);
        assertEquals(8, rotor1.convertForward(5));
        assertEquals(7, rotor1.convertBackward(9));
        assertFalse(rotor1.atNotch());
        rotor1.set(16);
        assertTrue(rotor1.atNotch());
    }

    @Test
    public void checkFixedRotor() {
        Permutation q = new Permutation(
                "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);
        Rotor rotorBeta = new FixedRotor("Beta", q);
        rotorBeta.set(0);
        assertEquals(22, rotorBeta.convertForward(9));
        assertEquals(23, rotorBeta.convertBackward(7));
    }

    @Test
    public void checkReflector() {
        Permutation r = new Permutation(
                "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) "
                + "(LO) (MP) (RX) (SZ) (TV)", UPPER);
        Rotor reflectorB = new Reflector("B", r);
        assertEquals(7, reflectorB.convertForward(22));

    }
}
