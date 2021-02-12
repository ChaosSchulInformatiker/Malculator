package malculator.shared;

public interface Token {
    class Number implements Token {
        public double value;
    }

    enum SumOp implements Token {
        PLUS, MINUS
    }

    enum ProductOp implements Token {
        TIMES, DIV
    }
}
