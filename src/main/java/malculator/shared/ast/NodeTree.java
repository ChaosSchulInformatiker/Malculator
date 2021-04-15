package malculator.shared.ast;

public class NodeTree {

    Node root;

    public NodeTree() {
        root = new Node(1);
    }

    public void setRoot(Node node) {
        root.children[0] = node;
        node.parent = root;
    }

    @Override
    public String toString() {
        return root.children[0].toString();
    }

    public String toFunction() { return root.children[0].toFunction(); }

    public Node solve(Node node) {
        return switch(node.token.getTokenType()) {
            case PLUS -> solve(node.children[0]).plus(solve(node.children[1]),true);
            case MIN -> solve(node.children[0]).plus(solve(node.children[1]),false);
            case MUL -> solve(node.children[0]).times(solve(node.children[1]));
            case DIV -> solve(node.children[0]).div(solve(node.children[1]));
            case POW -> solve(node.children[0]).pow(solve(node.children[1]));
            default -> node;
        };

    }

    public void solve() {
        solve(root.children[0]);
    }

    public void dsolve(Node node) {
        node.derivative();
    }

    public void dSolve() {
        dsolve(root.children[0]);
    }

}
