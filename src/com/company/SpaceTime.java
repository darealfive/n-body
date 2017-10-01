package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SpaceTime extends JPanel {

    private long currentTimeStep;

    private TreeNode _rootNode;

    Vector<Matter> bodies;

    @Override
    public void paint(Graphics g) {

        if (this._rootNode == null) {

            super.paint(g);
        } else {

            this._rootNode.show(g);
        }
    }

    void run() {

        long currentTimeStep;
        BarnesHutTree tree = new BarnesHutTree(this);

        //Time left is of course 0 :)
        double passedTime = 0;
        // Now the time starts running
        this.currentTimeStep = System.nanoTime();

        while (true) {

            this._rootNode = tree.build();
            // Measure time how long it took to build the tree. Only the mass distribution is calculated.
            // In all the following iterations the time needed to fully calc the tree is also in the time.
            currentTimeStep = System.nanoTime();

            this.repaint();

            passedTime = new Long(currentTimeStep - this.currentTimeStep).doubleValue() / 1_000_000_000;
            this.currentTimeStep = currentTimeStep;

            passedTime = 1;
            applyPhysics(passedTime);
        }
    }

    private void applyPhysics(double passedTime) {

        for (Matter body : this.bodies) {

            this._rootNode.calculateForce(body, passedTime);
        }

        for (Matter body : this.bodies) {

            body.applyPhysics();
        }
    }
}
