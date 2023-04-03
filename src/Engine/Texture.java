package Engine;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;


public class Texture {
    
    private int id;
    private int width;
    private int height;

    public Texture(String filename) {
        ByteBuffer pixels;
        if (filename == "red") {
            pixels = BufferUtils.createByteBuffer(10 * 10 * 4);

            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    pixels.put((byte) 255);
                    pixels.put((byte) 0);
                    pixels.put((byte) 0);
                    pixels.put((byte) 255);
                }
            }
            this.height = 10;
            this.width = 10;

            pixels.flip();
        } else {
            IntBuffer w = BufferUtils.createIntBuffer(1);
            IntBuffer h = BufferUtils.createIntBuffer(1);
            IntBuffer comp = BufferUtils.createIntBuffer(1);

            pixels = STBImage.stbi_load("res/" + filename, w, h, comp, 4);

            if (pixels == null) {
                throw new RuntimeException("Image file [" + filename + "] not loaded: " + STBImage.stbi_failure_reason());
            }

            this.width = w.get();
            this.height = h.get();
        }

        id = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);

        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pixels);
        STBImage.stbi_image_free(pixels);

    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + sampler);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        }
    }

}
