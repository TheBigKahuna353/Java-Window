
import org.joml.Vector3f;
import org.lwjgl.*;
import org.lwjgl.opengl.*;

import Engine.Camera;
import Engine.Input;
import Engine.Shader;
import Engine.TileRenderer;
import Engine.Timer;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;



public class App {

	// The window handle
	private Window window;

    public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");
		window = new Window(600, 600);

		window.init();
		loop();

		// Free the window callbacks and destroy the window
		window.close();

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
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

		//create Texture
		Shader shader = new Shader("shader");

		Camera camera = new Camera(window.getWidth(), window.getHeight());

		TileRenderer renderer = new TileRenderer();

		World world = new World(10, 10);
		world.setTile(0, 0, (byte)2);

		double frameCap = 1.0/60.0;
		double frameTime = Timer.getTime();
		double unprocessed = 0;
		int frames = 0;
		double frameCounter = 0;
		int total_frames = 0;

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
        while(window.isRunning()) {

			double new_time = Timer.getTime();
			unprocessed += new_time - frameTime;
			frameCounter += new_time - frameTime;
			frameTime = new_time;
			Boolean canRender = false;
			if (frameCounter >= 1.0) {
				System.out.println("FPS: " + frames);
				System.out.println("Total Frames: " + total_frames);
				frames = 0;
				frameCounter = 0;
				total_frames = 0;
			}

			// update/logic code
			// this happens as much as possible, but for 60 times a second, loop stops so render can happen
			while (unprocessed >= frameCap) {
				unprocessed -= frameCap;
				canRender = true;				

				window.update();
				int err = glGetError();
				if (err != GL_NO_ERROR) {
					System.out.println(err);
				}
				total_frames++;
			}
			if (canRender) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
				
				// shader.bind();
				// shader.setUniform("sample", 0);
				// shader.setUniform("projection", camera.getProjection().mul(target));
				// model.render();
				// texture.bind(0);
				if (Input.isKeyDown(GLFW_KEY_W)) {
					camera.addPosition(new Vector3f(0, 5, 0));
				}
				if (Input.isKeyDown(GLFW_KEY_S)) {
					camera.addPosition(new Vector3f(0, -5, 0));
				}
				if (Input.isKeyDown(GLFW_KEY_A)) {
					camera.addPosition(new Vector3f(-5, 0, 0));
				}
				if (Input.isKeyDown(GLFW_KEY_D)) {
					camera.addPosition(new Vector3f(5, 0, 0));
				}

				world.render(renderer, shader, camera);
				frames++;
			}
        }
	}

    public static void main(String[] args) throws Exception {
        new App().run();
    }
}
