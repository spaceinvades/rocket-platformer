package com.spaceinvds.weapons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.spaceinvds.handlers.MyInput;

import static com.spaceinvds.handlers.B2DVar.*;


public class Plasma implements Projectile {

    private Body plasma;
    private World world;
    private ProjectileReg<Plasma> plasmaReg;

    private Texture texture;

    private boolean isDestroyed = false;

    private int bounces = 0;

    private boolean isPlayerOne;

    public Plasma(World world, final float PPM, Body playerBody, OrthographicCamera cam,
                  float speed, ProjectileReg<Plasma> plasmaReg) {
        //System.out.println("Created");
        this.world = world;
        this.plasmaReg = plasmaReg;

        texture = new Texture("plasma.png");

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        CircleShape circle = new CircleShape();

        PolygonShape shape = new PolygonShape();
        bodyDef.position.set(playerBody.getPosition());
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        fixtureDef.isSensor = false;

        fixtureDef.filter.categoryBits = BIT_PROJECTILE;
        fixtureDef.filter.maskBits = BIT_GROUND;

        //fixtureDef.shape = shape;
        fixtureDef.shape = circle;
        plasma = world.createBody(bodyDef);
        //shape.setAsBox(.125f / PPM, 4 / PPM);
        //plasmaFixtureUp = plasma.createFixture(fixtureDef);
        //shape.setAsBox(4 / PPM, .125f / PPM);
        //plasmaFixtureSide = plasma.createFixture(fixtureDef);
        //shape.setAsBox(3.5f / PPM, 3.5f / PPM);
        circle.setRadius(3.5f / PPM);
        //private Fixture plasmaFixtureUp;
        //private Fixture plasmaFixtureSide;
        Fixture plasmaCenter = plasma.createFixture(fixtureDef);
        plasma.setGravityScale(0.0F);
        plasma.setLinearDamping(0.0F);

        Vector3 mousePos = MyInput.currentMousePos3();

        cam.unproject(mousePos);

        plasmaCenter.setUserData(this);
        plasmaCenter.setFriction(0);
        plasmaCenter.setRestitution(1);
        plasmaCenter.setDensity(0.01f);
        plasma.resetMassData();

        Vector2 velocity = new Vector2(mousePos.x, mousePos.y).sub(playerBody.getPosition().scl(PPM))
                .nor().scl(speed);
        //plasma.applyLinearImpulse(velocity, playerBody.getPosition(), true);
        plasma.setLinearVelocity(velocity);

        //plasmaFixtureUp.setUserData(this);
        //plasmaFixtureUp.setFriction(0.0001f);
        //plasmaFixtureSide.setUserData(this);
        //plasmaFixtureSide.setFriction(0.00011f);
        plasmaCenter.setUserData(this);
        plasmaCenter.setFriction(0);
        plasmaCenter.setRestitution(1);
        plasmaCenter.setDensity(0.01f);
        plasma.resetMassData();

        shape.dispose();

        plasmaReg.addToReg(this);
    }

    @Override
    public void act(/*boolean isSide, boolean isCenter*/) {
        if (bounces < 3) {
            /*if (isCenter) {
                plasma.setLinearVelocity(-(plasma.getLinearVelocity().x), -(plasma.getLinearVelocity().y));
                System.out.println("Bounced from center");
            }
            else {
                if (isSide) {
                    plasma.setLinearVelocity(-(plasma.getLinearVelocity().x), plasma.getLinearVelocity().y);
                    System.out.println("Bounced from side");
                }
                else {
                    plasma.setLinearVelocity(plasma.getLinearVelocity().x, -(plasma.getLinearVelocity().y));
                    System.out.println("Bounced from top");
                }
            }*/
            bounces++;
        } else {
            plasma.setLinearVelocity(0, 0);
            plasmaReg.addToRemove(this);
        }
    }

    @Override
    public void draw(SpriteBatch sb) {
        sb.draw(texture, plasma.getPosition().x * PPM - 3.5f, plasma.getPosition().y * PPM - 3.5f, 7, 7);
    }

    @Override
    public void dispose() {
        if (!this.isDestroyed) {
            this.world.destroyBody(this.plasma);
            this.isDestroyed = true;
            this.world = null;
            this.plasma = null;
            this.plasmaReg = null;
            //System.out.println("Removed");
        }
    }
}
