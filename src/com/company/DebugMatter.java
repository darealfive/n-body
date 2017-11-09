package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class DebugMatter extends Matter {

    private static final short velocityAmplification = 1;
    private static final short deltaVelocityAmplification = velocityAmplification * 10;
    static double maxVelocityX = 0, maxVelocityY = 0;

    private double lastVelocityX, lastVelocityY, absLastVelocityX, absLastVelocityY;

    private double getPositionWithVelocityX() {
        return getCenterOfMassPosX() + velocityX * velocityAmplification;
    }

    private double getPositionWithVelocityY() {
        return getCenterOfMassPosY() + velocityY * velocityAmplification;
    }

    private double getPositionWithDeltaVelocityX() {
        return getCenterOfMassPosX() + (lastVelocityX * deltaVelocityAmplification);
    }

    private double getPositionWithDeltaVelocityY() {
        return getCenterOfMassPosY() + (lastVelocityY * deltaVelocityAmplification);
    }

    protected Color getColor() {
        return new Color(200, 150, 10);
    }

    public DebugMatter(double posX, double posY, double mass) {
        this(0, 0, posX, posY, mass);
    }

    /**
     * Note: position must be relative (room). Can I fix this?
     *
     * @param velocityX velocity vector for X direction
     * @param velocityY velocity vector for Y direction
     * @param posX
     * @param posY
     * @param mass
     */
    DebugMatter(double velocityX, double velocityY, double posX, double posY, double mass) {

        super(velocityX, velocityY, posX, posY, mass);
    }

    void applyPhysics(SpaceTime spaceTime) {

        // Before super call will reset our vectors, we have to check how big they are to be able to show them
        lastVelocityX = deltaVelocityX;
        lastVelocityY = deltaVelocityY;
        absLastVelocityX = Math.abs(lastVelocityX);
        absLastVelocityY = Math.abs(lastVelocityY);
        if (absLastVelocityX > maxVelocityX) {
            maxVelocityX = absLastVelocityX;
        }
        if (absLastVelocityY > maxVelocityY) {
            maxVelocityY = absLastVelocityY;
        }
        super.applyPhysics(spaceTime);
    }

    private void renderVelocity(Graphics g) {

        g.setColor(new Color(0, 0, 255));

        int oldPosX = (int) getCenterOfMassPosX();
        int oldPosY = (int) getCenterOfMassPosY();
        int newPosX = ((int) getPositionWithVelocityX());
        int newPosY = ((int) getPositionWithVelocityY());

        g.drawLine(oldPosX, oldPosY, newPosX, oldPosY);
        g.drawLine(oldPosX, oldPosY, newPosX, newPosY);
        g.drawLine(newPosX, oldPosY, newPosX, newPosY);
    }

    private void renderDeltaVelocity(Graphics g) {

        g.setColor(new Color(255, 0, 0));

        int oldPosX = (int) getCenterOfMassPosX();
        int oldPosY = (int) getCenterOfMassPosY();
        int newPosX = ((int) getPositionWithDeltaVelocityX());
        int newPosY = ((int) getPositionWithDeltaVelocityY());

        g.drawLine(oldPosX, oldPosY, newPosX, newPosY);
    }

    public void show(Graphics g) {

        Color originalColor = g.getColor();

        super.show(g);

        /*
        double distance =  Math.abs(getCenterOfMassPosY());
        g.drawString(String.format("Geschwindigkeit: %s", velocityY), 10, 30);
        g.drawString(String.format("D Geschwindigkeit: %s", deltaVelocityY), 10, 50);
        g.drawString(String.format("Meter: %s", distance), 10, 70);
        g.drawString(String.format("G: %s", acceleration), 10, 90);
        g.drawString(String.format("Gesamtzeit: %s", Game.lastTime), 10, 110);
        g.drawString(String.format("Pos: %s", getCenterOfMassPosY()), 10, 130);
        */


        /*g.drawString(String.format("X: %s", _vectorSumX), 10, 25);
        g.drawString(String.format("Y: %s", _vectorSumY), 10, 40);
        g.drawString(String.format("VMag: %s", vectorMagnitude), 10, 55);
        g.drawString(String.format("F: %s", force), 10, 70);*/

        int length = 8;

        double ratioVelocityX, ratioVelocityY;

        ratioVelocityX = absLastVelocityX / maxVelocityX;
        Color colorVelocityX = new Color(0, (int) (255 * ratioVelocityX), 0);
        g.setColor(colorVelocityX);
        g.fillArc((int) (getCenterOfMassPosX() - (length / 2)), (int) (getCenterOfMassPosY() - (length / 2)), length, length, 0, 180);

        ratioVelocityY = absLastVelocityY / maxVelocityY;
        Color colorVelocityY = new Color(0, 0, (int) (255 * ratioVelocityY));
        g.setColor(colorVelocityY);
        g.fillArc((int) (getCenterOfMassPosX() - (length / 2)), (int) (getCenterOfMassPosY() - (length / 2)), length, length, 180, 360);

        renderVelocity(g);
        renderDeltaVelocity(g);

        g.setColor(originalColor);
    }
}
