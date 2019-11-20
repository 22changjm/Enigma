package enigma;
import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Michael Chang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whos permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initially in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        if (notches.equals(" ")) {
            throw error("Moving Rotor needs to have notches!");
        }
        _notches = notches;
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        set((setting() + 1) % size());
    }

    @Override
    boolean atNotch() {
        char setting = permutation().alphabet().toChar(setting());
        if (_notches.contains(String.valueOf(setting))) {
            return true;
        }
        return false;
    }

    /** notches for the rotor. */
    private String _notches;
}
