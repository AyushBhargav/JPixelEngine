package screen;

import core.Camera;
import core.Scene;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private Shader defaultShader;
    private float[] vertexArray = {
            // Position         // Color
            100.5f, -50.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,  // 0
            -0.5f, 100.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,  // 1
            100.5f, 100.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,   // 2
            -0.5f, -0.5f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f  // 3

    };
    // Counter-clockwise order.
    private int[] elementArray = {
            2, 1, 0,
            0, 1, 3
    };

    private int vaoId, vboId, eboId;
    public LevelEditorScene(Camera camera) {
        super(camera);
        this.camera = camera;
    }

    @Override
    public void init() {
        defaultShader = new Shader("/shaders/default.glsl");
        defaultShader.compile();

        // Create VAO
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        // Create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create indices
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        // Create EBO
        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatSize = 4; // bytes
        int vertexSize = (positionSize + colorSize) * floatSize;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSize, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSize, positionSize * floatSize);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {
        camera.translate(new Vector3f(-50.0f, 0.0f, 0.0f), dt);
        defaultShader.use();
        defaultShader.uploadMatrix4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMatrix4f("uView", camera.getViewMatrix());

        // Bind VAO
        glBindVertexArray(vaoId);
        // Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();
    }
}
