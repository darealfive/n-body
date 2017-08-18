package com.company;

import java.awt.*;
import java.util.Vector;

public class TreeNode implements Node {

    private int _bodyCounter = 0;

    private int _length;
    private int _startX;
    private int _startY;

    private TreeNode nw, ne, sw, se;
    private Node _body;

    private Vector<Node> nodes = new Vector<>(1);

    public TreeNode(int length, int startX, int startY) {

        this._length = length;
        this._startX = startX;
        this._startY = startY;
    }

    public float getCenterOfMassPosX() {
        return this._body.getCenterOfMassPosX();
    }

    public float getCenterOfMassPosY() {
        return this._body.getCenterOfMassPosY();
    }

    public float getMass() {

        return this._body.getMass();
    }

    public void merge(Node body) {

        if (this._body == null) {
            this._body = body;
        } else {
            this._body.merge(body);
        }
    }

    public void show(Graphics g) {

        Color originalColor = g.getColor();
        for (Node body : this.nodes) {

            body.show(g);
        }

        g.drawRect(this._startX, this._startY, this._length, this._length);

        // Draw pseudo body slightly different
        if (this._bodyCounter > 1) {

            int length = 10;
            g.setColor(new Color(255, 0, 0, 100));
            g.fillArc((int) (this.getCenterOfMassPosX() - (length / 2)), (int) (this.getCenterOfMassPosY() - (length / 2)), length, length, 0, 360);
        }

        g.setColor(originalColor);
    }

    public void calculateForce(Node body) {

        // No more elements in this node
        if (this._bodyCounter == 1) {

            // Calculate force to body directly
            body.calculateForce(this._body);

        } else {

            //Get distance between particle and center of mass
            //Calculate relation between distance and length of quadrant

            boolean treshold = false;

            if (treshold) {

                // Calculate force to body by using center of mass
                body.calculateForce(this._body);
            } else {

                //Try it one lvl deeper
                for (Node node : this.nodes) {

                    node.calculateForce(body);
                }
            }
        }
    }

    public void add(Node body) {

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
                Node oldBody = new Matter(this._body.getCenterOfMassPosX(), this._body.getCenterOfMassPosY(), this._body.getMass());
                subQuadrant = this.getQuadrant(oldBody);

                // The next smallest quadrant will also have a length of 1 which means the recursion wont terminate
                // so we have to set the body into the next free quadrant OR if there is no free quadrant left, pick the
                // last non-free quadrant and put the particle there and try again.
                if (this._length == 1) {

                }
                subQuadrant.add(oldBody);
            }

            subQuadrant = this.getQuadrant(body);
            subQuadrant.add(body);
        }

        this._updateCenterOfMass(body);
    }

    private TreeNode getQuadrant(Node body) {

        int posX, posY;
        int quadrantSideLength = (this._length / 2);
        int posXendOfWest = this._startX + quadrantSideLength;
        int posYendOfNorth = this._startY + quadrantSideLength;

        TreeNode node;

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
     * @param body the Node to be added to this
     */
    private void _updateCenterOfMass(Node body) {

        this.merge(body);
        this._bodyCounter++;
    }
}
