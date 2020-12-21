package screen;

import core.Camera;
import core.Scene;
import window.Window;

public class LevelScene extends Scene {

    public LevelScene(Camera camera) {
        super(camera);
        System.out.println("Inside level scene.");
        Window.getInstance().r = 1;
        Window.getInstance().g = 1;
        Window.getInstance().b = 1;
    }

    @Override
    public void update(float dt) {

    }
}
