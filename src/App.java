import org.joml.Matrix4f;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class App {

    // The window handle
	private long window;

	private int width = 600;
	private int height = 600;


    public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

    private void init() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		window = glfwCreateWindow(width, height, "Hello World!", NULL, NULL);
		if ( window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(window, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				window,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		GL11.glEnable(GL11.GL_TEXTURE_2D);


		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		float[] vertices = {
			-0.5f, 0.5f, 0f, //V0
			0.5f, 0.5f, 0f, //V1
			0.5f, -0.5f, 0f, //V2

			-0.5f, -0.5f, 0f, //V3
		};

		float[] textureCoords = {
			0, 0,
			1, 0,
			1, 1,

			0, 1,
		};

		int[] indices = {
			0, 1, 2,
			2, 3, 0
		};

		model model = new model(vertices, textureCoords, indices);

		//create Texture
		Texture texture = new Texture("images.jpg");
		Shader shader = new Shader("shader");

		Matrix4f projection = new Matrix4f().ortho2D(-width/2, width/2, -height/2, height/2);

		Matrix4f scale = new Matrix4f().scale(250, 170, 1);

		Matrix4f translate = new Matrix4f().translate(0.1f, 0, 0);

		Matrix4f target = new Matrix4f();
		projection.mul(scale, target);
		target.mul(translate);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
        while(!GLFW.glfwWindowShouldClose(window)) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	
			shader.bind();
			shader.setUniform("sample", 0);
			shader.setUniform("projection", target);
			model.render();
			texture.bind(0);

			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();
        }
	}

    public static void main(String[] args) throws Exception {
        new App().run();
    }
}
