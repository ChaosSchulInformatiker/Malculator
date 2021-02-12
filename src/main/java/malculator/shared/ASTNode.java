package malculator.shared;

import malculator.utils.Either;

public interface ASTNode {
    class Calculation implements ASTNode {
        public Expression calculation;
    }

    class Expression implements ASTNode {
        public Either<PrimaryExpression, Sum> value;
    }

    class Sum implements ASTNode {
        public Product[] children;
        public Token.SumOp[] operators;
    }

    class Product implements ASTNode {
        public Power[] children;
        public Token.ProductOp[] operators;
    }

    class Power implements ASTNode {
        public PrimaryExpression[] children;
    }

    class PrimaryExpression implements ASTNode {
        public Either<Token.Number, Expression> value;
    }

    class Number implements ASTNode {
        public Token.Number number;
        public Token.SumOp[] signs;
    }
}
