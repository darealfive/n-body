package com.company;

public abstract class Node implements NodeInterface {

    public double getDistanceTo(NodeInterface body) {

        double distanceX, distanceY;
        distanceX = Math.abs(this.getCenterOfMassPosX() - body.getCenterOfMassPosX());
        distanceY = Math.abs(this.getCenterOfMassPosY() - body.getCenterOfMassPosY());

        return Math.hypot(distanceX, distanceY);
    }
}
