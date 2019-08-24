package com.spaceinvds.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Disposable;

public class ClosestCallback implements RayCastCallback, Disposable {

    private Body closestBody;
    private Vector2 closestPoint;

    public ClosestCallback() {
    }

    @Override
    public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
        if (fixture.isSensor()) {
            return -1;
        }
        closestBody = fixture.getBody();
        closestPoint = new Vector2(point);
        return fraction;
    }

    public Vector2 getClosestPoint() {
        if (closestPoint != null) return closestPoint;
        else return null;
    }

    public Body getClosestBody() {
        if (closestBody != null) return closestBody;
        else return null;
    }

    @Override
    public void dispose() {
        closestBody = null;
        closestPoint = null;
    }
}
