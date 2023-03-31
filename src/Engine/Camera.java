package Engine;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    
    private Vector3f position;
    private Matrix4f projection;

    public Camera(int width, int height) {
        position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().ortho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void addPosition(Vector3f position) {
        this.position.sub(position);
    }

    public Matrix4f getProjection() {
        Matrix4f target = new Matrix4f();
        projection.mul(new Matrix4f().setTranslation(position), target);
        return target;
    }
}
