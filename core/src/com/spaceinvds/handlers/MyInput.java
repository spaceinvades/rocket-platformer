/*
 * Decompiled with CFR 0_125.
 *
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.spaceinvds.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class MyInput {
    public static int jumpKey = 51;
    public static int leftKey = 29;
    public static int rightKey = 32;
    static boolean leftMouseClick;
    private static boolean oldLeftMouseClick;
    private static Vector2 mousePos = new Vector2();
    private static Vector3 mousePos3 = new Vector3();
    static boolean[] keys;
    private static boolean[] oldkeys;

    public static void update() {
        int i = 0;
        do {
            if (i >= keys.length) {
                oldLeftMouseClick = leftMouseClick;
                return;
            }
            MyInput.oldkeys[i] = keys[i];
            ++i;
        } while (true);
    }

    public static boolean isPressed(int keycode) {
        return keys[keycode];
    }

    public static boolean isJustPressed(int keycode) {
        return keys[keycode] && !oldkeys[keycode];
    }

    public static Vector2 currentMousePos() {
        return mousePos.set(Gdx.input.getX(), Gdx.input.getY());
    }

    public static Vector3 currentMousePos3() {
        return mousePos3.set(Gdx.input.getX(), Gdx.input.getY(), 0.0F);
    }

    public static boolean isLeftClicked() {
        return leftMouseClick;
    }

    public static boolean isJustLeftClicked() {
        return leftMouseClick && !oldLeftMouseClick;
    }

    static {
        keys = new boolean[256];
        oldkeys = new boolean[keys.length];
    }
}
