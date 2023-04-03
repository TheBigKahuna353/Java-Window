package Engine;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    
    private Vector3f position;
    private Matrix4f projection;

    private int width, height;


    public Camera(int width, int height) {
        position = new Vector3f(0, 0, 0);
        projection = new Matrix4f().ortho2D(-width / 2, width / 2, -height / 2, height / 2);
        // projection = new Matrix4f().perspective((float)Math.toRadians(90.0f), (float)width / (float)height, 0.01f, 1000.0f);
        this.width = width;
        this.height = height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
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

    public boolean inView(Vector3f position) {
        if (position.x < this.position.x - width / 2 || position.x > this.position.x + width / 2) {
            return false;
        }
        if (position.y < this.position.y - height / 2 || position.y > this.position.y + height / 2) {
            return false;
        }
        return true;
    }

}
