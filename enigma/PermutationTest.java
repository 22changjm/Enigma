package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Michael Chang
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkDerangement() {
        Permutation permtest = new Permutation(
                "(ABCDEFGHIJKLMNOPQRSTUVWXY)", UPPER);
        assertEquals(false, permtest.derangement());
        Permutation permtest1 = new Permutation("", UPPER);
        assertEquals(false, permtest1.derangement());
        Permutation permtest2 = new Permutation(
                "(ABCDEFGHIJKLMNOPQRSTUVWXYZ)", UPPER);
        assertEquals(true, permtest2.derangement());
        Permutation p = new Permutation(
                "(ABCD)(EF)(GHIJKLMNOP)(QRSTUVWXYZ)", UPPER);
        assertEquals(true, p.derangement());

    }

    @Test
    public void checkPermute() {
        Permutation permtest = new Permutation(
                "(ABCDEF)(GHIJKLMNOPQRSTUVWXY)", UPPER);
        assertEquals(4, permtest.permute(3));
        assertEquals(25, permtest.permute(25));
        assertEquals(6, permtest.permute(24));
        assertEquals(0, permtest.permute(5));
        assertEquals(UPPER.toChar(0), permtest.permute(UPPER.toChar(5)));
        assertEquals(UPPER.toChar(25), permtest.permute(UPPER.toChar(25)));
        assertEquals(UPPER.toChar(6), permtest.permute(UPPER.toChar(24)));
        Permutation p = new Permutation(
                "(ABCD)(E)(FGHIJKLMNOP)(QRSTUVWXY)", UPPER);
        assertEquals(4, p.permute(4));
        assertEquals(0, p.permute(3));
        assertEquals(5, p.permute(15));
        assertEquals(25, p.permute(25));
        assertEquals('E', p.permute('E'));
        assertEquals('Z', p.permute('Z'));
        assertEquals('D', p.permute('C'));
        assertEquals('N', p.permute('M'));
        assertEquals('A', p.permute('D'));
    }

    @Test
    public void checkInvert() {
        Permutation permtest = new Permutation(
                "(ABCDEF)(GHIJKLMNOPQRSTUVWXY)", UPPER);
        assertEquals(3, permtest.invert(4));
        assertEquals(25, permtest.invert(25));
        assertEquals(24, permtest.invert(6));
        assertEquals(5, permtest.invert(0));
        assertEquals(UPPER.toChar(3), permtest.invert(UPPER.toChar(4)));
        assertEquals(UPPER.toChar(25),
                permtest.invert(UPPER.toChar(25)));
        assertEquals(UPPER.toChar(24), permtest.invert(UPPER.toChar(6)));
        Permutation p = new Permutation(
                "(ABCD)(E)(FGHIJKLMNOP)(QRSTUVWXY)", UPPER);
        assertEquals(4, p.invert(4));
        assertEquals(3, p.invert(0));
        assertEquals(15, p.invert(5));
        assertEquals(25, p.invert(25));
        assertEquals('E', p.invert('E'));
        assertEquals('Z', p.invert('Z'));
        assertEquals('C', p.invert('D'));
        assertEquals('M', p.invert('N'));
        assertEquals('D', p.invert('A'));
    }
}
