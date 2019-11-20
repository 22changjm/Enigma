package enigma;
import java.util.ArrayList;
import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Michael Chang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = new ArrayList<String>();
        cycles = cycles.replaceAll("[(, \\s+]", "");
        ArrayList<String> check = new ArrayList<>(
                Arrays.asList(cycles.split("\\)")));
        occurOnce(check.get(0));
        charInAlpha(check.get(0), alphabet);
        _cycles.add(check.remove(0));
        for (String cy: check) {
            occurOnce(cy);
            charInAlpha(cy, alphabet);
            addCycle(cy);
        }
        _cycles.remove("");
    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cycle = cycle.replaceAll("[(,)]", "");
        cycle = cycle.replaceAll("\\s+", "");
        for (int i = 0; i < cycle.length(); i += 1) {
            for (String cy : _cycles) {
                if (cy.contains(String.valueOf(cycle.charAt(i)))) {
                    throw new EnigmaException("Character " + cycle.charAt(i)
                           + " is already in a cycle");
                }
            }
        }
        _cycles.add(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char bfPerm = alphabet().toChar(wrap(p));
        if (inCycle(bfPerm)) {
            String cycle = findCycle(bfPerm);
            char permuted = cycle.charAt((
                    cycle.indexOf(bfPerm) + 1) % cycle.length());
            return alphabet().toInt(permuted);
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char bfInvert = alphabet().toChar(wrap(c));
        if (inCycle(bfInvert)) {
            String cycle = findCycle(bfInvert);
            int inverted = cycle.indexOf(bfInvert) - 1;
            if (inverted == -1) {
                inverted += cycle.length();
            }
            return alphabet().toInt(cycle.charAt(inverted));
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!alphabet().contains(p)) {
            throw error("Character " + p
                    + " is not in the Alphabet of this permutation!");
        }
        int permuted = permute(alphabet().toInt(p));
        return alphabet().toChar(permuted);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!alphabet().contains(c)) {
            throw error("Character " + c
                    + " is not in the Alphabet of this permutation!");
        }
        int inverted = invert(alphabet().toInt(c));
        return alphabet().toChar(inverted);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int count = 0; count < size(); count += 1) {
            if (permute(alphabet().toChar(count)) == alphabet().toChar(count)) {
                return false;
            }
        }
        return true;
    }

    /** returns boolean of whether or not c is in _cycles.
     * @param c character you're checking. */
    private boolean inCycle(char c) {
        for (String cy: _cycles) {
            if (cy.contains(String.valueOf(c))) {
                return true;
            }
        }
        return false;
    }

    /** Returns the cycle where character is in _cycles.
     * @param c character you're checking. */
    private String findCycle(char c) {
        for (String cy :_cycles) {
            if (cy.contains(String.valueOf(c))) {
                return cy;
            }
        }
        return "";
    }

    /** Checks if a character occurs once in a string.
     * @param string character you're checking. */
    private static void occurOnce(String string) {
        for (int i = 0; i < string.length(); i += 1) {
            StringBuilder sb = new StringBuilder(string);
            String ch = String.valueOf(sb.charAt(i));
            sb.deleteCharAt(i);
            if (sb.toString().contains(ch)) {
                throw error(ch + " occurs more than once in a cycle!");
            }
        }
    }

    /** Checks if a every character in a cycle is in the alphabet.
     * @param cycle Cycle you want to test.
     * @param alphabet Alphabet you want to check. */
    private void charInAlpha(String cycle, Alphabet alphabet) {
        for (int i = 0; i < cycle.length(); i += 1) {
            if (!alphabet.contains(cycle.charAt(i))) {
                throw error("Character "
                        + cycle.charAt(i) + " is not in alphabet.");
            }
        }
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Permutation cycles. */
    private ArrayList<String> _cycles;

}
