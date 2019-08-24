package com.spaceinvds.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.spaceinvds.player.Player;
import com.spaceinvds.weapons.Plasma;
import com.spaceinvds.weapons.ProjectileReg;
import com.spaceinvds.weapons.Rocket;

public class MyContactListener implements ContactListener {
    private boolean playerOnGround = false;

    private ProjectileReg<Rocket> rocketReg;
    private ProjectileReg<Plasma> plasmaReg;
    private Player bot;
    private Player player;

    public MyContactListener(ProjectileReg<Rocket> rocketReg, ProjectileReg<Plasma> plasmaReg, Player player) {
        this.rocketReg = rocketReg;
        this.plasmaReg = plasmaReg;
        this.player = player;
    }

    public void beginContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        assert fa.getUserData() != null : "Fixture data null";
        assert fa.getBody().getUserData() != null : "Body data null";
        assert player != null : "Player null";

        if ((fa.getUserData().equals("foot")) && (fa.getBody().getUserData().equals(true))) {
            playerOnGround = true;
        } else if ((fb.getUserData().equals("foot")) && (fb.getBody().getUserData().equals(true))) {
            playerOnGround = true;
        }

        for (Rocket r : rocketReg.getReg()) {
            if (fa.getUserData().equals(r)) {
                r.act(/*fa.getBody().getPosition()*/);

            } else if (fb.getUserData().equals(r)) {
                r.act(/*fb.getBody().getPosition()*/);
            }
        }

        for (Plasma p : plasmaReg.getReg()) {
            if (fa.getUserData().equals(p)) {
				/*if (fa.getFriction() == 0.00012f) {
					p.bounce(false, true);
					System.out.println("Collided and Detected");
				}
				else {
					if (fa.getFriction() == 0.00011f) {
						p.bounce(true, false);
						System.out.println("Collided and Detected");
					}
					else {
						p.bounce(false, false);
						System.out.println("Collided and Detected");
					}
				}*/
                p.act();
                //System.out.println("Collided");
            } else if (fb.getUserData().equals(p)) {
				/*if (fb.getUserData().equals(p)) {
					if (fb.getFriction() == 0.00012f) {
						p.bounce(false, true);
						System.out.println("Collided and Detected");
					}
					else {
						if (fb.getFriction() == 0.00011f) {
							p.bounce(true, false);
							System.out.println("Collided and Detected");
						}
						else {
							p.bounce(false, false);
							System.out.println("Collided and Detected");
						}
					}

				}*/
				/*if(fa.getBody().getUserData().equals(true)) {
					p.
				}*/
                p.act();

                //System.out.println("Collided");
            }
        }
    }

    public void endContact(Contact c) {
        Fixture fa = c.getFixtureA();
        Fixture fb = c.getFixtureB();

        assert fa.getUserData() != null : "Fixture data null";
        assert fa.getBody().getUserData() != null : "Body data null";
        assert player != null : "Player null";

        if ((fa.getUserData().equals("foot")) && (fa.getBody().getUserData().equals(true))) {
            playerOnGround = false;
        } else if ((fb.getUserData().equals("foot")) && (fb.getBody().getUserData().equals(true))) {
            playerOnGround = false;
        }
    }

    public void preSolve(com.badlogic.gdx.physics.box2d.Contact c, com.badlogic.gdx.physics.box2d.Manifold m) {

    }

    public void postSolve(com.badlogic.gdx.physics.box2d.Contact c, com.badlogic.gdx.physics.box2d.ContactImpulse ci) {
    }

    public boolean isPlayerOnGround() {
        return playerOnGround;
    }

}