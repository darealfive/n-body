package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Matter extends VirtualMatter implements NodeInterface{

    private double _vectorX, _vectorY, _vectorSumX, _vectorSumY;

    private double getPositionWithVectorSumX() {
        return getCenterOfMassPosX() + _vectorSumX * 80000;
    }

    private double getPositionWithVectorSumY() {
        return getCenterOfMassPosY() + _vectorSumY * 80000;
    }

    private double getPositionWithVectorX() {
        return getCenterOfMassPosX() + 10 * _vectorX * 800000000;
    }

    private double getPositionWithVectorY() {
        return getCenterOfMassPosY() + 10 * _vectorY * 800000000;
    }

    public void applyPhysics() {
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
    public Matter(double vectorX, double vectorY, float posX, double posY, float mass) {

        this._vectorSumX = vectorX;
        this._vectorSumY = vectorY;
        this._mass = mass;
        this._posX = posX;
        this._posY = posY;
    }

    private void renderVectorSum(Graphics g) {

        g.setColor(new Color(0, 0, 255));

        int oldPosX = (int) _posX;
        int oldPosY = (int) _posY;
        int newPosX = ((int) getPositionWithVectorSumX());
        int newPosY = ((int) getPositionWithVectorSumY());

        g.drawLine(oldPosX, oldPosY, newPosX, oldPosY);
        g.drawLine(oldPosX, oldPosY, newPosX, newPosY);
        g.drawLine(newPosX, oldPosY, newPosX, newPosY);
    }

    private void renderActualVector(Graphics g) {

        g.setColor(new Color(255, 0, 0));

        int oldPosX = (int) _posX;
        int oldPosY = (int) _posY;
        int newPosX = ((int) getPositionWithVectorX());
        int newPosY = ((int) getPositionWithVectorY());

        g.drawLine(oldPosX, oldPosY, newPosX, newPosY);
    }

    public void show(Graphics g) {

        int length = 10;

        Color orig = g.getColor();

        g.setColor(new Color(100, 150, 10));
        g.drawArc((int) (this._posX - (length / 2)), (int) (this._posY - (length / 2)), length, length, 0, 360);

        renderActualVector(g);
        renderVectorSum(g);

        g.setColor(orig);
    }
}
