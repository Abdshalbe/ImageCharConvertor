package ascii_art;

import java.io.IOException;
import java.util.function.Consumer;
/**
 * Shell for the ASCII art algorithm.
 * responsible for AsciiArtAlgorithm's user interface.
 * */
public class Shell {
    private static final int DEFAULT_RES = 2;
    private static final char[] STARTING_CHARS = new char[]{'0', '1', '2', '3', '4', '5' ,'6', '7', '8', '9'};
    private static final int MAX_CHAR = 127;
    private static final int MIN_CHAR = 32;
    private static final int LAST_INDEX = 2;
    private static final int INCREASE_FACTOR = 2;
    private static final float DECREASE_FACTOR = 0.5f;
    private static final int ADD_SIZE = 4;
    private static final int REMOVE_SIZE = 7;
    private static final int OUTPUT_SIZE = 7;
    private static final int RES_SIZE = 4;
    private static final int ROUND_SIZE = 6;
    private final AsciiArtAlgorithm asciiArtAlgorithm;

    /**
     * main method for the shell.
     * @param args the first argument should be the name of the image to be converted to ASCII art.
     * */
    public static void main(String[] args) {
        try {
            Shell shell = new Shell(args[0]);
            System.out.println("Enter a command:");
            System.out.print(">>> ");
            String input = KeyboardInput.readLine();
            shell.run(input);
        } catch (IOException e) {
            System.err.println("Failed to load image or initialize ASCII art algorithm: " + e.getMessage());
        }
    }

    /**
     * constructor for the shell.
     * @param imageName the name of the image to be converted to ASCII art.
     * @throws IOException if the image cannot be loaded.
     * */
    public Shell(String imageName) throws IOException {
        this.asciiArtAlgorithm = new AsciiArtAlgorithm(imageName, STARTING_CHARS, DEFAULT_RES);
    }

    /**
     * runs the shell.
     * The shell will prompt the user for commands.
     * */
    public void run(String input) {

        while (true) {
            if (input.startsWith("exit")) {
                return ;
            } else if (input.startsWith("chars")) {
                printChars();
            } else if (input.startsWith("add")) {
                if (input.length() <= ADD_SIZE ) {
                    System.out.println("Did not add, Wrong format.");
                }else {
                    String value = input.substring(ADD_SIZE);
                    handleChars(value, asciiArtAlgorithm::addChar, "add");
                }
            } else if (input.startsWith("remove")) {
                if (input.length() <= REMOVE_SIZE-1) {
                    System.out.println("Did not remove, Wrong format.");
                }else {
                    String value = input.substring(REMOVE_SIZE);
                    handleChars(value, asciiArtAlgorithm::removeChar, "remove");
                }
            } else if (input.startsWith("res")) {
                handleResolution(input);
            } else if (input.startsWith("output")) {
                if (input.length() <= OUTPUT_SIZE) {
                    System.out.println("Did not output method due to incorrect format.");
                }else{
                    String value = input.substring(OUTPUT_SIZE);
                    if (!asciiArtAlgorithm.setRenderer(value)) {
                        System.out.println("Did not output method due to incorrect format.");
                    }
                }
            } else if (input.startsWith("round")) {
                if (input.length() <= ROUND_SIZE) {
                    System.out.println("Did not change rounding method due to incorrect format.");
                }else {
                    String value = input.substring(ROUND_SIZE);
                    handleRounding(value);
                }
            } else if (input.startsWith("asciiArt")) {
                if (asciiArtAlgorithm.getCharSet().length <= 1) {
                    System.out.println("Did not execute. Charset is too small.");
                } else {
                    asciiArtAlgorithm.apply();
                }
            } else {
                System.out.println("Did not recognize command.");
            }
            System.out.print(">>> ");
            input = KeyboardInput.readLine();
        }
    }

    /**
     * handles the input for the rounding command.
     * @param input the input for the rounding command.
     * */
    private void handleRounding(String input) {
        if (input.startsWith("up")) {
            asciiArtAlgorithm.setRounder("upper");
        } else if (input.startsWith("down")) {
            asciiArtAlgorithm.setRounder("lower");
        } else if (input.startsWith("abs")) {
            asciiArtAlgorithm.setRounder("abs");
        } else {
            System.out.println("Did not change rounding method due to incorrect format.");
        }
    }

    /**
     * handles the input for the resolution command.
     * @param input the input for the resolution command.
     * The input should be in the format of "res up" or "res down"
     * */
    private void handleResolution(String input) {
        String value = input.substring(RES_SIZE);
        if (value.startsWith("up")) {
            boolean changed = asciiArtAlgorithm.changeResolution(INCREASE_FACTOR);
            if (changed) {
                System.out.println("Resolution set to " + asciiArtAlgorithm.getResolution());
            } else {
                System.out.println("Did not change resolution due to incorrect format.");
            }
        } else if (value.startsWith("down")) {
            boolean changed = asciiArtAlgorithm.changeResolution(DECREASE_FACTOR);
            if (changed) {
                System.out.println("Resolution set to " + asciiArtAlgorithm.getResolution());
            } else {
                System.out.println("Did not change resolution due to incorrect format.");
            }
        } else if (value.isEmpty()) {
            System.out.println("Resolution set to " + asciiArtAlgorithm.getResolution());
        } else {
            System.out.println("Did not change resolution due to incorrect format.");
        }
    }

    /**
     * Prints the current charset with readable characters or ASCII codes.
     */
    private void printChars() {
        for (char c : asciiArtAlgorithm.getCharSet()) {
            if (c >= MIN_CHAR && c < MAX_CHAR) {
                System.out.print(c + " ");
            } else {
                System.out.print("(" + (int) c + ") ");
            }
        }
        System.out.println();
    }

    /**
     * Handles the input for the add and remove commands.
     */
    private void handleChars(String input, Consumer<Character> charAction, String funcName) {
        if (input.isEmpty()) {
            return;
        }
        if (input.length() == 1) {
            charAction.accept(input.charAt(0));
        } else if (input.startsWith("all")) {
            for (int i = MIN_CHAR; i < MAX_CHAR; i++) {
                charAction.accept((char) i);
            }
        } else if (input.startsWith("space")) {
            charAction.accept(' ');
        } else if (input.matches(".-.")) {
            char start = input.charAt(0);
            char end = input.charAt(LAST_INDEX);
            if (start <= end) {
                for (char c = start; c <= end; c++) {
                    charAction.accept(c);
                }
            } else {
                for (char c = start; c >= end; c--) {
                    charAction.accept(c);
                }
            }
        } else {
            System.out.printf("Did not %s due to incorrect format.%n", funcName);
        }
    }
}
