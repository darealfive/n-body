package com.company;

import org.newdawn.slick.Graphics;

/**
 * Solves calculation problems in our space-time via Barnes-Hut algorithm.
 */
public class BarnesHutPhysics extends PhysicsEngine {

    private BarnesHutTree barnesHutTree;

    BarnesHutPhysics(SpaceTime spaceTime, short macThreshold) {
        super(spaceTime);
        barnesHutTree = BarnesHutTree.build(spaceTime, macThreshold);
    }

    @Override
    public void run() {
        for (Attractable body : spaceTime.getBodies()) {

            barnesHutTree.calculateForce(body, spaceTime.delta);
        }
    }

    @Override
    public void show(Graphics g) {
        barnesHutTree.show(g);
    }

    @Override
    void applyPhysics() {
        super.applyPhysics();
        barnesHutTree.collisionDetection();
    }
}
