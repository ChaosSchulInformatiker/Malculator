package malculator.shared;

import org.jetbrains.annotations.NotNull;

public interface Token {

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
