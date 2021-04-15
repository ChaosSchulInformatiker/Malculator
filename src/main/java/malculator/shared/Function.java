package malculator.shared;

import malculator.shared.ast.*;

public class Function {

    NodeTree tree;
    NodeTree dTree;
    String original_function;
    String simplified_function;

    public Function(String func) {
        original_function = func;
        Lexer lex = new Lexer(original_function);
        Parser parser = new Parser(lex.getTokens());
        tree = parser.getTree();
        tree.solve();
        simplified_function = tree.toFunction();
    }

    public String getSimplified_function() {
        return simplified_function;
    }

    public String getDerivative() {
        dTree = tree;
        dTree.dSolve();
        return dTree.toString();
    }

    public NodeTree getTree() {
        return tree;
    }

    public NodeTree getdTree() {
        return dTree;
    }
}
