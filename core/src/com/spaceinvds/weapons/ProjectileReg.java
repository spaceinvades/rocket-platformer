package com.spaceinvds.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class ProjectileReg<T extends Projectile> {
    private Array<T> register = new Array<>();
    private Array<T> toRem = new Array<>();

    public void addToRemove(T obj) {
        toRem.add(obj);
    }

    public void addToReg(T obj) {
        this.register.add(obj);
    }

    public void destroyAll() {
        for (T t : toRem) {
            t.dispose();
        }
        register.removeAll(toRem, true);
        toRem.clear();
    }

    public void draw(SpriteBatch sb) {
        for (T t : register) {
            t.draw(sb);
        }
    }

    public Array<T> getReg() {
        return register;
    }

    public void dispose() {
        this.register.clear();
    }
}