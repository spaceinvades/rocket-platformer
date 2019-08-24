package com.spaceinvds.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public interface Projectile extends Disposable {

    void act();

    void draw(SpriteBatch sb);

}
