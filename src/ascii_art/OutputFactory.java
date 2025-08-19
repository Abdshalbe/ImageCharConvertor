package ascii_art;

import ascii_output.AsciiOutput;
import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

/**
 * output factory class
 * */
public class OutputFactory {
    /**
     * create output
     * @param outputType output type
     * @return AsciiOutput
     * */
    public static AsciiOutput createOutput(String outputType) {
        return switch (outputType) {
            case "html" -> new HtmlAsciiOutput("out.html", "Courier New");
            case "console" -> new ConsoleAsciiOutput();
            default -> null;
        };
    }
}
