package com.company;

public abstract class Node implements NodeInterface {

    /**
     * Gets the absolute distance between two coordinates.
     * <p>
     * Gratulation to Stephan Bothur! He tells me how to get absolute distance between two points :)
     *
     * @param body
     * @return double
     */
    protected double getDistanceTo(NodeInterface body) {

        double distanceX, distanceY;
        distanceX = Math.abs(this.getCenterOfMassPosX() + (body.getCenterOfMassPosX() * -1));
        distanceY = Math.abs(this.getCenterOfMassPosY() + (body.getCenterOfMassPosY() * -1));

        return Math.hypot(distanceX, distanceY);
    }
}
