package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Matter extends Mass {

    private static final short vectorSumAmplification = 100;
    private static final short vectorAmplification = vectorSumAmplification * 10;

    protected Color getColor() {
        return new Color(100, 150, 10);
    }

    private double _vectorX, _vectorY, _vectorSumX, _vectorSumY;

    double getPositionWithVectorSumX() {
        return getCenterOfMassPosX() + _vectorSumX * vectorSumAmplification;
    }

    double getPositionWithVectorSumY() {
        return getCenterOfMassPosY() + _vectorSumY * vectorSumAmplification;
    }

    double getPositionWithVectorX() {
        return getCenterOfMassPosX() + (_vectorX * vectorAmplification);
    }

    double getPositionWithVectorY() {
        return getCenterOfMassPosY() + (_vectorY * vectorAmplification);
    }

    void applyPhysics(SpaceTime spaceTime) {

        updatePosX(spaceTime, _vectorSumX);
        updatePosY(spaceTime, _vectorSumY);
    }

    public void calculateForce(Attractable body, double timePassed) {

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
    Matter(double vectorX, double vectorY, double posX, double posY, double mass) {

        this._vectorSumX = vectorX;
        this._vectorSumY = vectorY;
        setCenterOfMassPosX(posX);
        setCenterOfMassPosY(posY);
        this._mass = mass;
    }

    public void show(Graphics g) {

        int length = 10;

        Color orig = g.getColor();

        g.setColor(getColor());
        g.drawArc((int) (getCenterOfMassPosX() - (length / 2)), (int) (getCenterOfMassPosY() - (length / 2)), length, length, 0, 360);

        g.setColor(orig);
    }
}
