package Engine;
import java.util.HashMap;

import org.joml.Matrix4f;

public class TileRenderer {
    
    private HashMap<String, Texture> textures;

    private Model model;

    public TileRenderer() {
        textures = new HashMap<String, Texture>();

        float[] vertices = {
			-1f, 1f, 0f, //V0    	top left
			1f, 1f, 0f, //V1		top right
			1f, -1f, 0f, //V2			bottom right

			-1f, -1f, 0f, //V3		bottom left
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

        // so every tile has the same model, aka the same vertices, texture coordinates, and indices
        // aka, every tile is the starts as the whole window, then gets scaled, rotated, and translated to the right place
		model = new Model(vertices, textureCoords, indices);

        for (int i = 0; i < Tile.tiles.length; i++) {
            if (Tile.tiles[i] != null) {
                if (!textures.containsKey(Tile.tiles[i].getTexture())) {
                    textures.put(Tile.tiles[i].getTexture(), new Texture(Tile.tiles[i].getTexture()));
                }
            }
        }
    }

    public void renderTile(byte id, int x, int y, Shader shader, Matrix4f world, Camera camera) {
        shader.bind();
        if (textures.containsKey(Tile.tiles[id].getTexture())) {
            textures.get(Tile.tiles[id].getTexture()).bind(0);
        } else {
            textures.get("missing").bind(0);
        }
        Matrix4f tile_pos = new Matrix4f().translate(x*2, -y*2, 0);
        Matrix4f target = new Matrix4f();
        camera.getProjection().mul(world, target);
        target.mul(tile_pos);

        shader.setUniform("sample", 0);
        shader.setUniform("projection", target);

        model.render();
    }

}
