package com.company;

import org.newdawn.slick.geom.Shape;

abstract class Node implements NodeInterface {

    final Shape shape;

    Node(Shape shape) {
        this.shape = shape;
    }

    @Override
    public Shape getShape() {
        return shape;
    }

    /**
     * Gets the absolute distance between two coordinates.
     * <p>
     * Gratulation to Stephan Bothur! He tells me how to get absolute distance between two points :)
     *
     * @param body
     * @return double
     */
    double getDistanceTo(Locatable body) {

        return Math.hypot(
                Math.abs(getCenterOfMassPosX() + (body.getCenterOfMassPosX() * -1)),
                Math.abs(getCenterOfMassPosY() + (body.getCenterOfMassPosY() * -1))
        );
    }
}
