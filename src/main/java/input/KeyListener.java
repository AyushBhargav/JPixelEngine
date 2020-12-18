package input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyListener {

    private static KeyListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyListener() {}

    public static KeyListener getInstance() {
        if (instance == null) {
            instance = new KeyListener();
        }
        return instance;
    }

    public static void keyCallback(long window, int key, int scancode, int action, int mods) {
        if (key >= getInstance().keyPressed.length) {
            return;
        }

        switch (action) {
            case GLFW_PRESS:
                getInstance().keyPressed[key] = true;
                break;
            case GLFW_RELEASE:
                getInstance().keyPressed[key] = false;
                break;
        }
    }

    public static boolean isKeyPressed(int keycode) {
        if (keycode >= getInstance().keyPressed.length) {
            return false;
        }

        return getInstance().keyPressed[keycode];
    }
}
