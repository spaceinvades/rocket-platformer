package com.spaceinvds.handlers;


import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.World;
import com.spaceinvds.player.Player;
import com.spaceinvds.weapons.*;

public class WeaponHandler {
    private Player player;
    private World world;
    private OrthographicCamera cam;
    private ProjectileReg<Rocket> rocketReg;
    private ProjectileReg<Plasma> plasmaReg;
    private Weapons currentWeapon = Weapons.ROCKET;

    private long rocketCooldown = 0L;
    private long plasmaCooldown = 0L;

    private final float PPM;

    public WeaponHandler(World world, final float PPM, OrthographicCamera cam,
                         ProjectileReg<Rocket> rocketReg, ProjectileReg<Plasma> plasmaReg, Player player) {
        this.player = player;
        this.PPM = PPM;
        this.world = world;
        this.cam = cam;
        this.rocketReg = rocketReg;
        this.plasmaReg = plasmaReg;
    }

    public void setWeapon(Weapons w) {
        switch (w) {
            case PLASMA:
                currentWeapon = Weapons.PLASMA;
                break;

            case ROCKET:
                currentWeapon = Weapons.ROCKET;
                break;
        }
    }

    public void fire(boolean holdingMouse) {
        long rocketPause = WeaponVar.ROCKET_COOLDOWN;
        long plasmaPause = WeaponVar.PLASMA_COOLDOWN;
        switch (currentWeapon) {
            case ROCKET:
                if (System.currentTimeMillis() - rocketCooldown >= rocketPause && !holdingMouse) {
                    if (world != null)
                        new Rocket(world, PPM, player.getMainFixture(), player.getPlayerBody(), cam, 4.0F, rocketReg);
                    rocketCooldown = System.currentTimeMillis();
                }
                break;

            case PLASMA:
                if (System.currentTimeMillis() - plasmaCooldown >= plasmaPause) {
                    if (world != null)
                        new Plasma(world, PPM, player.getPlayerBody(), cam, 6.0F, plasmaReg);
                    plasmaCooldown = System.currentTimeMillis();
                }
                break;

            case RIFLE:

        }
    }

    public Weapons getCurrentWeapon() {
        return currentWeapon;
    }
}