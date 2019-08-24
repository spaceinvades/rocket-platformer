package com.spaceinvds.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.spaceinvds.game.Game;
import com.spaceinvds.handlers.*;
import com.spaceinvds.player.Player;
import com.spaceinvds.weapons.Plasma;
import com.spaceinvds.weapons.ProjectileReg;
import com.spaceinvds.weapons.Rocket;

import static com.spaceinvds.handlers.B2DVar.*;

public class Play extends GameScreen {
    private OrthographicCamera b2dCam;
    private World world;
    private Box2DDebugRenderer b2dr;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;
    private B2DTiledBoxMaker btbm;
    private ProjectileReg<Rocket> rocketReg;
    private ProjectileReg<Plasma> plasmaReg;
    private Texture playerImg;
    private float accum;
    private ShapeRenderer sr;
    private BitmapFont font;

    private Player player;

    public Play(ScreenManager sm) {
        super(sm);
    }

    @Override
    public void init() {

        super.init();

        playerImg = new Texture(Gdx.files.internal("player.jpg"));

        font = new BitmapFont();

        accum = 0;

        toDispose = false;

        isDisposed = false;

        rocketReg = new ProjectileReg<>();
        plasmaReg = new ProjectileReg<>();

        float playerHeight = 6 / PPM;
        float playerWidth = 6 / PPM;

        b2dr = new Box2DDebugRenderer();
        world = new World(new Vector2(0.0F, -9f), false);

        MyContactListener cl = new MyContactListener(rocketReg, plasmaReg, player);
        world.setContactListener(cl);

        sr = new ShapeRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();

        b2dCam = new OrthographicCamera();
        b2dCam.setToOrtho(false, Game.V_WIDTH / PPM, Game.V_HEIGHT / PPM);

        tiledMap = new TmxMapLoader().load("level.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap, 17 / 20f);

        fdef.friction = 0.9F;
        fdef.filter.categoryBits = BIT_GROUND;
        fdef.filter.maskBits = BIT_PLAYER | BIT_PROJECTILE;
        fdef.isSensor = false;

        bdef.type = BodyDef.BodyType.StaticBody;

        btbm = new B2DTiledBoxMaker((TiledMapTileLayer) tiledMap.getLayers().get("layer"), world,
                PPM / tmr.getUnitScale(), bdef, fdef);
        btbm.create();

        player = new Player(world, playerImg, cam, sb, cl, playerWidth, playerHeight, rocketReg, plasmaReg, true);

        shape.dispose();
    }

    public void handleEvents() {

        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.P)) {
            sm.changeScreen(Screens.MENU);
        }
        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.O)) {
            sm.changeScreen(Screens.PAUSE);
        }
        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.F11) && !game.fullscreen) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            game.fullscreen = true;
        } else if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.F11) && game.fullscreen) {
            Gdx.graphics.setWindowedMode(Game.V_WIDTH, Game.V_HEIGHT);
            game.fullscreen = false;
        }
        if (MyInput.isJustPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }

    @Override
    public void update(final float STEP, float delta) {
        accum += delta;

        while (accum >= STEP) {

            accum -= STEP;

            if (world != null)
                world.step(STEP, 6, 2);
            if (world != null && !world.isLocked()) {
                rocketReg.destroyAll();
                plasmaReg.destroyAll();
            }
            player.update(STEP);
            //ppSystem.out.println(playerOne.onGround);
            //bot.update(STEP);
        }
    }

    public void show() {
    }

    public void render(float delta) {
        realDispose();
        if (!toDispose && !isDisposed) {
            b2dCam.setToOrtho(false, cam.viewportWidth / PPM, cam.viewportHeight
                    / PPM);
            cam.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

            Body playerBody = player.getPlayerBody();

            Fixture playerFixture = player.getMainFixture();

            playerBody.setTransform(playerBody.getPosition(), 0);
            sb.setProjectionMatrix(cam.combined);
            sr.setProjectionMatrix(cam.combined);
            sr.setColor(Color.WHITE);

            update(Game.STEP, delta);

            handleEvents();

            player.render(delta);
            //bot.render(delta);

            Vector3 mousePos = MyInput.currentMousePos3();

            cam.unproject(mousePos);

            float mouseAng = new Vector2(mousePos.x, mousePos.y).sub(playerBody.getPosition()
                    .scl(PPM)).scl(-1).angle();

            Vector2 lineDir = new Vector2(1, 1).setLength(99999999).setAngle(mouseAng);

            Vector2 closestPoint = new Vector2(playerBody.getPosition().scl(PPM).sub(lineDir));

            ClosestCallback callback = new ClosestCallback();
            world.rayCast(callback, playerBody.getPosition(), closestPoint);
            if (callback.getClosestPoint() != null) closestPoint.set(callback.getClosestPoint().scl(PPM));

            //sb.enableBlending();
            sb.begin();

            player.draw();

            font.draw(sb, Integer.toString(Gdx.graphics.getFramesPerSecond()), Game.V_WIDTH / 2 + 10, Game.V_HEIGHT / 2 + 50);

            plasmaReg.draw(sb);
            rocketReg.draw(sb);

            sb.end();

            sr.begin(ShapeRenderer.ShapeType.Filled);

            sr.rectLine(playerBody.getPosition().scl(PPM), closestPoint, 2);

            sr.end();

            b2dr.render(world, b2dCam.combined);

            callback.dispose();

            tmr.setView(cam);
            tmr.render();

        }
    }

    public void resize(int width, int height) {
        vp.update(width, height);
    }

    public void pause() {
    }

    public void resume() {
    }

    public void hide() {
    }

    public void dispose() {
        toDispose = true;
    }

    protected void realDispose() {
        if (!isDisposed && toDispose) {
            isDisposed = true;
            toDispose = false;
            if (!world.isLocked()) {
                btbm.remove();
                world.dispose();
                b2dr.dispose();
            }
            tmr.dispose();
            tiledMap.dispose();
            rocketReg.dispose();
            playerImg.dispose();
            sb.dispose();
            sr.dispose();
            System.out.println("Play disposed!");
        }
    }
}