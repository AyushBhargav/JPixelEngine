package core;

import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private final Matrix4f projectionMatrix;
    private final Matrix4f viewMatrix;
    @Getter @Setter
    private Vector3f position;

    public Camera(Vector3f position) {
        this.position = position;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.adjustProjection();
    }

    public void adjustProjection() {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 40.0f,
                0.0f, 32.0f * 21.0f, 0.0f, 100.0f);
    }

    public Matrix4f getViewMatrix() {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);
        this.viewMatrix.identity();
        viewMatrix.lookAt(position,
                cameraFront.add(new Vector3f(position.x, position.y, 0.0f)),
                cameraUp);

        return this.viewMatrix;
    }

    public Matrix4f getProjectionMatrix() {
        return this.projectionMatrix;
    }

    public void translate(Vector3f dv, float dt) {
        this.position.x += dv.x * dt;
        this.position.y += dv.y * dt;
        this.position.z += dv.z * dt;
    }
}
