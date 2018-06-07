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

            getRootNode().calculateForce(body, spaceTime.delta);
        }
    }

    @Override
    public void show(Graphics g) {
        getRootNode().show(g);
        barnesHutTree.getViolatedQuadrantCardinalPoints().clear();
    }

    @Override
    void applyPhysics() {
        super.applyPhysics();
        barnesHutTree.collisionDetection();
    }

    /**
     * Gets the root node Quadrant of the Barnes-Hut tree.
     *
     * @return the root node
     */
    private Quadrant getRootNode() {
        return barnesHutTree.rootNode;
    }
}
