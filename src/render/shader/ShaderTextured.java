package render.shader;

public class ShaderTextured extends Shader{
	
	public ShaderTextured() {
		super("Textured.vs", "Textured.fs");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}

	@Override
	protected void getAllUniformLocations() {}
}