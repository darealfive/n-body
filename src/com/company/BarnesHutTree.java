package com.company;

class BarnesHutTree {

    private SpaceTime space;

    BarnesHutTree(SpaceTime space) {

        this.space = space;
    }

    Quadrant build() {

        Quadrant rootQuadrant = new Quadrant(this.space.getWidth(), 0, 0);
        for (Mass body : space.getBodies()) {

            rootQuadrant.add(body);
        }

        return rootQuadrant;
    }
}
