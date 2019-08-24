package com.spaceinvds.weapons;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.spaceinvds.handlers.ClosestCallback;
import com.spaceinvds.handlers.MyInput;

import static com.spaceinvds.handlers.B2DVar.PPM;

public class Rifle implements Disposable {

    private World world;
    private Body playerBody;

    public Rifle(World world, Body playerBody) {
        this.world = world;
        this.playerBody = playerBody;
    }

    public void trace() {
        Vector2 mousePos = MyInput.currentMousePos();

        float mouseAng = new Vector2(mousePos.x, mousePos.y).sub(playerBody.getPosition()
                .scl(PPM)).scl(-1).angle();

        Vector2 lineDir = new Vector2(1, 1).setLength(99999999).setAngle(mouseAng);

        Vector2 closestPoint = new Vector2(playerBody.getPosition().scl(PPM).sub(lineDir));

        ClosestCallback callback = new ClosestCallback();
        world.rayCast(callback, playerBody.getPosition(), closestPoint);
        if (callback.getClosestPoint() != null) closestPoint.set(callback.getClosestPoint().scl(PPM));
    }

    @Override
    public void dispose() {

    }
}
