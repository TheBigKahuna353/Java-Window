import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class Shader {
    
    private int program;
    private int vs;
    private int fs;

    public Shader(String filename) {
        program = GL20.glCreateProgram();
        if (program == 0) {
            System.err.println("Shader creation failed: Could not find valid memory location in constructor");
            System.exit(1);
        }

        vs = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
        GL20.glShaderSource(vs, loadShader(filename + ".vs"));
        GL20.glCompileShader(vs);
        if (GL20.glGetShaderi(vs, GL20.GL_COMPILE_STATUS) == 0) {
            System.err.println(GL20.glGetShaderInfoLog(vs, 1024));
            System.exit(1);
        }

        fs = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
        GL20.glShaderSource(fs, loadShader(filename + ".fs"));
        GL20.glCompileShader(fs);
        if (GL20.glGetShaderi(fs, GL20.GL_COMPILE_STATUS) == 0) {
            System.err.println(GL20.glGetShaderInfoLog(fs, 1024));
            System.exit(1);
        }

        GL20.glAttachShader(program, vs);
        GL20.glAttachShader(program, fs);


        GL20.glBindAttribLocation(program, 0, "vertices");
        GL20.glBindAttribLocation(program, 1, "textures");

        GL20.glLinkProgram(program);
        if (GL20.glGetProgrami(program, GL20.GL_LINK_STATUS) == 0) {
            System.err.println(GL20.glGetProgramInfoLog(program, 1024));
            System.exit(1);
        }
    }

    public void bind() {
        GL20.glUseProgram(program);
    }

    public void setUniform(String name, int value) {
        int location = GL20.glGetUniformLocation(program, name);
        if (location >= 0) {
            GL20.glUniform1i(location, value);
        } else {
            System.err.println("Uniform " + name + " not found!");
        }
    }

    public void setUniform(String name, Matrix4f value) {
        int location = GL20.glGetUniformLocation(program, name);
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        value.get(buffer);
        if (location >= 0) {
            GL20.glUniformMatrix4fv(location, false, buffer);
        } else {
            System.err.println("Uniform " + name + " not found!");
        }
    }

    private String loadShader(String filename) {
        StringBuilder shaderSource = new StringBuilder();
        BufferedReader shaderReader = null;
        try {
            shaderReader = new BufferedReader(new FileReader("res/shaders/" + filename));
            String line;

            while ((line = shaderReader.readLine()) != null) {
                shaderSource.append(line);
                shaderSource.append("\n");
            }
            shaderReader.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return shaderSource.toString();
    }
}
