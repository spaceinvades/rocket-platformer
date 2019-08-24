package com.spaceinvds.handlers;

import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {
    @Override
    public boolean keyDown(int keycode) {
        MyInput.keys[keycode] = true;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        MyInput.keys[keycode] = false;
        return true;
    }

    @Override
    public boolean touchDown(int sX, int sY, int pointer, int button) {
        MyInput.leftMouseClick = true;
        return true;
    }

    @Override
    public boolean touchUp(int sX, int sY, int pointer, int button) {
        MyInput.leftMouseClick = false;
        return true;
    }
}
