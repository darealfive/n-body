package com.company;

class BarnesHutTree {

    /**
     * @param spaceTime the space to build on top the barnes hut tree
     * @return the root quadrant of the barnes hut tree
     */
    static Quadrant build(SpaceTime spaceTime) {

        Quadrant rootQuadrant = new Quadrant(spaceTime.getWidth());
        for (Mass body : spaceTime.getBodies()) {

            rootQuadrant.add(body);
        }

        return rootQuadrant;
    }

    Quadrant build() {

        Quadrant rootQuadrant = new Quadrant(this.space.getWidth());
        for (Mass body : space.getBodies()) {

            rootQuadrant.add(body);
        }

        return rootQuadrant;
    }
}
