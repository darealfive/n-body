package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

public class Matter extends Mass implements Acceleratable {

    private final static short DEFAULT_WIDTH = 5;

    double velocityX, velocityY, deltaVelocityX, deltaVelocityY;

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

        this(velocityX, velocityY, posX, posY, DEFAULT_WIDTH, mass);
    }

    /**
     * @param posX
     * @param posY
     * @param mass
     */
    Matter(double velocityX, double velocityY, double posX, double posY, double width, double mass) {

        this(new Circle((float) posX, (float) posY, (float) width), velocityX, velocityY, mass);
    }

    Matter(Shape shape, double velocityX, double velocityY, double mass) {

        super(shape, mass);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
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

        g.setColor(getColor());
        g.draw(shape);
    }
}
