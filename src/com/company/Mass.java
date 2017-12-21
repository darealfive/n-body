package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Shape;

/**
 * Mass represents a mass in the spacetime. It has a center of mass but it has no vectors.
 */
public class Mass extends Node {

    protected Color getColor() {
        return new Color(255, 0, 0, 100);
    }

    private static final double G = 6.674 * Math.pow(10, -11);

    double _mass;

    Shape shape;

    public double getCenterOfMassPosX() {
        return shape.getCenterX();
    }

    public double getCenterOfMassPosY() {
        return shape.getCenterY();
    }

    private void setCenterOfMassPosX(double centerOfMassPosX) {
        shape.setCenterX((float) centerOfMassPosX);
    }

    private  void setCenterOfMassPosY(double centerOfMassPosY) {

        shape.setCenterY((float) centerOfMassPosY);
    }

    double deltaVelocity, radians, acceleration, force;

    Mass(Shape shape, double mass) {

        this.shape = shape;
        _mass = mass;
    }

    /**
     * Note: position must be relative (room). Can I fix this?
     *
     * @param posX
     * @param posY
     * @param mass
     */
    Mass(double posX, double posY, double mass) {

        this(new Point((float) posX, (float) posY), mass);
    }

    public void calculateForce(Attractable body, double timePassed) {

        if (!equals(body)) {

            // Firstly, calculate the force which takes effect on this particle
            // F = (G * m1 * m2) / d^2
            force = (G * _mass * body.getMass()) / Math.pow(getDistanceTo(body), 2);

            // Secondly, the force will lead to vector changes on the current force vectors
            // The amount of change is determined by looking to the actual force compared with the position of both particles
            // relatively to each other PLUS the actual vector of the current particle.

            // This is the acceleration which acts on this particle = m/s2
            acceleration = force / _mass;

            double x1, x2, y1, y2;
            x1 = getCenterOfMassPosX();
            y1 = getCenterOfMassPosY();
            x2 = body.getCenterOfMassPosX();
            y2 = body.getCenterOfMassPosY();

            // Radian between two masses
            radians = Math.atan2((y2 - y1), (x2 - x1));

            // The velocity delta for current time
            deltaVelocity = acceleration * timePassed;
        }
    }

    public void merge(Attractable body) {
        double newMass = body.getMass();

        setCenterOfMassPosX(
                ((getCenterOfMassPosX() * this._mass) + (body.getCenterOfMassPosX() * newMass)) / (this._mass + newMass)
        );
        setCenterOfMassPosY(
                ((getCenterOfMassPosY() * this._mass) + (body.getCenterOfMassPosY() * newMass)) / (this._mass + newMass)
        );
        this._mass += newMass;
    }

    public double getMass() {

        return this._mass;
    }

    public void show(Graphics g) {

        int length = 5;

        g.setColor(getColor());
        g.fillArc((int) (this.getCenterOfMassPosX() - (length / 2)), (int) (this.getCenterOfMassPosY() - (length / 2)), length, length, 0, 360);
    }

    void updatePosX(SpaceTime spaceTime, double offset) {

        setCenterOfMassPosX(spaceTime.determinePosX(this, offset));
    }

    void updatePosY(SpaceTime spaceTime, double offset) {

        setCenterOfMassPosY(spaceTime.determinePosY(this, offset));
    }

    void applyPhysics(SpaceTime spaceTime) {
    }
}
