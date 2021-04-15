package malculator.shared.ast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import static malculator.shared.ast.Token.*;

import java.util.*;

public class Node {

    Token token;
    Node parent;
    Node[] children;
    int maximumSize;
    int currentSize;
    Calculations calc = new Calculations();
    //Parent is nullable, but token is not
    public Node(@NotNull Token token, @Nullable Node parent) {
        this.token = token;
        this.parent = parent;
        if(parent != null) parent.addChild(this);
        maximumSize = switch(token.getTokenType()) {
            case VAR,NUM,EXP,FRAC,LB,RB -> 0;
            case POW,MUL,PLUS,MIN,DIV -> 2;
        };
        currentSize = 0;
        children = new Node[maximumSize];
    }

    public Node(int maximumSize) {
        this.maximumSize = maximumSize;
        children = new Node[maximumSize];
    }

    public void addChild(@NotNull Node node) {
        if(currentSize < maximumSize) {
            children[currentSize] = node;
            currentSize++;
        }
    }

    public boolean isParent() {
        return currentSize > 0;
    }

    public Node plus(@NotNull Node node, boolean plus) {
        boolean fractions = token instanceof Fraction || node.token instanceof Fraction;

        var sheet1 = node.token.getDenominatorSheet()  == null ? token.getNumeratorSheet() : calc.times(token.getNumeratorSheet(),node.token.getDenominatorSheet());
        var sheet2 = token.getDenominatorSheet()  == null ? node.token.getNumeratorSheet() : calc.times(node.token.getNumeratorSheet(),token.getDenominatorSheet());

        HashMap<Integer,Double> denominatorSheet;
        if(token.getDenominatorSheet() == null) denominatorSheet = node.token.getDenominatorSheet();
        else if(node.token.getDenominatorSheet() == null) denominatorSheet = token.getDenominatorSheet();
        else denominatorSheet = calc.times(token.getDenominatorSheet(), node.token.getDenominatorSheet());

        sheet1 = calc.plus(sheet1,sheet2,plus);

        Node node2 = new Node(fractions ? new Fraction(sheet1,denominatorSheet) : new Expression(hashMapToString(sheet1),sheet1),node.parent.parent);
        node.parent = node2;


        return node2;
    }
    public Node times(@NotNull Node node) {

        boolean fractions = token instanceof Fraction || node.token instanceof Fraction;

        var result = calc.times(token.getNumeratorSheet(),node.token.getNumeratorSheet());

        HashMap<Integer,Double> resultDenominator;
        if(token.getDenominatorSheet() == null) resultDenominator = node.token.getDenominatorSheet();
        else if(node.token.getDenominatorSheet() == null) resultDenominator = token.getDenominatorSheet();
        else resultDenominator = calc.times(token.getDenominatorSheet(), node.token.getDenominatorSheet());

        Node node2 = new Node(fractions ? new Fraction(result,resultDenominator) : new Expression(hashMapToString(result),result),node.parent.parent);
        node.parent = node2;


        return node2;
    }
    public Node div(@NotNull Node node) {
        boolean fractions = token instanceof Fraction || node.token instanceof Fraction;

        var sheet1 = token.getNumeratorSheet();
        var denominatorSheet1 = token.getDenominatorSheet();

        var sheet2 = node.token.getNumeratorSheet();
        var denominatorSheet2 = node.token.getDenominatorSheet();

        var result = new HashMap<Integer,Double>();

        if(sheet2.size() == 1) {

            for(int i : sheet1.keySet()) {
                for(int j : sheet2.keySet()) {
                    result.put(i-j,sheet1.get(i)/sheet2.get(j));
                }
            }

        }
        if(fractions) {
            sheet1 = denominatorSheet2 == null ? sheet1 : calc.times(sheet1,denominatorSheet2);
            sheet2 = denominatorSheet1 == null ? sheet2 : calc.times(denominatorSheet1,sheet2);
        }

        Node node2 = new Node((sheet2.size() == 1 && !fractions) ? new Expression(hashMapToString(result),result) : new Fraction(sheet1,sheet2),node.parent.parent);
        node.parent = node2;


        return node2;

    }
    public Node pow(@NotNull Node node) {
        boolean fractions =  token instanceof Fraction;

        var sheet1 = token.getNumeratorSheet();
        double power = node.token.getNumeratorSheet().get(0);
        var result = sheet1;
        HashMap<Integer,Double> resultDenominator = new HashMap<>();

        if(power == 0) {
            fractions = false;
            result.clear();
            result.put(0,1d);
        }else if(power > 0){
            for (int i = 1; i < power; i++) {
                result = calc.times(result, sheet1);
                if(fractions) resultDenominator = calc.times( i==1 ? token.getDenominatorSheet() : resultDenominator, token.getDenominatorSheet());
            }
        }else {
            resultDenominator = sheet1;
            for(int i=1; i<Math.abs(power); i++) {
                resultDenominator = calc.times(resultDenominator, sheet1); //((x+1)/(x-1))^-2 => (x^2-1/) => 1
                if(fractions) result = calc.times( i==1 ? token.getDenominatorSheet() : result, token.getDenominatorSheet());
            }
            if(!fractions) {
                result.clear();
                result.put(0, 1d);
                fractions = true;
            }
        }

        Node node2 = new Node(fractions ? new Fraction(result,resultDenominator) : new Expression(hashMapToString(result),result),node.parent.parent);
        node.parent = node2;


        return node2;
    }


    public void derivative() {
        boolean fraction = token instanceof Fraction;

        var numeratorSheet = token.getNumeratorSheet();
        var dx = calc.derivative(numeratorSheet);
        var denominatorSheet = token.getDenominatorSheet();
        var dy = calc.derivative(denominatorSheet);

        if(fraction) {

            numeratorSheet = calc.plus(calc.times(dx,denominatorSheet),calc.times(numeratorSheet,dy),false);
            denominatorSheet = calc.times(denominatorSheet,denominatorSheet);

        }

        token = fraction ? new Fraction(numeratorSheet,denominatorSheet) : new Expression(hashMapToString(dx),dx);
    }

    public void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        //TreeORFunction
        buffer.append(prefix);
        buffer.append(token.toString());
        buffer.append('\n');
        for (Iterator<Node> it = Arrays.asList(children).iterator(); it.hasNext();) {
            Node next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }

    public static String hashMapToString(HashMap<Integer,Double> sheet) {
        String output = "";
        List<Integer> keys = new ArrayList<>(sheet.keySet());
        Collections.reverse(keys);
        for(int i : keys) {
            if(sheet.get(i) == 0d) continue;
            output += sheet.get(i)+switch(i) {
                case 0 -> " + ";
                case 1 -> "x + ";
                default -> "x^"+i+" + ";
            };
        }
        if(output.equals("")) return "0";
        output = output.substring(0,output.length()-3);
        //output = output.replace("E","*10^");
        output = output.replace("+ -","- ");
        return output;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    public String toFunction() {

        if(isParent()) {
            return children[0].toFunction()+token.toString()+children[1].toFunction();
        }else {
            return token.toString();
        }

    }

}
