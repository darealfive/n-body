package com.company;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SpaceTime extends JPanel {

    private long passedTime, currentTimeStep;

    private TreeNode _rootNode;

    public Vector<Matter> bodies;

    //private long timePassedBy;

    public SpaceTime() {

    }

    @Override
    public void paint(Graphics g) {

        if (this._rootNode == null) {

            super.paint(g);
        } else {

            this._rootNode.show(g);
        }
    }

    public void run() {

        BarnesHutTree tree;
        this.passedTime = System.currentTimeMillis();

        //while (true) {

        this.currentTimeStep = System.currentTimeMillis() - this.passedTime;
        this.passedTime = System.currentTimeMillis();
        System.out.println(this.currentTimeStep);
        tree = new BarnesHutTree(this);
        this._rootNode = tree.build();

        this.repaint();
        //}
    }
}
