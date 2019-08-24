package com.spaceinvds.weapons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.spaceinvds.handlers.B2DVar;
import com.spaceinvds.handlers.ClosestCallback;
import com.spaceinvds.handlers.MyInput;

import static com.spaceinvds.handlers.B2DVar.BIT_GROUND;
import static com.spaceinvds.handlers.B2DVar.BIT_PROJECTILE;

public class Rocket implements Projectile {
    private Body rocket;
    private Body playerBody;
    private World world;
    private Body closestBody;
    private boolean isDestroyed;
    private boolean isExploded;
    private Fixture playerFixture;
    private ProjectileReg<Rocket> rocketReg;
    private Texture texture;
    private Vector2 velocity;

    public Rocket(World world, float PPM, Fixture playerFixture, Body playerBody, OrthographicCamera cam,
                  float speed, ProjectileReg<Rocket> rocketReg) {

        PolygonShape shape = new PolygonShape();
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();

        texture = new Texture("rocket.png");

        this.rocketReg = rocketReg;
        this.isDestroyed = false;
        this.isExploded = false;
        this.world = world;
        this.playerBody = playerBody;
        this.playerFixture = playerFixture;
        bdef.position.set(playerBody.getPosition());
        bdef.type = BodyDef.BodyType.DynamicBody;
        fdef.isSensor = false;

        fdef.filter.categoryBits = BIT_PROJECTILE;
        fdef.filter.maskBits = BIT_GROUND;

        shape.setAsBox(4.5f / PPM, 3 / PPM);
        fdef.shape = shape;
        rocket = world.createBody(bdef);
        Fixture rocketFix = rocket.createFixture(fdef);
        rocket.setGravityScale(0.0F);
        rocket.setLinearDamping(0.0F);

        Vector3 mousePos = MyInput.currentMousePos3();

        cam.unproject(mousePos);

        velocity = new Vector2(mousePos.x, mousePos.y).sub(playerBody.getPosition().scl(PPM))
                .nor().scl(speed);
        rocket.setLinearVelocity(velocity);
        rocket.setTransform(rocket.getPosition(), velocity.angleRad());
        shape.dispose();

        rocketReg.addToReg(this);

        rocketFix.setUserData(this);
    }

    private void applyBlast(Body body, Vector2 blastCenter, Vector2 applyPoint, float power) {
        Vector2 blastDir = applyPoint.sub(blastCenter);
        float distance = blastDir.len();
        if (distance <= 0.0f)
            return;
        if (distance > 1.75f)
            return;
        float invDistance = 1f / distance;
        float impulseMag = power * invDistance * invDistance;
        Vector2 blast = new Vector2(blastDir.nor().scl(impulseMag));
        if (blast.len() >= power) {
            blast.setLength(power);
        } else if (blast.len() <= -power) {
            blast.setLength(-power);
        }
        if (!this.isExploded) {
            body.applyLinearImpulse(blast, applyPoint, true);
        }
        //System.out.println(distance);
        //System.out.println(blast);
        this.isExploded = true;
    }

    @Override
    public void act() {

        float blastPower = 6.5f;

        Vector2 center = rocket.getPosition();

        ClosestCallback callback = new ClosestCallback();
        world.rayCast(callback, center, playerBody.getPosition());

        if (callback.getClosestBody() != null) closestBody = callback.getClosestBody();
        if ((closestBody == playerBody) || (playerFixture.testPoint(center))) {
            applyBlast(playerBody, center, playerBody.getPosition(), blastPower);
        }

        rocket.setLinearVelocity(0.0F, 0.0F);

        rocketReg.addToRemove(this);

        callback.dispose();
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(new TextureRegion(texture), rocket.getPosition().x * B2DVar.PPM - texture.getWidth() / 2f, rocket.getPosition().y * B2DVar.PPM - texture.getWidth() / 2f, texture.getWidth() / 2, texture.getHeight() / 2, texture.getWidth(), texture.getHeight(), 1, 1, velocity.angle());
    }

    @Override
    public void dispose() {
        if (!isDestroyed) {
            world.destroyBody(rocket);
            isDestroyed = true;
            world = null;
            rocket = null;
            playerBody = null;
            playerFixture = null;
            rocketReg = null;
            closestBody = null;
        }
    }
}