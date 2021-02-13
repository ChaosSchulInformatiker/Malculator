package malculator.ui;

import imgui.ImDrawList;
import imgui.ImGui;
import imgui.ImVec2;
import malculator.shared.ASTNode;
import malculator.shared.Token;
import malculator.utils.Either;
import org.jetbrains.annotations.NotNull;

public class ASTRenderer {
    public static final ASTNode.Calculation test = new ASTNode.Calculation() {{
        calculation = new Either.First<>() {{
            value = new Expression() {{
                    value = new Either.Second<>() {{
                        value = new Sum() {{
                            children = new Product[]{
                                    new Product() {{
                                        children = new Power[]{
                                                new Power() {{
                                                    children = new PrimaryExpression[]{
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
                                        children = new Power[]{
                                                new Power() {{
                                                    children = new PrimaryExpression[]{
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
                            operators = new Token.SumOp[]{Token.SumOp.PLUS};
                        }};
                    }};
            }};
        }};
    }};

    public static float posX = 100f, posY = 100f;

    public static final float SPACE = 5f;
    private static boolean useSpace = true;

    private static ImDrawList drawList;
    private static float offX, offY;

    public static void renderCalculation(@NotNull ASTNode.Calculation ctx) {
        drawList = ImGui.getWindowDrawList();
        ctx.calculation.either(ASTRenderer::renderExpression, error -> ImGui.text("Syntax error"));
        offX = offY = 0;
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
        float goBack = 0f; // After rendering the powers the previous y is used
        for (int i = 1; i < ctx.children.length; i++) {
            posX += SPACE / 2f;
            posY -= SPACE;
            goBack += SPACE;
            renderPrimaryExpression(ctx.children[i]);
        }
        posY += goBack;
    }

    public static void renderPrimaryExpression(@NotNull ASTNode.PrimaryExpression ctx) {
        ctx.value.either(ASTRenderer::renderValue, ASTRenderer::renderExpression);
    }

    public static void renderValue(@NotNull ASTNode.Value ctx) {
        useSpace = false;
        for (Token.SumOp sign : ctx.signs) {
            renderSumOp(sign);
        }
        useSpace = true;
        renderNumber(ctx.number);
    }

    public static void renderNumber(@NotNull Token.Number ctx) {
        // Don't know if there's a method for this
        var afterDot = ctx.value * 10.0 - (int) ctx.value * 10;
        var numberString = afterDot == 0.0 ? String.valueOf((int) ctx.value) : String.valueOf(ctx.value);
        var vec = new ImVec2();
        ImGui.calcTextSize(vec, numberString);
        drawList.addText(posX + offX, posY + offY, -1, numberString);
        offX += vec.x;
        if (useSpace) offX += SPACE;
    }

    public static void renderSumOp(@NotNull Token.SumOp ctx) {
        var text = switch (ctx) {
            case PLUS -> "+";
            case MINUS -> "-";
        };
        var vec = new ImVec2();
        ImGui.calcTextSize(vec, text);
        drawList.addText(posX + offX, posY + offY, -1, text);
        offX += vec.x;
        if (useSpace) offX += SPACE;
    }

    public static void renderProductOp(@NotNull Token.ProductOp ctx) {
        var text = switch (ctx) {
            case TIMES -> "×";
            case DIV -> "÷";
        };
        var vec = new ImVec2();
        ImGui.calcTextSize(vec, text);
        drawList.addText(posX + offX, posY + offY, -1, text);
        offX += vec.x;
        if (useSpace) offX += SPACE;
    }
}
