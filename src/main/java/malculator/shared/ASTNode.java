package malculator.shared;

import malculator.utils.Either;

public interface ASTNode {
    /**
     * Represents the whole calculation
     * Either an expression or a syntax error
     */
    class Calculation implements ASTNode {
        public Either<Expression, Token.SyntaxError> calculation;
    }

    /**
     * Represents an expression
     */
    class Expression implements ASTNode {
        public Either<PrimaryExpression, Sum> value;
    }

    /**
     * Represents a sum
     * children[0] operators[0] children[1] operators[1] ... children[n-1] operators[n-1] children[n]
     * operators is 1 smaller than children
     */
    class Sum implements ASTNode {
        public Product[] children;
        public Token.SumOp[] operators;
    }

    /**
     * Represents a product
     * children[0] operators[0] children[1] operators[1] ... children[n-1] operators[n-1] children[n]
     * operators is 1 smaller than children
     */
    class Product implements ASTNode {
        public Power[] children;
        public Token.ProductOp[] operators;
    }

    /**
     * Represents a power
     * the children are separated by ^
     */
    class Power implements ASTNode {
        public PrimaryExpression[] children;
    }

    /**
     * A value or a parenthesized expression
     */
    class PrimaryExpression implements ASTNode {
        public Either<Value, Expression> value;
    }

    /**
     * A number with signs
     */
    class Value implements ASTNode {
        public Token.Number number;
        public Token.SumOp[] signs;
    }
}
