package com.company;

import org.newdawn.slick.Graphics;

import java.util.HashMap;
import java.util.Map;

class BarnesHutTree {

    private final short macThreshold;

    private Quadrant rootNode;

    private Map<Quadrant, Map<CardinalPoint, Float>> violatedQuadrantCardinalPoints = new HashMap<>(1000);

    /**
     * Getter for macThreshold
     *
     * @return the MAC threshold
     */
    short getMacThreshold() {
        return macThreshold;
    }

    /**
     * @return map of all Quadrant objects whose boundaries were exceeded by its body
     */
    Map<Quadrant, Map<CardinalPoint, Float>> getViolatedQuadrantCardinalPoints() {
        return violatedQuadrantCardinalPoints;
    }

    private BarnesHutTree(short macThreshold) {
        this.macThreshold = macThreshold;
    }

    /**
     * @param spaceTime    the space to build on top the barnes hut tree
     * @param macThreshold the threshold when to use approximated values and when not
     * @return a BarnesHutTree instance
     */
    static BarnesHutTree build(SpaceTime spaceTime, short macThreshold) {

        BarnesHutTree barnesHutTree = new BarnesHutTree(macThreshold);
        barnesHutTree.rootNode = new Quadrant(spaceTime.getWidth(), barnesHutTree);
        for (Mass body : spaceTime.getBodies()) {

            barnesHutTree.rootNode.add(body);
        }

        return barnesHutTree;
    }

    void collisionDetection() {

        for (Map.Entry<Quadrant, Map<CardinalPoint, Float>> entrySet : violatedQuadrantCardinalPoints.entrySet()) {

            entrySet.getKey().collisionDetection(entrySet.getValue());
        }
    }

    void show(Graphics g) {
        rootNode.show(g);
        violatedQuadrantCardinalPoints.clear();
    }

    void calculateForce(Attractable body, double timePassed) {
        rootNode.calculateForce(body, timePassed);
    }
}
