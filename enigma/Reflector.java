package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Michael Chang
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw error("Permutation of a Reflector needs to be deranged!");
        }
    }

    @Override
    boolean reflecting() {
        return true;
    }

    @Override
    int convertBackward(int e) {
        throw error("Reflector can only convert forward, never backwards!");
    }

    @Override
    void set(int posn) {
        if (posn != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    void set(char cposn) {
        if (alphabet().toInt(cposn) != 0) {
            throw error("reflector has only one position");
        }
    }

    @Override
    public String toString() {
        return "Reflector " + name();
    }

}
