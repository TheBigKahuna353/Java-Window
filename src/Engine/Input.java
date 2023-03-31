package Engine;
import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private static long window;

    private static boolean[] keys = new boolean[GLFW_KEY_LAST];

    public static void init(long window) {
        Input.window = window;
    }

    public static boolean isKeyDown(int key) {
        // returns true every frame the key is down
        return glfwGetKey(window, key) == GLFW_PRESS;
    }

    public static boolean isKeyPressed(int key) {
        // returns true only the first frame the key is down
        return isKeyDown(key) && !keys[key];
    }

    public static boolean isKeyReleased(int key) {
        // returns true only the first frame the key is released
        return !isKeyDown(key) && keys[key];
    }

    public static boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == GLFW_PRESS;
    }

    public static void update() {
        for (int i = 32; i < keys.length; i++) {
            keys[i] = isKeyDown(i);
        }
    }
}
