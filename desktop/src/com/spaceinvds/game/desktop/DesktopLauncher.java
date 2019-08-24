package com.spaceinvds.game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.spaceinvds.game.Game;

//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    public static void main(String[] args) {

        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        config.useOpenGL3(true, 3, 2);
        config.setWindowedMode(Game.V_WIDTH, Game.V_HEIGHT);
        config.setResizable(false);

        new Lwjgl3Application(new Game(), config);

    }
}