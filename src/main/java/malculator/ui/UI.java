package malculator.ui;

import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import malculator.shared.Input;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class UI {
    public static final boolean VSYNC = false;

    public static double deltaTime;
    public static double fps;

    private static long window;

    private static ImGuiImplGlfw implGlfw;
    private static ImGuiImplGl3 implGl3;

    private static double fpsUpdateTimer;

    public static void setup() {
        if (!glfwInit())
            throw new IllegalStateException("Could not initialize GLFW");

        window = glfwCreateWindow(1280, 720, "Malculator", 0L, 0L);
        //glfwMaximizeWindow(window); //Don't need that
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

        glfwSetCharCallback(window, UI::charCallback);

        GL.createCapabilities();

        glClearColor(1f, 0f, 1f, 1f);

        ImGui.createContext();

        implGlfw = new ImGuiImplGlfw();
        implGl3 = new ImGuiImplGl3();

        //var io = ImGui.getIO();
        //io.addConfigFlags(ImGuiConfigFlags.DockingEnable | ImGuiConfigFlags.ViewportsEnable);

        implGlfw.init(window, true);
        implGl3.init();

        setStyle();

        glfwSwapInterval(VSYNC ? 1 : 0);
    }

    public static void setStyle() {
        ImGui.getStyle().setWindowRounding(0f);
    }

    public static void loop() {
        var lastTime = 0.0;
        while (!glfwWindowShouldClose(window)) {
            deltaTime = glfwGetTime() - lastTime;
            lastTime = glfwGetTime();
            fps = 1.0 / deltaTime;

            glfwSwapBuffers(window);
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            implGlfw.newFrame();
            ImGui.newFrame();

            render();

            ImGui.render();
            implGl3.renderDrawData(ImGui.getDrawData());

            /*if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                var backupCurrentContext = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupCurrentContext);
            }*/ //Always false

            fpsUpdateTimer = fpsUpdateTimer > 0.1 ? 0.0 : fpsUpdateTimer + deltaTime;
            if (fpsUpdateTimer == 0) savedFps = fps;
        }
    }

    private static double savedFps;

    public static void render() {
        ImGui.setNextWindowPos(0, 0);
        ImGui.setNextWindowSize(ImGui.getMainViewport().getSizeX(), ImGui.getMainViewport().getSizeY());
        ImGui.begin("Content", ImGuiWindowFlags.NoDecoration);

        ImGui.text("Hello World!");
        ImGui.text("Your fps: " + (int) (savedFps + 0.5));
        ImGui.text("");

        ASTRenderer.renderCalculation(ASTRenderer.test);

        ImGui.text(Input.input);

        ImGui.end();
    }

    public static void shutdown() {
        implGlfw.dispose();
        implGl3.dispose();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private static void charCallback(long window, int codepoint) {
        Input.input += (char) codepoint;
    }

    private static void keyCallback() {
        //TODO
    }
}
