package com.company;

import java.awt.*;

public class Matter extends Node {

    public static final double G = 6.674 * Math.pow(10, -11);

    private float _mass;

    private float _posX, _posY;

    private float _vectorX, _vectorY;

    public float getCenterOfMassPosX() {
        return this._posX;
    }

    public float getCenterOfMassPosY() {
        return this._posY;
    }

    private float getPositionWithVectorX() {
        return _posX + _vectorX;
    }

    private float getPositionWithVectorY() {
        return _posY + _vectorY;
    }

    public void calculateForce(NodeInterface body) {

        if (!this.equals(body)) {

            double distanceSquared, acceleration, force, radians;

            distanceSquared = Math.pow(this.getDistanceTo(body), 2);

            // Firstly, calculate the force which takes effect on this particle
            // F = (G * m1 * m2) / d^2
            force = (G * this._mass * body.getMass()) / distanceSquared;

            // Secondly, the force will lead to vector changes on the current force vectors
            // The amount of change is determined by looking to the actual force compared with the position of both particles
            // relatively to each other PLUS the actual vector of the current particle.

            // This is the acceleration which acts on this particle = m/s2
            acceleration = force / this._mass;

            //acceleration *= 1000000000;
            //acceleration *= 500000;


            double x1, x2, y1, y2;
            x1 = getCenterOfMassPosX();
            y1 = getCenterOfMassPosY();
            x2 = body.getCenterOfMassPosX();
            y2 = body.getCenterOfMassPosY();

            // Radian between two masses
            radians = Math.atan((y2 - y1) / (x2 - x1));

            radians = Math.atan2((y2 - y1), (x2 - x1));

            // The distance my particle would move as hypotenuse in meters, alias "magnitude of the vector"
            double vectorMagnitude = (acceleration / 2) * Math.pow(SECONDS_PASSED_BY, 2);

            double sinAlpha = Math.sin(radians);
            double cosAlpha = Math.cos(radians);
            // sin(alpha) = oppositeSide / hypotenuse
            double vectorY = vectorMagnitude * sinAlpha;
            // cos(alpha) = adjacentSide / hypotenuse
            double vectorX = vectorMagnitude * cosAlpha;

            this._vectorX += vectorX;
            this._vectorY += vectorY;
        }
    }

    /**
     * Note: position must be relative (room). Can I fix this?
     *
     * @param posX
     * @param posY
     * @param mass
     */
    public Matter(float posX, float posY, float mass) {

        this._mass = mass;
        this._posX = posX;
        this._posY = posY;
    }

    public void merge(NodeInterface body) {
        float newMass = body.getMass();
        this._posX = ((this._posX * this._mass) + (body.getCenterOfMassPosX() * newMass)) / (this._mass + newMass);
        this._posY = ((this._posY * this._mass) + (body.getCenterOfMassPosY() * newMass)) / (this._mass + newMass);
        this._mass += newMass;
    }

    public float getMass() {

        return this._mass;
    }

    public void show(Graphics g) {

        int length = 10;

        g.drawArc((int) (this._posX - (length / 2)), (int) (this._posY - (length / 2)), length, length, 0, 360);
    }
}
