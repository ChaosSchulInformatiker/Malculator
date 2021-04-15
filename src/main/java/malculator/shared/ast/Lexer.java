package malculator.shared.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static malculator.shared.ast.Token.*;

public class Lexer {

     String function;
     Token[] tokens;


     public Lexer(String function) {
          this.function = function;
          tokens = stringToTokens();
     }

     private Token[] stringToTokens() {
          //Turns String into List of Strings (f.e "(20x+15)^2" => ["(","20","x","+","15",")","^","2",]

          function = function.replaceAll(" ","");
          String[] split = function.split("((?<=[-+/*^()x])|(?=[-+/*^()x]))");
          List<String> splittedFunction = new ArrayList<>(Arrays.asList(split));

          // Optimizing the outputted strings (f.e solving multiple sign "x++-1" => ["x","+","+","-","1"] => ["x","-","1"]
          // and fixing minus values "x^-3" => ["x","^","-","3"] => ["x","^","-3"]
          // and more importantly adds a * if a number comes after an x "20x" => ["20","x"] => ["20","*","x"]
          fixAll(splittedFunction);

          System.out.println("splitted Function "+splittedFunction);

          Token[] tokens = new Token[splittedFunction.size()];
          for(int i=0; i<splittedFunction.size(); i++) {
               switch (splittedFunction.get(i)) {
                    case "x" -> tokens[i] = new Expression(splittedFunction.get(i));
                    case "+" -> tokens[i] = new OP(Token.TokenType.PLUS, splittedFunction.get(i));
                    case "-" -> tokens[i] = new OP(Token.TokenType.MIN, splittedFunction.get(i));
                    case "*" -> tokens[i] = new OP(Token.TokenType.MUL, splittedFunction.get(i));
                    case "/" -> tokens[i] = new OP(Token.TokenType.DIV, splittedFunction.get(i));
                    case "^" -> tokens[i] = new OP(Token.TokenType.POW, splittedFunction.get(i));
                    case "(" -> tokens[i] = new OP(Token.TokenType.LB, splittedFunction.get(i));
                    case ")" -> tokens[i] = new OP(Token.TokenType.RB, splittedFunction.get(i));
                    default -> tokens[i] = new Expression(Double.toString(parse(splittedFunction.get(i))),parse(splittedFunction.get(i)));
               }
          }
          return tokens;
     }

     private void fixAll(List<String> args) {
          checkandsolvemultiplesigns(args);
          for(int i=0; i<args.size(); i++) {
               var s = args.get(i);

               if(i+2 < args.size() && s.contains("E")) {
                    if(args.get(i+1).equals("-")) {
                         args.remove(i+1);
                         args.set(i,s+"-"+args.get(i+1));
                         args.remove(i+1);
                         s = args.get(i);
                    }
               }

               if(isNumber(s)) {
                    if(i+1 < args.size() && args.get(i+1).equals("x")) {
                         args.add(i+1,"*");
                    }
               }

               if(i+1 < args.size() && s.equals(")") && args.get(i+1).equals("(")) {
                    args.add(i+1,"*");
               }

               if(i==0 && s.matches("[-+]") && !args.get(i+1).matches("[(]")) {
                    if(s.equals("-")) args.set(i+1,"-"+args.get(i+1));
                    args.remove(i);
               }

               if(i < args.size()-1 && s.equals("-")) {
                    if(args.get(i+1).equals("(")) {
                         args.set(i,"-1");
                         args.add(i+1,"*");
                         if(i>0) {
                              args.add(i,"+");
                         }
                    }
               }

               if(i < args.size()-1 && s.matches("[+*^/(]")) {
                    if(args.get(i+1).equals("-")) {
                         args.remove(i+1);
                         args.set(i+1,"-"+args.get(i+1));
                    }
               }

               if(i < args.size()-1 && s.matches("[*^/(]")) {
                    if(args.get(i+1).equals("+")) {
                         args.remove(i+1);
                    }
               }

          }
     }
     private void checkandsolvemultiplesigns(List<String> args) {
          for(int i=0; i<args.size(); i++) {
               if(args.get(i).equals("+")) {
                    if(i < args.size()-1) {
                         if(args.get(i+1).equals("+")) {
                              args.remove(i+1);
                              i = -1;
                         }else if(args.get(i+1).equals("-")) {
                              args.remove(i+1);
                              args.set(i,"-");
                              i = -1;
                         }
                    }
               }else if(args.get(i).equals("-")) {
                    if (i < args.size() - 1) {
                         if (args.get(i + 1).equals("+")) {
                              args.remove(i + 1);
                              i = -1;
                         } else if (args.get(i + 1).equals("-")) {
                              args.remove(i + 1);
                              args.set(i, "+");
                              i = -1;
                         }
                    }
               }
          }
     }
     public double parse(String s) {
          if(s.contains("E")) {
               int powerOfTen = 0;
               for(int i=0; i<s.toCharArray().length;i++) {
                    if(s.charAt(i) == 'E') {
                         powerOfTen = Integer.parseInt(s.substring(i+1));
                         s = s.substring(0,i);
                    }
               }
               return Math.pow(10,powerOfTen)*Double.parseDouble(s);
          }
          return Double.parseDouble(s);
     }
     public boolean isNumber(String s) {
          try {
               parse(s);
          }catch(NumberFormatException e) {
               return false;
          }
          return true;
     }
     public Token[] getTokens() {
          return tokens;
     }
}
