/*
 * Decompiled with CFR 0_125.
 *
 * Could not load the following classes:
 *  java.lang.String
 */
package com.spaceinvds.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spaceinvds.handlers.MyInput;
import com.spaceinvds.handlers.MyInputProcessor;
import com.spaceinvds.handlers.ScreenManager;

public class Game extends com.badlogic.gdx.Game {
    public static final String TITLE = "Rocket Platformer Draft";
    public static int V_WIDTH = 1280;
    public static int V_HEIGHT = 720;
    public static final float STEP = 1 / 120f;
    public boolean fullscreen = false;
    public boolean vsync = false;
    private ScreenManager sm;
    private OrthographicCamera cam;
    private SpriteBatch sb;
    private FitViewport vp;

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new MyInputProcessor());
        if (fullscreen) Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.graphics.setVSync(vsync);
        sm = new ScreenManager(this);
        cam = new OrthographicCamera();
        cam.setToOrtho(false);
        vp = new FitViewport(V_WIDTH, V_HEIGHT, cam);
        sb = new SpriteBatch();
        //Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("cursor.png")), 2, 2));
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Crosshair);
    }

    @Override
    public void render() {
        Gdx.gl30.glClear(GL30.GL_COLOR_BUFFER_BIT);
        super.render();
        MyInput.update();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void pause() {
    }

    public ScreenManager getSm() {
        return this.sm;
    }

    public SpriteBatch getSb() {
        return sb;
    }

    public OrthographicCamera getCam() {
        return cam;
    }

    public FitViewport getVp() {
        return vp;
    }
}
