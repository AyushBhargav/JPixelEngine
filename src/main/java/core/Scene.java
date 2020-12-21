package core;

public abstract class Scene {

    protected Camera camera;
    public Scene(Camera camera) {
        this.camera = camera;
    }

    public void init() {}

    public abstract void update(float dt);
}
