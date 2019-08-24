package com.spaceinvds.player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Disposable;
import com.spaceinvds.game.Game;
import com.spaceinvds.handlers.MyContactListener;
import com.spaceinvds.handlers.MyInput;
import com.spaceinvds.handlers.WeaponHandler;
import com.spaceinvds.weapons.Plasma;
import com.spaceinvds.weapons.ProjectileReg;
import com.spaceinvds.weapons.Rocket;
import com.spaceinvds.weapons.Weapons;

import static com.spaceinvds.handlers.B2DVar.*;

public class Player implements Disposable {

    private Body playerBody;
    private Fixture mainFixture;
    private World world;
    private Texture playerImg;
    private SpriteBatch sb;
    private MyContactListener cl;
    private WeaponHandler weaponHandler;
    private BitmapFont font;

    private float health;

    public Player(World world, Texture playerImg, OrthographicCamera cam, SpriteBatch sb, MyContactListener cl,
                  float playerWidth, float playerHeight, ProjectileReg<Rocket> rocketReg,
                  ProjectileReg<Plasma> plasmaReg, boolean isPlayerOne) {
        this.world = world;
        this.playerImg = playerImg;
        this.sb = sb;
        this.cl = cl;

        font = new BitmapFont();

        weaponHandler = new WeaponHandler(world, PPM, cam, rocketReg, plasmaReg, this);

        health = 100f;

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        bdef.position.set(160 / PPM, 300 / PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        this.playerBody = this.world.createBody(bdef);
        playerBody.setLinearDamping(.5f);
        playerBody.setUserData(isPlayerOne);

        shape.setAsBox(playerWidth, playerHeight);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND;
        fdef.friction = .4f;
        this.mainFixture = this.playerBody.createFixture(fdef);
        this.mainFixture.setUserData("player");

        shape.setAsBox(4.25f / PPM, 1 / PPM, new Vector2(0.0F, -7 / PPM), 0.0F);
        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_GROUND;
        fdef.isSensor = true;
        this.playerBody.createFixture(fdef).setUserData("foot");
    }

    public void update(final float STEP) {

        boolean onGround = cl.isPlayerOnGround();

        if (playerBody.getLinearVelocity().y >= 5.25f) {
            playerBody.setLinearVelocity(playerBody.getLinearVelocity().x, 5.25f);
        }
        if (playerBody.getLinearVelocity().x >= 6.25f) {
            playerBody.setLinearVelocity(6.25f, playerBody.getLinearVelocity().y);
        }
        if (playerBody.getLinearVelocity().x <= -6.25f) {
            playerBody.setLinearVelocity(-6.25f, playerBody.getLinearVelocity().y);
        }
        if ((MyInput.isPressed(MyInput.leftKey)) && (onGround)) {
            this.playerBody.setLinearVelocity(this.playerBody.getLinearVelocity().x - 15 * STEP,
                    this.playerBody.getLinearVelocity().y);
        } else if ((MyInput.isPressed(MyInput.leftKey)) && (!onGround)) {
            this.playerBody.setLinearVelocity(this.playerBody.getLinearVelocity().x - 10 * STEP,
                    this.playerBody.getLinearVelocity().y);
        }
        if ((MyInput.isPressed(MyInput.rightKey)) && (onGround)) {
            this.playerBody.setLinearVelocity(this.playerBody.getLinearVelocity().x + 15 * STEP,
                    this.playerBody.getLinearVelocity().y);
        } else if ((MyInput.isPressed(MyInput.rightKey)) && (!onGround)) {
            this.playerBody.setLinearVelocity(this.playerBody.getLinearVelocity().x + 10 * STEP,
                    this.playerBody.getLinearVelocity().y);
        }
    }

    public void render(float delta) {
        if (MyInput.isJustLeftClicked()) {
            weaponHandler.fire(false);
        } else if (MyInput.isLeftClicked()) {
            weaponHandler.fire(true);
        }
        if (MyInput.isJustPressed(Input.Keys.NUM_1)) {
            weaponHandler.setWeapon(Weapons.ROCKET);
        }
        if (MyInput.isJustPressed(Input.Keys.NUM_2)) {
            weaponHandler.setWeapon(Weapons.PLASMA);
        }
    }

    public void draw() {
        if (playerBody != null) sb.draw(playerImg, (playerBody.getPosition().x -
                (playerImg.getWidth() / PPM / 2)) * PPM, (playerBody.getPosition().y -
                (playerImg.getHeight() / PPM / 2)) * PPM);
        font.draw(sb, weaponHandler.getCurrentWeapon().toString(), Game.V_WIDTH / 2 + 10, Game.V_HEIGHT / 2 - 50);
    }

    @Override
    public void dispose() {
        if (!world.isLocked()) {
            world.destroyBody(playerBody);
            playerBody = null;
        }
        playerImg.dispose();

    }

    public Fixture getMainFixture() {
        return mainFixture;
    }

    public Body getPlayerBody() {
        return playerBody;
    }
}
