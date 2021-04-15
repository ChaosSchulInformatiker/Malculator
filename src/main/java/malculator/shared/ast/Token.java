package malculator.shared.ast;

import java.util.HashMap;

public interface Token {

    class Fraction implements Token {

        HashMap<Integer, Double> numeratorSheet;
        HashMap<Integer, Double> denominatorSheet;
        TokenType tokenType;
        String appearance;

        public Fraction(HashMap<Integer, Double> numeratorSheet, HashMap<Integer, Double> denominatorSheet) {
            this.numeratorSheet = numeratorSheet;
            this.denominatorSheet = denominatorSheet;
            tokenType = TokenType.FRAC;
            appearance = "("+Node.hashMapToString(numeratorSheet)+")/("+Node.hashMapToString(denominatorSheet)+")";
        }

        @Override
        public TokenType getTokenType() {
            return tokenType;
        }

        @Override
        public String toString() {
            return appearance;
        }

        @Override
        public HashMap<Integer, Double> getNumeratorSheet() {
            return numeratorSheet;
        }
        @Override
        public HashMap<Integer, Double> getDenominatorSheet() {
            return denominatorSheet;
        }
    }

    class Expression implements Token {

        HashMap<Integer,Double> expressionSheet = new HashMap<>();
        TokenType tokenType;
        String appearance;

        public Expression(String appearance) {
            this.tokenType = TokenType.VAR;
            this.appearance = appearance;
            expressionSheet.put(1,1d);
        }
        public Expression(String appearance, double value) {
            this.tokenType = TokenType.NUM;
            this.appearance = appearance;
            expressionSheet.put(0,value);
        }
        public Expression(String appearance, HashMap<Integer,Double> expressionSheet) {
            this.tokenType = TokenType.EXP;
            this.appearance = appearance;
            this.expressionSheet = expressionSheet;
        }

        @Override
        public TokenType getTokenType() {
            return tokenType;
        }

        @Override
        public String toString() {
            return appearance;
        }

        @Override
        public HashMap<Integer, Double> getNumeratorSheet() {
            return expressionSheet;
        }

        @Override
        public HashMap<Integer, Double> getDenominatorSheet() {
            return null;
        }
    }

    class OP implements Token {

        TokenType tokenType;
        String appearance;

        public OP(TokenType tokenType, String appearance) {
            this.tokenType = tokenType;
            this.appearance = appearance;
        }

        @Override
        public TokenType getTokenType() {
            return tokenType;
        }

        @Override
        public String toString() {
            return appearance;
        }

        @Override @Deprecated
        public HashMap<Integer, Double> getNumeratorSheet() {
            return null;
        }

        @Override @Deprecated
        public HashMap<Integer, Double> getDenominatorSheet() {
            return null;
        }
    }

    enum TokenType {
        VAR /* variable */ ,
        NUM /* number (integer, non-integer) */ ,
        EXP, /* one or multiple NUMs and VARs together (f.e x+1) */
        LB /* first/opening bracket */,
        RB /* second/closing bracket */,
        PLUS /* plus sign */ ,
        MUL /* product sign */ ,
        DIV /* division sign */ ,
        MIN /* minus sign */ ,
        POW /* power sign */,
        FRAC /* fraction */;

        @Override
        public String toString() {
            return super.toString();
        }
    }

    TokenType getTokenType();

    String toString();

    HashMap<Integer,Double> getNumeratorSheet();

    HashMap<Integer,Double> getDenominatorSheet();
}
