package enigma;

import java.util.HashSet;
import java.util.Collection;
import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Michael Chang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        if (1 >= numRotors) {
            throw error("Number of rotors needs to be greater than 1!");
        }
        if (pawls < 0) {
            throw error(
                    "Number of pawls needs to be greater than or equal to 0!");
        }
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotorsUsed = new Rotor[_numRotors];
        _plugBoard = new Permutation("", alpha);
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    public void insertRotors(String[] rotors) {
        int count = 0;
        if (rotors.length != numRotors()) {
            throw error("You tried to insert the wrong amount of rotors!");
        }
        for (int i = 0; i < rotors.length; i += 1) {
            checkRotor(rotors[i]);
            for (Rotor used: _allRotors) {
                if (rotors[i].toUpperCase().equals(used.name().toUpperCase())) {
                    _rotorsUsed[i] = used;
                    if (used.rotates()) {
                        count += 1;
                    }
                    break;
                }
            }
        }
        if (count != numPawls()) {
            throw error("Number of pawls must be "
                    + "equal to number of Moving Rotors!");
        }
        checkOrder();
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 upper-case letters. The first letter refers to the
     *  leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw error("Wrong amount of settings for this machine.");
        }
        for (int i = 1; i < setting.length() + 1; i += 1) {
            _rotorsUsed[i].set(setting.charAt(i - 1));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugBoard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceSet();
        c = _plugBoard.permute(c);
        for (int p = numRotors() - 1; p > -1; p -= 1) {
            c = _rotorsUsed[p].convertForward(c);
        }
        for (int i = 1; i < numRotors(); i += 1) {
            c = _rotorsUsed[i].convertBackward(c);
        }
        c = _plugBoard.permute(c);
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        msg = msg.replaceAll("\\s+", "");
        msg = msg.toUpperCase();
        for (int i = 0; i < msg.length(); i += 1) {
            result += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Places all rotors that need to be advanced
    into a set and advances them. */
    private void advanceSet() {
        HashSet<Rotor> adset = new HashSet<>();
        if (_rotorsUsed[_rotorsUsed.length - 1].rotates()) {
            adset.add(_rotorsUsed[_rotorsUsed.length - 1]);
            for (int i = _rotorsUsed.length - 1;
                 _rotorsUsed[i].rotates(); i -= 1) {
                if (_rotorsUsed[i].atNotch() && _rotorsUsed[i - 1].rotates()) {
                    adset.add(_rotorsUsed[i]);
                    if (_rotorsUsed[i - 1].rotates()) {
                        adset.add(_rotorsUsed[i - 1]);
                    }
                }
            }
            for (Rotor adv: adset) {
                adv.advance();
            }
        }
    }

    /** Checks if order of rotors is correct. Throws error otherwise. */
    private void checkOrder() {
        if (!_rotorsUsed[0].reflecting()) {
            throw error("First rotor must be a Reflector!");
        }
        for (int i = _rotorsUsed.length - 1;
             i > _rotorsUsed.length - numPawls() - 1; i -= 1) {
            if (_rotorsUsed[i].reflecting()) {
                throw error("There can only be one reflector!");
            }
            if (!_rotorsUsed[i].rotates()) {
                throw error("Rotors are not in correct order!");

            }
        }
        for (int i = 1; i < _rotorsUsed.length - numPawls(); i += 1) {
            if (_rotorsUsed[i].rotates()) {
                throw error("Rotors are not in correct order!");
            }
            if (_rotorsUsed[i].reflecting()) {
                throw error("There can only be one reflector!");
            }

        }
    }

    /** Checks if Rotor name is in set of available rotors.
     *  Throws error otherwise.
     * @param name is the Rotor's name. */
    private void checkRotor(String name) {
        ArrayList<String> rotorNames = new ArrayList<>();
        for (Rotor rotor: _allRotors) {
            rotorNames.add(rotor.name().toUpperCase());
        }
        if (!rotorNames.contains(name.toUpperCase())) {
            throw error("Rotor " + name
                    + " is not found in the set of available rotors!");

        }
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** number of rotors in the machine. */
    private int _numRotors;

    /** number of pawls in the machine. */
    private int _pawls;

    /** Collection of all rotors. */
    private Collection<Rotor> _allRotors;

    /** list of rotors that are used in machine. */
    private Rotor[] _rotorsUsed;

    /** plugboard permutation. */
    private Permutation _plugBoard;

}
