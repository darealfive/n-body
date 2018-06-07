package com.company;

import org.newdawn.slick.Graphics;

/**
 * Base class to solve calculation problems in our space-time.
 */
abstract class PhysicsEngine {
    SpaceTime spaceTime;

    /**
     * Runs necessary calculations since last iteration.
     */
    abstract void run();

    /**
     * Shows the contents of its space time.
     *
     * @param g the graphics used to draw the result
     */
    abstract void show(Graphics g);

    PhysicsEngine(SpaceTime spaceTime) {
        this.spaceTime = spaceTime;
    }

    /**
     * Applies a quantum "measurement" to concretize the actual location in the space-time.
     * Think of the superposition of an object.
     */
    void applyPhysics() {

        for (Mass body : spaceTime.getBodies()) {

            body.applyPhysics(spaceTime);
        }
    }
}
