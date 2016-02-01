package biscuitbaker.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import biscuitbaker.game.BiscuitBaker;
import biscuitbaker.game.Config;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Biscuit Baker";
        config.width = Config.SCREEN_WIDTH;
        config.height = Config.SCREEN_HEIGHT;
        //config.samples = 2;
<<<<<<< HEAD
        new LwjglApplication(new BiscuitBaker(Config.DEBUG), config);
=======
        new LwjglApplication(new BiscuitBaker(false), config);
>>>>>>> Added assistant image
    }
}
