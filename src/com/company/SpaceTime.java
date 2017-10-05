package com.company;

import org.newdawn.slick.Graphics;

import java.util.Vector;

public class SpaceTime {

    private int width, height;

    private TreeNode _rootNode;

    Vector<Matter> bodies;

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

        for (Matter body : this.bodies) {

            body.applyPhysics();
        }
    }
}
