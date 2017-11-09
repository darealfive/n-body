package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Matter extends Mass implements Acceleratable {
    protected double velocityX, velocityY, deltaVelocityX, deltaVelocityY;

    protected Color getColor() {
        return new Color(100, 150, 200);
    }

    public double getVelocityX() {
        return velocityX;
    }

    public double getVelocityY() {
        return velocityY;
    }

    private void updateVelocityX(SpaceTime spaceTime) {
        velocityX = spaceTime.determineVelocityX(this, deltaVelocityX);
    }

    private void updateVelocityY(SpaceTime spaceTime) {
        velocityY = spaceTime.determineVelocityY(this, deltaVelocityY);
    }

    /**
     * @param posX
     * @param posY
     * @param mass
     */
    Matter(double velocityX, double velocityY, double posX, double posY, double mass) {

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        setCenterOfMassPosX(posX);
        setCenterOfMassPosY(posY);
        this._mass = mass;
    }

    void applyPhysics(SpaceTime spaceTime) {

        super.applyPhysics(spaceTime);

        updateVelocityX(spaceTime);
        updateVelocityY(spaceTime);

        updatePosX(spaceTime, velocityX);
        updatePosY(spaceTime, velocityY);

        // Reset vectors for next calculations
        deltaVelocityX = 0;
        deltaVelocityY = 0;
    }

    public void calculateForce(Attractable body, double timePassed) {

        if (!equals(body)) {

            super.calculateForce(body, timePassed);

            double sinAlpha = Math.sin(radians);
            double cosAlpha = Math.cos(radians);

            // sin(alpha) = oppositeSide / hypotenuse
            deltaVelocityY += deltaVelocity * sinAlpha;

            // cos(alpha) = adjacentSide / hypotenuse
            deltaVelocityX += deltaVelocity * cosAlpha;
        }
    }

    public void show(Graphics g) {

        int length = 10;

        Color orig = g.getColor();

        g.setColor(getColor());
        g.drawArc((int) (getCenterOfMassPosX() - (length / 2)), (int) (getCenterOfMassPosY() - (length / 2)), length, length, 0, 360);

        g.setColor(orig);
    }
}
