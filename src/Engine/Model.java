package Engine;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL11;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.lwjgl.opengl.GL20;

public class Model {
    /* This is much more efficient for drawing because, although the bus between the CPU and the GPU is very wide,
    a bottleneck for drawing performance is created when drawing operations stall to send OpenGL commands from the 
    CPU to the GPU. To avoid this we try to keep as much data and processing on the graphics hardware as we can.  */


    private int drawCount;
    private int vID;
    private int tID;

    private int iID; 

    public Model(float[] vertices, float[] texture, int[] indices) {
        drawCount = indices.length;

        //send vertices to the GPU
        vID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vID);   // name of the buffer, and the type of buffer
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL15.GL_STATIC_DRAW);  // put data into buffer

        //send texture coordinates to the GPU
        tID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createFloatBuffer(texture), GL15.GL_STATIC_DRAW);

        //send indices to the GPU
        iID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iID);

        IntBuffer buffer = BufferUtils.createIntBuffer(indices.length);
        buffer.put(indices);
        buffer.flip();

        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);

        // unbind the buffer, dont want to accidentally change it
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public void render() {
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vID);
        GL20.glVertexAttribPointer(0, 3, GL20.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, tID);

        GL20.glVertexAttribPointer(1,2, GL20.GL_FLOAT, false, 0, 0);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, iID);

        GL11.glDrawElements(GL11.GL_TRIANGLES, drawCount, GL11.GL_UNSIGNED_INT, 0);
        

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
