package com.company;

class BarnesHutTree {

    final short macThreshold;

    Quadrant rootNode;

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

    static void collisionDetection() {

        for (Quadrant quadrant : Quadrant.violatedQuadrantCardinalPoints.keySet()) {

            quadrant.collisionDetection();
        }
    }
}
