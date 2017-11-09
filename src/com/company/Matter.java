package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Matter extends Mass implements Vectorizable {

    protected double _vectorX, _vectorY, _vectorSumX, _vectorSumY, _velocityX, _velocityY, _deltaVelocityX, _deltaVelocityY;

    protected Color getColor() {
        return new Color(100, 150, 10);
    }

    public double getVectorSumX() {
        return _vectorSumX;
    }

    public double getVectorSumY() {
        return _vectorSumY;
    }

    private void updateVectorSumX(SpaceTime spaceTime) {
        _vectorSumX = spaceTime.determineVectorSumX(this, _velocityX);
    }

    private void updateVectorSumY(SpaceTime spaceTime) {
        _vectorSumY = spaceTime.determineVectorSumY(this, _velocityY);
    }

    void applyPhysics(SpaceTime spaceTime) {

        super.applyPhysics(spaceTime);

        updateVectorSumX(spaceTime);
        //updateVectorSumY(spaceTime);

        updatePosX(spaceTime, getVectorSumX());
        updatePosY(spaceTime, getVectorSumY());

        setCenterOfMassPosY(getCenterOfMassPosY() + (_velocityY * spaceTime.delta));

        _velocityY += _deltaVelocityY;
        _velocityX += _deltaVelocityX;

        // Reset vectors for next calculations
        _vectorX = 0;
        _vectorY = 0;
        //_velocityX = 0;
        //_velocityY = 0;
    }

    public void calculateForce(Attractable body, double timePassed) {

        if (!equals(body)) {

            super.calculateForce(body, timePassed);

            double sinAlpha = Math.sin(radians);
            double cosAlpha = Math.cos(radians);

            // sin(alpha) = oppositeSide / hypotenuse
            _deltaVelocityY = deltaVelocity * sinAlpha;

            // cos(alpha) = adjacentSide / hypotenuse
            _deltaVelocityX = deltaVelocity * cosAlpha;
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
