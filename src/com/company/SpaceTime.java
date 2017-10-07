package com.company;

import org.newdawn.slick.Graphics;

import java.util.Vector;

public class SpaceTime {

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

    double determinePosX(Locatable body, double offset) {

        return determinePosition(body.getCenterOfMassPosX() + offset, getWidth());
    }

    double determinePosY(Locatable body, double offset) {

        return determinePosition(body.getCenterOfMassPosY() + offset, getHeight());
    }

    private double determinePosition(double relativePosition, double limited) {

        if (relativePosition > limited) {

            relativePosition = limited;
        } else if (relativePosition < 0) {

            relativePosition = 0;
        }

        return relativePosition;
    }
}
