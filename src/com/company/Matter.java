package com.company;

import java.awt.*;

public class Matter extends Node {

    public static final double G = 6.674 * Math.pow(10, -11);

    private float _mass;

    private float _posX;

    private float _posY;

    public float getCenterOfMassPosX() {
        return this._posX;
    }

    public float getCenterOfMassPosY() {
        return this._posY;
    }

    public void calculateForce(NodeInterface body) {

        if (!this.equals(body)) {

            double force, distance;
            distance = this.getDistanceTo(body);

            // First, calculate the force between two masses
            // F = (G * m1 * m2) / r^2
            force = (G * this._mass * body.getMass()) / Math.pow(distance, 2);
        }
    }

    /*public void mergeWith(Matter body) {
        float newMass = body.getMass();
        this._posX = ((this._posX * this._mass) + (body.getCenterOfMassPosX() * newMass)) / (this._mass + newMass);
        this._posY = ((this._posY * this._mass) + (body.getCenterOfMassPosY() * newMass)) / (this._mass + newMass);
        this._mass += newMass;
    }*/

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
