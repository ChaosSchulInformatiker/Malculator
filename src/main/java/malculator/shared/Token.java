package malculator.shared;

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
