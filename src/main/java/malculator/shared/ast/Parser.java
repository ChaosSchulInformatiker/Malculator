package malculator.shared.ast;

import static malculator.shared.ast.Token.TokenType.*;
import java.util.Arrays;

public class Parser {

    Token[] tokens;
    NodeTree tree = new NodeTree();

    public Parser(Token[] tokens) {
        this.tokens = tokens;
        tokensToTree(tokens, null);
    }

    public void tokensToTree(Token[] tokens, Node parent) {
        if(tokens.length == 1) {
            if(parent != null) new Node(tokens[0],parent);
            else tree.setRoot(new Node(tokens[0], null));
            return;
        }
        boolean PMSkip,MDSkip,POWSkip;
        PMSkip = true;
        MDSkip = POWSkip = false;

        for(int i=tokens.length-1; i >= 0; i--) {
            var token = tokens[i];
            if(token.getTokenType() == RB) {
                boolean ClosingPBisLastToken = i == tokens.length-1;
                int bracketLvl = 1;
                while(!(tokens[i].getTokenType() == LB && bracketLvl == 0)) {
                    i--;
                    if(tokens[i].getTokenType() == LB) bracketLvl--;
                    else if(tokens[i].getTokenType() == RB) bracketLvl++;
                }
                boolean OpeningPBisFirstToken = i == 0;
                if(ClosingPBisLastToken && OpeningPBisFirstToken) {
                    tokensToTree(Arrays.copyOfRange(tokens,i+1,tokens.length-1),parent);
                    break;
                }
            }
            else if(PMSkip && (token.getTokenType() == PLUS || token.getTokenType() == MIN)) {
                loopFunc(tokens,token,parent,i);
                break;
            }else if(MDSkip && (token.getTokenType() == MUL || token.getTokenType() == DIV)) {
                loopFunc(tokens,token,parent,i);
                break;
            }else if(POWSkip && token.getTokenType() == POW) {
                loopFunc(tokens,token,parent,i);
                break;
            }
            if(i==0){
                if(PMSkip) {
                    PMSkip = false;
                    MDSkip = true;
                    i = tokens.length;
                    continue;
                }
                if(MDSkip) {
                    MDSkip = false;
                    POWSkip = true;
                    i = tokens.length;
                    continue;
                }
                POWSkip = false;
                PMSkip = true;
                i = tokens.length;
            }
        }
    }

    public void loopFunc(Token[] tokens, Token token, Node parent, int i) {
        Node node = new Node(token, parent);
        if(parent == null) {
            tree.setRoot(node);
        }
        Token[] left = Arrays.copyOfRange(tokens,0,i);
        tokensToTree(left,node);
        Token[] right = Arrays.copyOfRange(tokens, i+1,tokens.length);
        tokensToTree(right,node);
    }

    public NodeTree getTree() {
        return tree;
    }

}
