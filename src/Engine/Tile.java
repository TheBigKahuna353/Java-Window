package Engine;
public class Tile {
    
    private byte id;
    private String texture;

    public static Tile tiles[] = new Tile[256];

    public static final Tile test_tile = new Tile((byte)0, "test.png");
    public static final Tile test_tile2 = new Tile((byte)1, "images.jpg");
    public static final Tile red_tile = new Tile((byte)2, "red");

    public Tile(byte id, String texture) {
        this.id = id;
        this.texture = texture;

        if (tiles[id] != null) {
            throw new RuntimeException("Duplicate tile id on " + id);
        }
        tiles[id] = this;
    }

    public byte getId() {
        return id;
    }

    public String getTexture() {
        return texture;
    }
}
