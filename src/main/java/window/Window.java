package window;

import core.Scene;
import input.KeyListener;
import input.MouseListener;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import screen.LevelEditorScene;
import screen.LevelScene;
import util.Time;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {

    @Getter
    @Setter
    private int width, height;
    @Getter
    @Setter
    private String title;

    private static Window instance;
    private long glfwWindow;

    private static Scene currentScene = null;

    public float r = 1.0f, g = 1.0f, b = 1.0f, a = 1.0f;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Pixel engine";
    }

    public static void changeScene(int newScene) {
        switch (newScene) {
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
        currentScene.init();
    }

    public static Window getInstance() {
        if (instance == null) {
            instance = new Window();
        }

        return instance;
    }

    public void run() {
        System.out.println("Running LWJGL " + Version.getVersion() + ".");

        init();
        loop();
        free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW.");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        glfwWindow = glfwCreateWindow(width, height, title, NULL, NULL);
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Failed to create GLFW window.");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);        // Enable V-Sync

        glfwShowWindow(glfwWindow); // Make window visible

        GL.createCapabilities();

        Window.changeScene(0);
    }

    private void loop() {
        Time.TimeInfo frameData = new Time.TimeInfo(Time.getTime(), Time.getTime(), 0.0f);

        while (!glfwWindowShouldClose(glfwWindow)) {
            pollAndClearScreen();

            currentScene.update(frameData.getDt());

            updateFrameData(frameData);
        }
    }

    private void pollAndClearScreen() {
        // Poll events
        glfwPollEvents();
        // Clear screen
        glClearColor(r, g, b, 1.0f);
        glClear(GL_COLOR_BUFFER_BIT);
    }

    private void updateFrameData(Time.TimeInfo frameData) {
        glfwSwapBuffers(glfwWindow);
        frameData.tick();
    }

    private void free() {
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }


}
