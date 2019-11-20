package enigma;

import org.junit.Test;
import ucb.junit.textui;
import java.util.ArrayList;
import static org.junit.Assert.*;


import static enigma.TestUtils.UPPER;


/** The suite of all JUnit tests for the enigma package.
 *  @author Michael Chang
 */
public class UnitTest {
    @Test
    public void test() {
        Permutation ia = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Permutation iiia = new Permutation(
                "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", UPPER);
        Permutation iva = new Permutation(
                "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", UPPER);
        Permutation va = new Permutation(
                "(AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", UPPER);
        Permutation via = new Permutation(
                "(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)", UPPER);
        Permutation betaa = new Permutation(
                "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);
        Permutation gamaa = new Permutation(
                "(AFNIRLBSQWVXGUZDKMTPCOYJHE)", UPPER);
        Permutation ba = new Permutation(
                "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) "
                        + "(IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
        Permutation ca = new Permutation(
                "(AR) (BD) (CO) (EJ) (FN) (GT) "
                        + "(HK) (IV) (LM) (PW) (QZ) (SX) (UY)", UPPER);
        Rotor i = new MovingRotor("I", ia, "Q");
        Rotor iii = new MovingRotor("III", iiia, "V");
        Rotor iv = new MovingRotor("IV", iva, "J");
        Rotor v = new MovingRotor("V", va, "Z");
        Rotor vi = new MovingRotor("VI", via, "ZM");
        Rotor beta = new FixedRotor("Beta", betaa);
        Rotor gamma = new FixedRotor("Gamma", gamaa);
        Rotor b = new Reflector("B", ba);
        Rotor c = new Reflector("C", ca);
        ArrayList<Rotor> collection = new ArrayList<>(
                java.util.Arrays.asList(
                        i, iii, iv, v, vi, beta, gamma, b, c));
        String[] u = new String[] {"B", "Beta", "III", "IV", "I"};
        Machine test = new Machine(UPPER, 5, 3, collection);
        test.insertRotors(u);
        test.setRotors("AXLE");
        Permutation plugboard = new Permutation(
                "(HQ) (EX) (IP) (TR) (BY)", UPPER);
        test.setPlugboard(plugboard);
        String s = "FROM his shoulder Hiawatha Took the camera "
                + "of rosewood Made of sliding folding rosewood"
                + "Neatly put it all together In its case it "
                + "lay compactly Folded into nearly nothing"
                + "But he opened out the hinges Pushed and pulled"
                + " the joints and hinges Till it looked all squares"
                + "and oblongs Like a complicated figure in the "
                + "Second Book of Euclid";
        String q = "QVPQS OKOIL PUBKJ ZPISF XDW BHCNS CXNUO AATZX "
                + "SRCFY DGU FLPNX GXIXT YJUJR CAUGE UNCFM KUF"
                + "WJFGK CIIRG XODJG VCGPQ OH ALWEB UHTZM OXIIV "
                + "XUEFP RPR KCGVP FPYKI KITLB URVGT SFU"
                + "SMBNK FRIIM PDOFJ VTTUG RZM UVCYL FDZPG IBXRE "
                + "WXUEB ZQJO YMHIP GRRE GOHET UXDTW LCMMW AVNVJ VH"
                + "OUFAN TQACK KTOZZ RDABQ NNVPO IEFQA FS VVICV "
                + "UDUER EYNPF FMNBJ VGQ";
        q = q.replaceAll("\\s+", "");
        assertEquals(q, test.convert(s));
    }

    @Test
    public void checkRotates() {
        Permutation ia = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Permutation betaa = new Permutation(
                "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);
        Permutation ca = new Permutation(
                "(AR) (BD) (CO) (EJ) (FN) (GT) "
                        + "(HK) (IV) (LM) (PW) (QZ) (SX) (UY)", UPPER);
        Rotor i = new MovingRotor("I", ia, "Q");
        Rotor beta = new FixedRotor("Beta", betaa);
        Rotor c = new Reflector("C", ca);
        assertTrue(i.rotates());
        assertFalse(beta.rotates());
        assertFalse(c.rotates());
    }

    @Test
    public void checkatNotch() {
        Permutation ia = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Rotor i = new MovingRotor("I", ia, "A");
        assertTrue(i.atNotch());
        i.advance();
        assertFalse(i.atNotch());
    }

    @Test
    public void checkconvertForward() {
        Permutation ia = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Rotor i = new MovingRotor("I", ia, "B");
        assertEquals(4, i.convertForward(0));
        assertEquals(0, i.convertForward(20));
        assertEquals(9, i.convertForward(25));
        assertEquals(18, i.convertForward(18));
    }

    @Test
    public void checkconvertBackward() {
        Permutation ia = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Rotor i = new MovingRotor("I", ia, "B");
        assertEquals(0, i.convertBackward(4));
        assertEquals(20, i.convertBackward(0));
        assertEquals(25, i.convertBackward(9));
        assertEquals(18, i.convertBackward(18));
    }

    @Test
    public void checkSet() {
        Permutation ia = new Permutation(
                "(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        Rotor i = new MovingRotor("I", ia, "B");
        assertEquals(0, i.setting());
        i.set(3);
        assertEquals(3, i.setting());
        i.set('Z');
        assertEquals(25, i.setting());
    }


    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(PermutationTest.class, MovingRotorTest.class);
    }

}


