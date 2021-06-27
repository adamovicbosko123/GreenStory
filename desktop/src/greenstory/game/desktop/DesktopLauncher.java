package greenstory.game.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import greenstory.game.GreenStory;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 900;
        config.height = 500;
        config.resizable = true;
        config.addIcon("icon.png", Files.FileType.Internal);
        new LwjglApplication(new GreenStory(), config);
    }
}
