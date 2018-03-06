package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

public class SpaceTime {

    public double delta = 0;
    /**
     * The area dimension in percent at the edge of the universe is a bit sticky for every {@link Acceleratable} body.
     * So they will slow down dependent till the end of the universe to a speed of 0 (if they reach the end).
     */
    private static final short stickyArea = 5;

    private double stickyAreaDimensionX, stickyAreaDimensionY;

    private int width, height;

    private Quadrant _rootNode;

    private List<Mass> bodies = new ArrayList<>(500);

    public List<Mass> getBodies() {
        return bodies;
    }

    void addMass(Mass object) {

        bodies.add(object);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public SpaceTime(int width, int height) {

        this.width = width;
        this.height = height;

        stickyAreaDimensionX = ((this.width / 100.0f) * (double) stickyArea);
        stickyAreaDimensionY = ((this.height / 100.0f) * (double) stickyArea);
    }

    public void render(Graphics graphics) {

        if (this._rootNode != null) {

            applyPhysics();
            this._rootNode.show(graphics);

            DebugMatter.maxVelocityX = 0;
            DebugMatter.maxVelocityY = 0;
        }

        // Draws the sticky area
        graphics.setColor(new Color(255, 255, 255, 100));
        graphics.drawRect((float) stickyAreaDimensionX, (float) stickyAreaDimensionY, 512 - (2 * (float) stickyAreaDimensionX), 512 - (2 * (float) stickyAreaDimensionY));
    }

    void run(double delta) {

        this.delta = delta;
        BarnesHutTree tree = new BarnesHutTree(this);
        this._rootNode = tree.build();

        for (Attractable body : bodies) {

            this._rootNode.calculateForce(body, delta);
        }
    }

    /**
     * Applies a quantum "measurement" to concretize the actual location in the spacetime.
     * Think of the superposition of an object.
     */
    private void applyPhysics() {

        for (Mass body : this.bodies) {

            body.applyPhysics(this);
        }
    }

    double determineVelocityX(Acceleratable body, double deltaVelocityX) {
        return determineVector(body.getCenterOfMassPosX(), body.getVelocityX(), deltaVelocityX, getWidth(), stickyAreaDimensionX);
    }

    double determineVelocityY(Acceleratable body, double deltaVelocityY) {
        return determineVector(body.getCenterOfMassPosY(), body.getVelocityY(), deltaVelocityY, getHeight(), stickyAreaDimensionY);
    }

    private double determineVector(double position, double vector, double offset, double limit, double sticky) {
        // If vector summary points to the opposite direction of the edge of the universe, return the new vector without
        // any changes.
        double vectorSum = vector + offset;

        // The hypothetically position the particle would be if we do not do anything
        double vectorPosition = position + vectorSum * delta;

        //Is vector pointing to the right or bottom edge of the universe?
        if (vectorSum > 0) {

            //The position where the sticky part begins
            double stickyPosition = limit - sticky;

            //Right or bottom edge

            //Calculate the sticky penetration length trough the object
            double stickyPenetrationLength = vectorPosition - stickyPosition;

            //Check whether the new position is in sticky area. Otherwise the vector does not touch the sticky areas
            if (stickyPenetrationLength > 0) {

                vectorSum -= getVectorCorrection(position, stickyPenetrationLength, stickyPosition, sticky);
            }

        } else {

            //The position where the sticky part begins
            double stickyPosition = 0 + sticky;

            //Left or top edge

            //Calculate the sticky penetration length trough the object
            double stickyPenetrationLength = vectorPosition - stickyPosition;

            //Check whether the new position is in sticky area. Otherwise the vector does not touch the sticky areas
            if (stickyPenetrationLength < 0) {

                vectorSum += getVectorCorrection(position, stickyPenetrationLength, stickyPosition, sticky);
            }
        }

        return vectorSum;
    }

    private double getVectorCorrection(double bodyPosition, double stickyPenetrationLength, double stickyPosition, double stickyDimension) {

        double alreadyStickedLength = bodyPosition - stickyPosition;
        stickyPenetrationLength -= alreadyStickedLength;

        double factor = (stickyPenetrationLength % stickyDimension) / stickyDimension;
        //Slow down the vector by the hypothetically sticky area penetration
        double reducedPenetrationLength = stickyPenetrationLength * (1 - factor);

        return (stickyPenetrationLength - reducedPenetrationLength) / delta;
    }

    double determinePosX(Locatable body, double offset) {

        return determinePosition(body.getCenterOfMassPosX() + (offset * delta), getWidth());
    }

    double determinePosY(Locatable body, double offset) {

        return determinePosition(body.getCenterOfMassPosY() + (offset * delta), getHeight());
    }

    private double determinePosition(double position, int limited) {

        if (position > limited) {

            position = position % limited;
        } else if (position < 0) {

            position = limited + position;
        }

        return position;
    }
}
