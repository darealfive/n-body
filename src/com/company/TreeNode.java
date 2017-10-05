package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.*;

public class TreeNode extends Node {

    private boolean fill = false;
    private Color frameColor = new Color(255, 255, 255);

    private static final short MAC_TRESHOLD = 1;

    private int _bodyCounter = 0;

    private int _length;
    private int _startX;
    private int _startY;

    private TreeNode nw, ne, sw, se;
    private NodeInterface _body;

    private Vector<NodeInterface> nodes = new Vector<>(1);

    TreeNode(int length, int startX, int startY) {

        this._length = length;
        this._startX = startX;
        this._startY = startY;
    }

    public double getCenterOfMassPosX() {
        return this._body.getCenterOfMassPosX();
    }

    public double getCenterOfMassPosY() {
        return this._body.getCenterOfMassPosY();
    }

    public double getMass() {

        return this._body.getMass();
    }

    public void merge(NodeInterface body) {

        if (this._body == null) {
            this._body = body;
        } else {
            this._body.merge(body);
        }
    }

    public void show(Graphics g) {

        Color originalColor = g.getColor();
        for (NodeInterface body : this.nodes) {

            body.show(g);
        }

        g.setColor(frameColor);
        if (fill) {

            Color c = new Color(255, 255, 255, 100);
            g.setColor(c);
            g.fillRect(this._startX, this._startY, this._length, this._length);
            g.setColor(frameColor);
        } else {

            g.drawRect(this._startX, this._startY, this._length, this._length);
        }

        // Draw pseudo body slightly different
        if (this._bodyCounter > 1) {

            this._body.show(g);
        }

        g.setColor(originalColor);
    }

    public void calculateForce(NodeInterface body, double timePassed) {

        // No more elements in this node
        if (this._bodyCounter == 1) {

            // Calculate force to body directly
            body.calculateForce(this._body, timePassed);

        } else {

            //Calculate MAC (multipole acceptance criterion)
            if (getMAC(body) < MAC_TRESHOLD) {

                if (body instanceof DebugMatter) {
                    this.fill = true;
                }
                // Calculate force to body by using center of mass
                body.calculateForce(this._body, timePassed);
            } else {

                //Try it one lvl deeper
                for (NodeInterface node : this.nodes) {

                    node.calculateForce(body, timePassed);
                }
            }
        }
    }

    /**
     * Gets the actual MAC (Multipole-Acceptance-Criterion) to a given body
     *
     * @param body to test the length/distance ratio against
     * @return mac
     */
    private double getMAC(NodeInterface body) {

        return this._length / getDistanceTo(body);
    }

    void add(NodeInterface body) {

        if (this._bodyCounter == 0) {

            this.nodes.add(body);
        } else {

            TreeNode subQuadrant;
            if (this._bodyCounter == 1) {

                // Now the old body becomes a pseudo body (can get aggregated data)
                this.nodes.removeElement(this._body);

                // "Clone" the properties of the old body to be able to safely put it deeper into the recursion tree.
                // This way we can keep it virtually here and modify its properties without effecting the body deeper
                // in the tree.
                Node oldBody = new Mass(this._body.getCenterOfMassPosX(), this._body.getCenterOfMassPosY(), this._body.getMass());
                subQuadrant = this.getQuadrant(this._body);

                // The next smallest quadrant will also have a length of 1 which means the recursion wont terminate
                // so we have to set the body into the next free quadrant OR if there is no free quadrant left, pick the
                // last non-free quadrant and put the particle there and try again.
                subQuadrant.add(this._body);
                this._body = oldBody;
            }

            subQuadrant = this.getQuadrant(body);
            subQuadrant.add(body);
        }

        this._updateCenterOfMass(body);
    }

    private TreeNode getQuadrant(NodeInterface body) {

        TreeNode node;
        if (this._length == 1) {

            node = null;
            if (nw == null) {
                node = nw = new TreeNode(1, 1, 1);
            } else if (ne == null) {
                node = ne = new TreeNode(1, 1, 1);
            } else if (sw == null) {
                node = sw = new TreeNode(1, 1, 1);
            } else if (se == null) {
                node = se = new TreeNode(1, 1, 1);
            }

            node = (node == null) ? nw : node;
            nodes.add(node);
            return node;
        }

        int posX, posY;
        int quadrantSideLength = (this._length / 2);
        int posXendOfWest = this._startX + quadrantSideLength;
        int posYendOfNorth = this._startY + quadrantSideLength;
        //Is it in a north quadrant?
        if (body.getCenterOfMassPosY() < posYendOfNorth) {

            posY = this._startY;
            //Is it in a west quadrant?
            if (body.getCenterOfMassPosX() < posXendOfWest) {

                posX = this._startX;
                if (this.nw == null) {
                    this.nw = new TreeNode(quadrantSideLength, posX, posY);
                    this.nodes.add(this.nw);
                }
                node = this.nw;
            } else {

                posX = posXendOfWest;
                if (this.ne == null) {
                    this.ne = new TreeNode(quadrantSideLength, posX, posY);
                    this.nodes.add(this.ne);
                }
                node = this.ne;
            }
        } else {

            posY = posYendOfNorth;
            if (body.getCenterOfMassPosX() < posXendOfWest) {

                posX = this._startX;
                if (this.sw == null) {
                    this.sw = new TreeNode(quadrantSideLength, posX, posY);
                    this.nodes.add(this.sw);
                }
                node = this.sw;
            } else {

                posX = posXendOfWest;
                if (this.se == null) {
                    this.se = new TreeNode(quadrantSideLength, posX, posY);
                    this.nodes.add(this.se);
                }
                node = this.se;
            }
        }

        return node;
    }

    /**
     * Updates center of mass of this node with the new one.
     *
     * @param body the NodeInterface to be added to this
     */
    private void _updateCenterOfMass(NodeInterface body) {

        this.merge(body);
        this._bodyCounter++;
    }
}
