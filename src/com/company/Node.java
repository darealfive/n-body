package com.company;

abstract class Node implements NodeInterface {

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
