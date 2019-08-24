package com.spaceinvds.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.spaceinvds.game.Game;
import com.spaceinvds.handlers.MyInput;
import com.spaceinvds.handlers.ScreenManager;

public class Menu implements Screen {
    private ScreenManager sm;

    public Menu(ScreenManager sm) {
        this.sm = sm;
    }

    public void show() {
        System.out.println("menu");
    }

    public void render(float delta) {
        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.P)) {
            this.sm.changeScreen(Screens.PLAY);
        }
        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.F11) && !sm.getGame().fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            sm.getGame().fullscreen = true;
        } else if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.F11) && sm.getGame().fullscreen) {
            Gdx.graphics.setWindowedMode(Game.V_WIDTH, Game.V_HEIGHT);
            sm.getGame().fullscreen = false;
        }
    }

    public void resize(int width, int height) {
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
        dispose();
    }

    public void dispose() {
    }
}
