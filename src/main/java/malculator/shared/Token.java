package malculator.shared;

import org.jetbrains.annotations.NotNull;

public interface Token {
    /**
     * Syntax error
     */
    class SyntaxError implements Token {
        @NotNull public static SyntaxError INSTANCE = new SyntaxError();
        private SyntaxError() {}
    }

    /**
     * A number
     */
    class Number implements Token {
        public double value;
    }

    /**
     * + or -
     */
    enum SumOp implements Token {
        PLUS, MINUS
    }

    /**
     * * or /
     */
    enum ProductOp implements Token {
        TIMES, DIV
    }
}
