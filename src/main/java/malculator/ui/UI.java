package malculator.ui;

import imgui.ImGui;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class UI {
    public static double deltaTime;

    private static long window;

    private static ImGuiImplGlfw implGlfw;
    private static ImGuiImplGl3 implGl3;

    public static void setup() {
        if (!glfwInit())
            throw new IllegalStateException("Could not initialize GLFW");

        window = glfwCreateWindow(1280, 720, "Malculator", 0L, 0L);
        glfwMaximizeWindow(window);
        glfwShowWindow(window);

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        glClearColor(1f, 0f, 0f, 1f);

        ImGui.createContext();

        implGlfw = new ImGuiImplGlfw();
        implGl3 = new ImGuiImplGl3();

        var io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable | ImGuiConfigFlags.ViewportsEnable);

        implGlfw.init(window, true);
        implGl3.init();
    }

    public static void loop() {
        var lastTime = 0.0;
        while (!glfwWindowShouldClose(window)) {
            deltaTime = glfwGetTime() - lastTime;
            lastTime = glfwGetTime();

            glfwSwapBuffers(window);
            glfwPollEvents();

            glClear(GL_COLOR_BUFFER_BIT);

            implGlfw.newFrame();
            ImGui.newFrame();

            render();

            ImGui.render();
            implGl3.renderDrawData(ImGui.getDrawData());

            if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
                var backupCurrentContext = glfwGetCurrentContext();
                ImGui.updatePlatformWindows();
                ImGui.renderPlatformWindowsDefault();
                glfwMakeContextCurrent(backupCurrentContext);
            }
        }
    }

    public static void render() {
        ImGui.text("Hello World!");
    }

    public static void shutdown() {
        implGlfw.dispose();
        implGl3.dispose();

        glfwDestroyWindow(window);
        glfwTerminate();
    }
}
