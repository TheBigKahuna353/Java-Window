import org.joml.Matrix4f;
import org.joml.Vector3f;

import Engine.Camera;
import Engine.Shader;
import Engine.Tile;
import Engine.TileRenderer;

public class World {
    
    private int width, height;

    private byte[] tiles;

    private Matrix4f world;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        tiles = new byte[width * height];
        generateWorld();
        world = new Matrix4f().setTranslation(new Vector3f(0)).scale(30);
    }

    public void generateWorld() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = Tile.test_tile.getId();
        }
    }

    public byte getTile(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return 0;
        }
        return tiles[x + y * width];
    }

    public void setTile(int x, int y, byte id) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return;
        }
        tiles[x + y * width] = id;
    }

    public void render(TileRenderer renderer, Shader shader, Camera camera) {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                renderer.renderTile(getTile(x, y), x, y, shader, world, camera);
            }
        }
    }

}

