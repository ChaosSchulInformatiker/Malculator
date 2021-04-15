package malculator.shared;

import malculator.shared.ast.*;

public class Function {

    NodeTree tree;
    NodeTree dTree;
    String original_function;
    String simplified_function;
    String derivative;

    public Function(String func) {
        original_function = func;
        Lexer lex = new Lexer(original_function);
        Parser parser = new Parser(lex.getTokens());
        tree = parser.getTree();
        tree.solve();
        simplified_function = tree.toFunction();
        dTree = tree;
        dTree.dSolve();
        derivative = dTree.toFunction();
    }

    public String getSimplified_function() {
        return simplified_function;
    }

    public String getDerivative() {
        return derivative;
    }
}

