package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class DebugMatter extends Matter {

    private static final short vectorSumAmplification = 100;
    private static final short vectorAmplification = vectorSumAmplification * 10;
    static double maxVectorX = 0;
    static double maxVectorY = 0;

    private double lastVectorX, lastVectorY, absLastVectorX, absLastVectorY;

    private double getPositionWithVectorSumX() {
        return getCenterOfMassPosX() + _vectorSumX * vectorSumAmplification;
    }

    private double getPositionWithVectorSumY() {
        return getCenterOfMassPosY() + _vectorSumY * vectorSumAmplification;
    }

    private double getPositionWithVectorX() {
        return getCenterOfMassPosX() + (lastVectorX * vectorAmplification);
    }

    private double getPositionWithVectorY() {
        return getCenterOfMassPosY() + (lastVectorY * vectorAmplification);
    }

    protected Color getColor() {
        return new Color(200, 150, 10);
    }

    void applyPhysics(SpaceTime spaceTime) {

        // Before super call will reset our vectors, we have to check how big they are to be able to show them
        lastVectorX = _vectorX;
        lastVectorY = _vectorY;
        absLastVectorX = Math.abs(lastVectorX);
        absLastVectorY = Math.abs(lastVectorY);
        if (absLastVectorX > maxVectorX) {
            maxVectorX = absLastVectorX;
        }
        if (absLastVectorY > maxVectorY) {
            maxVectorY = absLastVectorY;
        }
        super.applyPhysics(spaceTime);
    }

    public DebugMatter(double posX, double posY, double mass) {
        this(0, 0, posX, posY, mass);
    }

    /**
     * Note: position must be relative (room). Can I fix this?
     *
     * @param vectorX vector in X direction
     * @param vectorY vector in X direction
     * @param posX
     * @param posY
     * @param mass
     */
    DebugMatter(double vectorX, double vectorY, double posX, double posY, double mass) {

        super(vectorX, vectorY, posX, posY, mass);
    }

    private void renderVectorSum(Graphics g) {

        g.setColor(new Color(0, 0, 255));

        int oldPosX = (int) getCenterOfMassPosX();
        int oldPosY = (int) getCenterOfMassPosY();
        int newPosX = ((int) getPositionWithVectorSumX());
        int newPosY = ((int) getPositionWithVectorSumY());

        g.drawLine(oldPosX, oldPosY, newPosX, oldPosY);
        g.drawLine(oldPosX, oldPosY, newPosX, newPosY);
        g.drawLine(newPosX, oldPosY, newPosX, newPosY);
    }

    private void renderActualVector(Graphics g) {

        g.setColor(new Color(255, 0, 0));

        int oldPosX = (int) getCenterOfMassPosX();
        int oldPosY = (int) getCenterOfMassPosY();
        int newPosX = ((int) getPositionWithVectorX());
        int newPosY = ((int) getPositionWithVectorY());

        g.drawLine(oldPosX, oldPosY, newPosX, newPosY);
    }

    public void show(Graphics g) {

        Color originalColor = g.getColor();

        super.show(g);

        double distance =  Math.abs(getCenterOfMassPosY());
        g.drawString(String.format("Geschwindigkeit: %s", _velocityY), 10, 30);
        g.drawString(String.format("D Geschwindigkeit: %s", _deltaVelocityY), 10, 50);
        g.drawString(String.format("Meter: %s", distance), 10, 70);
        g.drawString(String.format("G: %s", acceleration), 10, 90);
        g.drawString(String.format("Gesamtzeit: %s", Game.lastTime), 10, 110);
        g.drawString(String.format("Pos: %s", getCenterOfMassPosY()), 10, 130);
        /*g.drawString(String.format("X: %s", _vectorSumX), 10, 25);
        g.drawString(String.format("Y: %s", _vectorSumY), 10, 40);
        g.drawString(String.format("VMag: %s", vectorMagnitude), 10, 55);
        g.drawString(String.format("F: %s", force), 10, 70);*/

        int length = 8;

        double ratioVectorX, ratioVectorY;

        ratioVectorX = absLastVectorX / maxVectorX;
        Color colorVectorX = new Color(0, (int) (255 * ratioVectorX), 0);
        g.setColor(colorVectorX);
        g.fillArc((int) (getCenterOfMassPosX() - (length / 2)), (int) (getCenterOfMassPosY() - (length / 2)), length, length, 0, 180);

        ratioVectorY = absLastVectorY / maxVectorY;
        Color colorVectorY = new Color(0, 0, (int) (255 * ratioVectorY));
        g.setColor(colorVectorY);
        g.fillArc((int) (getCenterOfMassPosX() - (length / 2)), (int) (getCenterOfMassPosY() - (length / 2)), length, length, 180, 360);

        renderVectorSum(g);
        renderActualVector(g);

        g.setColor(originalColor);
    }
}
