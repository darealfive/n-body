package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.*;

public class Quadrant extends Node {

    private final int length;

    private boolean fill = false;

    private Color frameColor = new Color(255, 255, 255);

    private static final short MAC_TRESHOLD = 1;

    private int _bodyCounter = 0;

    private Mass _body;

    private Vector<NodeInterface> nodes = new Vector<>(1);

    private static Vector<Quadrant> exceededBoundaries = new Vector<>();

    private Quadrant parentNode = null;

    private CardinalPoint location = null;

    private HashMap<CardinalPoint, Quadrant> locations = new HashMap<>(1);

    Quadrant(int length, int startX, int startY) {

        super(new Square(startX, startY, length));
        this.length = (int) shape.getWidth();
    }

    private Quadrant(Quadrant parentNode, CardinalPoint location, int length, int startX, int startY) {

        this(parentNode, length, startX, startY);
        this.location = location;
    }

    private Quadrant(Quadrant parentNode, int length, int startX, int startY) {

        super(new Square(startX, startY, length));
        this.parentNode = parentNode;
        this.length = (int) shape.getWidth();
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

    @Override
    public void merge(Attractable body) {

        this._body.merge(body);
    }

    public void show(Graphics g) {

        for (Showable body : this.nodes) {

            body.show(g);
        }

        g.setColor(frameColor);
        if (fill) {

            g.setColor(new Color(255, 255, 255, 100));
            g.fill(shape);
        } else {

            g.draw(shape);
        }

        /*if (_bodyCounter == 1 && location != null) {

            g.drawString(location.name(), shape.getX(), shape.getY());
        }*/
        /*
        Quadrant node;
        for (Map.Entry<CardinalPoint, Quadrant> entry : this.locations.entrySet()) {

            node = entry.getValue();
            if (node._bodyCounter == 1) {

                node.showOrientation(g, entry.getKey());
            }
        }
        */


        if (doesContentExceedsBoundaries()) {

            g.setColor(new Color(255, 0, 0, 100));
            g.fill(shape);
        }

        if (this._bodyCounter > 1) {

            this._body.show(g);
        }
    }

    /**
     * Draws the given location at its topper left of this node
     *
     * @param g
     */
    void showOrientation(Graphics g, CardinalPoint orientation) {

        g.drawString(orientation.name(), shape.getX(), shape.getY());
    }

    public void calculateForce(Attractable body, double timePassed) {

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
                for (Attractable node : this.nodes) {

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
    private double getMAC(Attractable body) {

        return length / getDistanceTo(body);
    }

    /**
     * Adds the matter to this quadrant and additionally checks if the matter does exceeds the boundaries
     *
     * @param body
     */
    private void addInternal(NodeInterface body) {

        if (!shape.contains(body.getShape())) {

            exceededBoundaries.add(this);
        }
        this.nodes.add(body);
    }

    /**
     * Add a mass to this and increases the body counter.
     * If this does not contains any body, the mass object will be simply added.
     * If there is at least one body, the body will be removed adn inserted recursively in the quadrant tree till every
     * body is alone in its quadrant. Then the the given mass will be added and the process of recursive insertion will
     * be repeated.
     *
     * @param body
     */
    void add(Mass body) {

        if (this._bodyCounter == 0) {

            addInternal(body);
        } else {

            if (doesContentExceedsBoundaries()) {

                exceededBoundaries.remove(this);
            }

            Quadrant subQuadrant;
            if (this._bodyCounter == 1) {

                // Now the old body becomes a pseudo body (can get aggregated data)
                this.nodes.removeElement(this._body);

                subQuadrant = this.getQuadrant(this._body);

                // The next smallest quadrant will also have a length of 1 which means the recursion wont terminate
                // so we have to set the body into the next free quadrant OR if there is no free quadrant left, pick the
                // last non-free quadrant and put the particle there and try again.
                subQuadrant.add(this._body);

                // "Clone" the properties of the old body to be able to safely put it deeper into the recursion tree.
                // This way we can keep it virtually here and modify its properties without effecting the body deeper
                // in the tree.
                this._body = new Mass(this._body.getCenterOfMassPosX(), this._body.getCenterOfMassPosY(), this._body.getMass());
            }

            subQuadrant = this.getQuadrant(body);
            subQuadrant.add(body);
        }

        this._updateCenterOfMass(body);
    }

    private Quadrant getQuadrant(Locatable body) {

        Quadrant node = null;
        CardinalPoint location = null;

        if (length == 1) {

            // Finds the first non existing quadrant in the map in order they are listed in enum class or returns the north west quadrant.
            location = Arrays.stream(CardinalPoint.values())
                    .filter(q -> !locations.containsKey(q))
                    .findFirst()
                    .orElse(CardinalPoint.SE);

            // Regardless whether the quadrant exists or not, we want the first empty quadrant to be our new tree node. In most cases this would be the NW quadrant node.
            node = locations.putIfAbsent(
                    location,
                    new Quadrant(this, location, 1, 1, 1)
            );

        } else {

            int startPosX = (int) shape.getX();
            int startPosY = (int) shape.getY();
            int quadrantSideLength = (length / 2);
            int posXendOfWest = startPosX + quadrantSideLength;
            int posYendOfNorth = startPosY + quadrantSideLength;

            Horizontal horizontal;
            Vertical vertical;

            //Is it in a north quadrant?
            if (body.getCenterOfMassPosY() < posYendOfNorth) {

                horizontal = Horizontal.NORTH;
            } else {

                horizontal = Horizontal.SOUTH;
                startPosY = posYendOfNorth;
            }

            //Is it in a west quadrant?
            if (body.getCenterOfMassPosX() < posXendOfWest) {

                vertical = Vertical.WEST;
            } else {

                vertical = Vertical.EAST;
                startPosX = posXendOfWest;
            }

            try {

                location = CardinalPoint.getCardinalPoint(horizontal, vertical);

                node = locations.putIfAbsent(
                        location,
                        new Quadrant(this, location, quadrantSideLength, startPosX, startPosY)
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        // Is null if element was not there before putIfAbsent => no body in it. This also means it is new and needs to
        // be stored in the node list of this node.
        if (node == null) {

            node = locations.get(location);
            nodes.add(node);
        }

        return node;
    }

    /**
     * Checks if its matter exceeds the boundaries of this Quadrant
     *
     * @return
     */
    private boolean doesContentExceedsBoundaries() {
        return exceededBoundaries.contains(this);
    }

    /**
     * Updates center of mass of this node with the new one.
     *
     * @param body the NodeInterface to be added to this
     */
    private void _updateCenterOfMass(Mass body) {

        if (_body == null) {
            _body = body;
        } else {
            this.merge(body);
        }
        this._bodyCounter++;
    }
}
