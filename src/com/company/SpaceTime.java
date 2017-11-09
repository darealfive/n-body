package com.company;

import org.newdawn.slick.Graphics;

import java.util.Vector;

public class SpaceTime {

    public double delta = 0;
    /**
     * The area dimension in percent at the edge of the universe is a bit sticky for every {@link Vectorizable} body.
     * So they will slow down dependent till the end of the universe to a speed of 0 (if they reach the end).
     */
    private static final short stickyArea = 5;

    private double stickyAreaDimensionX, stickyAreaDimensionY;

    private int width, height;

    private TreeNode _rootNode;

    private Vector<Mass> bodies = new Vector<>();

    public Vector<Mass> getBodies() {
        return bodies;
    }

    void addMass(Mass object) {

        bodies.addElement(object);
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

            DebugMatter.maxVectorX = 0;
            DebugMatter.maxVectorY = 0;
        }
    }

    void run(double delta) {

        this.delta = delta;
        BarnesHutTree tree = new BarnesHutTree(this);
        this._rootNode = tree.build(delta);
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

    double determineVectorSumX(Vectorizable body, double offset) {
        return determineVector(body.getCenterOfMassPosX(), body.getVectorSumX(), offset, getWidth(), stickyAreaDimensionX);
    }

    double determineVectorSumY(Vectorizable body, double offset) {
        return determineVector(body.getCenterOfMassPosY(), body.getVectorSumY(), offset, getHeight(), stickyAreaDimensionY);
    }

    private double determineVector(double position, double vector, double offset, double limit, double sticky) {
        // If vector summary points to the opposite direction of the edge of the universe, return the new vector without
        // any changes.
        double vectorSum = vector + offset;
        double vectorPosition = position + vectorSum;

        //Is vector pointing to the right or bottom edge of the universe?
        /*if (vectorSum > 0) {

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
        }*/

        return vectorSum;
    }

    private double getVectorCorrection(double bodyPosition, double stickyPenetrationLength, double stickyPosition, double stickyDimension) {

        double alreadyStickedLength = bodyPosition - stickyPosition;
        stickyPenetrationLength -= alreadyStickedLength;

        double factor = stickyPenetrationLength / stickyDimension;
        //Slow down the vector by the hypothetically sticky area penetration
        double reducedPenetrationLength = stickyPenetrationLength * (1 - factor);

        return stickyPenetrationLength - reducedPenetrationLength;
    }

    double determinePosX(Locatable body, double offset) {

        return determinePosition(body.getCenterOfMassPosX() + (offset * delta), getWidth());
    }

    double determinePosY(Locatable body, double offset) {

        return determinePosition(body.getCenterOfMassPosY() + (offset * delta), getHeight());
    }

    private double determinePosition(double position, int limited) {

        /*if (position > limited) {

            position = position % limited;
        } else if (position < 0) {

            position = limited + position;
        }*/

        return position;
    }
}
