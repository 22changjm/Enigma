package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Michael Chang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        if (_input.hasNextLine()) {
            String setting = _input.nextLine();
            if (!setting.contains("*")) {
                throw error("You must put a setting config in your input!");
            } else {
                setUp(machine, setting);
            }
        }
        while (_input.hasNextLine()) {
            String in = _input.nextLine();
            if (in.startsWith("*")) {
                setUp(machine, in);
            } else {
                String decode = machine.convert(in.toUpperCase());
                decode = printMessageLine(decode);
                _output.println(decode);
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alpha = _config.nextLine().replaceAll("\\s+", "");
            if (alpha.contains("-")) {
                if (alpha.charAt(2) < alpha.charAt(0)) {
                    throw error("C2 must be greater than or equal to C1!");
                } else if (alpha.replaceAll("\\s+", "").length() > 3) {
                    throw error("Configuration file not in the right format!");
                }
                _alphabet = new CharacterRange(
                        alpha.charAt(0), alpha.charAt(2));
            } else {
                _alphabet = new SequenceAlphabet(alpha);
            }
            ArrayList<String> setting =  new ArrayList<>(
                    Arrays.asList(_config.nextLine().split("\\s+")));
            setting.remove("");
            if (setting.size() != 2) {
                throw error("Configuration file not in the right format!");
            }
            String strConfig = "";
            while (_config.hasNextLine()) {
                strConfig += _config.nextLine();
                strConfig += " ";
            }
            if (occurence(strConfig, "(")
                    != occurence(strConfig, ")")) {
                throw error("Configuration file not in the right format!");
            }
            strConfig = strConfig.replaceAll(" +", " ");
            ArrayList<String> strRotors = rotorStrings(strConfig);
            ArrayList<Rotor> rotors = new ArrayList<>();
            for (int i = 0; i < strRotors.size(); i += 1) {
                rotors.add(readRotor(strRotors.get(i)));
            }
            return new Machine(_alphabet, Integer.valueOf(setting.get(0)),
                    Integer.valueOf(setting.get(1)), rotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }


    /** Return a rotor, reading its description from _config.
     * @param str rotor description. */
    private Rotor readRotor(String str) {
        try {
            String[] line = str.split("\\s+", 3);
            String name = line[0];
            String type = line[1];
            if (name.contains("(") || name.contains(")")) {
                throw error("Name of rotor must not contain parentheses!");
            }
            Permutation perm = new Permutation(line[2], _alphabet);
            if (type.contains("M")) {
                String notches = type.substring(1);
                if (notches.equals("")) {
                    throw error("Moving Rotor must have a notch!");
                }
                return new MovingRotor(name, perm, notches);
            } else if (type.contains("N")) {
                return new FixedRotor(name, perm);
            } else if (type.contains("R")) {
                return new Reflector(name, perm);
            } else {
                throw error("Configuration must indicate what type of rotor!");
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        if (!settings.contains("*")) {
            throw error("There are no settings for the Machine!");
        }
        String str = settings.substring(1);
        while (str.startsWith(" ")) {
            str = str.substring(1);
        }
        ArrayList<String> setting = new ArrayList<>(
                Arrays.asList(str.split("(\\s+)", M.numRotors() + 2)));
        if (setting.size() < M.numRotors() + 1) {
            throw error("Setting line contains wrong number of arguments!");
        }
        String[] rotors = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i += 1) {
            rotors[i] = setting.get(i);
        }
        M.insertRotors(rotors);
        M.setRotors(setting.get(M.numRotors()));
        if (setting.size() == M.numRotors() + 2) {
            M.setPlugboard(new Permutation(
                    setting.get(M.numRotors() + 1), _alphabet));
        }
    }


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters).
     *  @return the message in groups of five. */
    private String printMessageLine(String msg) {
        return msg.replaceAll(".....", "$0 ");
    }

    /** Returns ArrayList of strings where the
     * strings are the rotor information. @param strRotors */
    private ArrayList<String> rotorStrings(String strRotors) {
        ArrayList<String> result = new ArrayList<>();
        String regex = "[a-zA-Z0-9]+ +[a-zA-Z0-9]+ +( *\\([a-zA-Z0-9]+\\))+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(strRotors);
        while (matcher.find()) {
            String str = matcher.group();
            result.add(str);
            strRotors = strRotors.substring(str.length());
        }
        return result;
    }

    /** Returns how many times a character occurs in a string.
     * @param str The string you are checking.
     * @param occur Should be a character */
    private int occurence(String str, String occur) {
        int occurrences = 0;
        for (char c: str.toCharArray()) {
            if (occur.equals(String.valueOf(c))) {
                occurrences += 1;
            }
        }
        return occurrences;
    }


    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
