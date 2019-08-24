/*
 * Decompiled with CFR 0_125.
 *
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.spaceinvds.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.spaceinvds.game.Game;
import com.spaceinvds.handlers.ScreenManager;

public abstract class GameScreen implements Screen {
    protected final Game game;
    protected ScreenManager sm;
    protected SpriteBatch sb;
    protected FitViewport vp;
    protected OrthographicCamera cam;
    protected boolean toDispose;
    protected boolean isDisposed;

    protected GameScreen(ScreenManager sm) {
        this.sm = sm;
        this.game = sm.getGame();
    }

    public void init() {
        this.sb = game.getSb();
        this.cam = game.getCam();
        this.vp = game.getVp();
    }

    public abstract void handleEvents();

    public abstract void update(float step, float delta);

    protected abstract void realDispose();
}