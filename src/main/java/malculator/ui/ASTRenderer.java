package malculator.ui;

import imgui.ImGui;
import imgui.flag.ImGuiStyleVar;
import malculator.shared.ASTNode;
import malculator.shared.Token;
import malculator.utils.Either;
import org.jetbrains.annotations.NotNull;

public class ASTRenderer {
    public static final ASTNode.Calculation test = new ASTNode.Calculation() {{
        calculation = new Expression() {{
            value = new Either.Second<>() {{
                value = new Sum() {{
                     children = new Product[] {
                           new Product() {{
                               children = new Power[] {
                                       new Power() {{
                                           children = new PrimaryExpression[] {
                                                   new PrimaryExpression() {{
                                                       value = new First<>() {{
                                                           value = new Value() {{
                                                               signs = new Token.SumOp[0];
                                                               number = new Token.Number() {{
                                                                   value = 1.0;
                                                               }};
                                                           }};
                                                       }};
                                                   }}
                                           };
                                       }}
                               };
                               operators = new Token.ProductOp[0];
                           }},
                             new Product() {{
                                 children = new Power[] {
                                         new Power() {{
                                             children = new PrimaryExpression[] {
                                                     new PrimaryExpression() {{
                                                         value = new First<>() {{
                                                             value = new Value() {{
                                                                 signs = new Token.SumOp[0];
                                                                 number = new Token.Number() {{
                                                                     value = 2.5;
                                                                 }};
                                                             }};
                                                         }};
                                                     }}
                                             };
                                         }}
                                 };
                                 operators = new Token.ProductOp[0];
                             }}
                    };
                     operators = new Token.SumOp[] { Token.SumOp.PLUS };
                }};
            }};
        }};
    }};

    public static void renderCalculation(@NotNull ASTNode.Calculation ctx) {
        renderExpression(ctx.calculation);
    }

    public static void renderExpression(@NotNull ASTNode.Expression ctx) {
        ctx.value.either(ASTRenderer::renderPrimaryExpression, ASTRenderer::renderSum);
    }

    public static void renderSum(@NotNull ASTNode.Sum ctx) {
        renderProduct(ctx.children[0]);
        for (int i = 0; i < ctx.operators.length; i++) {
            renderSumOp(ctx.operators[i]);
            renderProduct(ctx.children[i + 1]);
        }
    }

    public static void renderProduct(@NotNull ASTNode.Product ctx) {
        renderPower(ctx.children[0]);
        for (int i = 0; i < ctx.operators.length; i++) {
            renderProductOp(ctx.operators[i]);
            renderPower(ctx.children[i + 1]);
        }
    }

    public static void renderPower(@NotNull ASTNode.Power ctx) {
        renderPrimaryExpression(ctx.children[0]);
        for (int i = 1; i < ctx.children.length; i++) {
            ImGui.text("^");
            ImGui.sameLine();
            renderPrimaryExpression(ctx.children[i]);
        }
    }

    public static void renderPrimaryExpression(@NotNull ASTNode.PrimaryExpression ctx) {
        ctx.value.either(ASTRenderer::renderValue, ASTRenderer::renderExpression);
    }

    public static void renderValue(@NotNull ASTNode.Value ctx) {
        ImGui.pushStyleVar(ImGuiStyleVar.ItemSpacing, 0f, 0f);
        for (Token.SumOp sign : ctx.signs) {
            renderSumOp(sign);
        }
        ImGui.popStyleVar();
        renderNumber(ctx.number);
    }

    public static void renderNumber(@NotNull Token.Number ctx) {
        var afterDot = ctx.value * 10.0 - (int) ctx.value * 10;
        ImGui.text(afterDot == 0.0 ? String.valueOf((int) ctx.value) : String.valueOf(ctx.value));
        ImGui.sameLine();
    }

    public static void renderSumOp(@NotNull Token.SumOp ctx) {
        ImGui.text(
            switch (ctx) {
                case PLUS -> "+";
                case MINUS -> "-";
            }
        );
        ImGui.sameLine();
    }

    public static void renderProductOp(@NotNull Token.ProductOp ctx) {
        ImGui.text(
            switch (ctx) {
                case TIMES -> "×";
                case DIV -> "÷";
            }
        );
        ImGui.sameLine();
    }
}
