package com.company;

import org.newdawn.slick.Graphics;

/**
 * Solves calculation problems in our space-time via Barnes-Hut algorithm.
 */
public class BarnesHutPhysics extends PhysicsEngine {

    private Quadrant rootNode;

    BarnesHutPhysics(SpaceTime spaceTime) {
        super(spaceTime);
        rootNode = BarnesHutTree.build(spaceTime);
    }

    @Override
    public void run() {
        for (Attractable body : spaceTime.getBodies()) {

            rootNode.calculateForce(body, spaceTime.delta);
        }
    }

    @Override
    public void show(Graphics g) {
        rootNode.show(g);
        Quadrant.violatedQuadrantCardinalPoints.clear();
    }

    @Override
    void applyPhysics() {
        super.applyPhysics();
        BarnesHutTree.collisionDetection();
    }
}
