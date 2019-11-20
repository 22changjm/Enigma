package enigma;
import java.util.ArrayList;
import static enigma.EnigmaException.*;

/** Class that extends Alphabet used for Extra Credit Part. You
 * can create an sequence of the alphabet to use.
 @author Michael Chang */
public class SequenceAlphabet extends Alphabet {

    /** Stores the sequence of Alphabets. */
    private ArrayList<Character> _seq;

    /** Create a new sequence Alphabet where you can choose how
     * many characters are in the alphabet.
     * @param seq is the sequence you choose. */
    SequenceAlphabet(String seq) {
        _seq = new ArrayList<>();
        for (int i = 0; i < seq.length(); i += 1) {
            _seq.add(seq.charAt(i));
        }
    }

    @Override
    int size() {
        return _seq.size();
    }

    @Override
    boolean contains(char ch) {
        for (int i = 0; i < size(); i += 1) {
            if (_seq.get(i) == ch) {
                return true;
            }
        }
        return false;
    }

    @Override
    char toChar(int index) {
        if (index > size() - 1) {
            throw error("Character index out of range!");
        }
        return _seq.get(index);
    }

    @Override
    int toInt(char ch) {
        if (!contains(ch)) {
            throw error("Character out of range!");
        }
        return _seq.indexOf(ch);
    }

}
