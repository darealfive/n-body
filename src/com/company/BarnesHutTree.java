package com.company;

class BarnesHutTree {

    private SpaceTime space;

    BarnesHutTree(SpaceTime space) {

        this.space = space;
    }

    Quadrant build() {

        Quadrant rootQuadrant = new Quadrant(this.space.getWidth(), 0, 0);
        for (NodeInterface body : space.getBodies()) {

            rootQuadrant.add(body);
        }

        return rootQuadrant;
    }
}
