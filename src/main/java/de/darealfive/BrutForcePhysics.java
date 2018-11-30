package de.darealfive;

import org.newdawn.slick.Graphics;

/**
 * Solves calculation problems in our space-time in a brute force approach.
 */
public class BrutForcePhysics extends PhysicsEngine {

    BrutForcePhysics(SpaceTime spaceTime) {
        super(spaceTime);
    }

    @Override
    public void run() {
        for (Mass body : spaceTime.getBodies()) {

            for (Attractable mass : spaceTime.getBodies()) {

                body.calculateForce(mass, spaceTime.delta);
            }
        }
    }

    @Override
    public void show(Graphics g) {
        for (Showable body : spaceTime.getBodies()) {

            body.show(g);
        }
    }
}
