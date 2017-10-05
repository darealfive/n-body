package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class DebugMatter extends Matter {

    protected Color getColor() {
        return new Color(200, 150, 10);
    }

    public DebugMatter(double posX, double posY, long mass) {
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
    DebugMatter(double vectorX, double vectorY, double posX, double posY, long mass) {

        super(vectorX, vectorY, posX, posY, mass);
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

        Color originalColor = g.getColor();

        super.show(g);

        renderVectorSum(g);
        renderActualVector(g);

        //g.setColor(getColor());

        g.setColor(originalColor);
    }
}
