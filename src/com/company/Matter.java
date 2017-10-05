package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Matter extends Mass implements NodeInterface {

    protected Color getColor() {
        return new Color(100, 150, 10);
    }

    private double _vectorX, _vectorY, _vectorSumX, _vectorSumY;

    double getPositionWithVectorSumX() {
        return getCenterOfMassPosX() + _vectorSumX * 100;
    }

    double getPositionWithVectorSumY() {
        return getCenterOfMassPosY() + _vectorSumY * 100;
    }

    double getPositionWithVectorX() {
        return getCenterOfMassPosX() + (_vectorX * 1000);
    }

    double getPositionWithVectorY() {
        return getCenterOfMassPosY() + (_vectorY * 1000);
    }

    void applyPhysics() {
        _posX += _vectorSumX;
        _posY += _vectorSumY;
    }

    public void calculateForce(NodeInterface body, double timePassed) {

        if (!equals(body)) {

            super.calculateForce(body, timePassed);

            double sinAlpha = Math.sin(radians);
            double cosAlpha = Math.cos(radians);
            // sin(alpha) = oppositeSide / hypotenuse
            _vectorY = vectorMagnitude * sinAlpha;
            // cos(alpha) = adjacentSide / hypotenuse
            _vectorX = vectorMagnitude * cosAlpha;

            this._vectorSumX += _vectorX;
            this._vectorSumY += _vectorY;
        }
    }

    /**
     * Note: position must be relative (room). Can I fix this?
     *
     * @param posX
     * @param posY
     * @param mass
     */
    Matter(double vectorX, double vectorY, double posX, double posY, long mass) {

        this._vectorSumX = vectorX;
        this._vectorSumY = vectorY;
        this._mass = mass;
        this._posX = posX;
        this._posY = posY;
    }

    public void show(Graphics g) {

        int length = 10;

        Color orig = g.getColor();

        g.setColor(getColor());
        g.drawArc((int) (this._posX - (length / 2)), (int) (this._posY - (length / 2)), length, length, 0, 360);

        g.setColor(orig);
    }
}
