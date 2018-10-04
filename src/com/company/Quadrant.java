package com.company;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import com.company.CardinalPoint.*;

import java.util.*;
import java.util.stream.Collectors;

public class Quadrant extends Node {

    private final int length;

    private boolean fill = false;

    private Color frameColor = new Color(255, 255, 255);

    private final BarnesHutTree barnesHutTree;

    private int _bodyCounter = 0;

    private Mass _body;

    private List<NodeInterface> nodes = new ArrayList<>(4);

    private Quadrant parentNode = null;

    /**
     * Bidirectional connects BisectorCardinalPoint with Quadrant
     */
    private BidiMap<BisectorCardinalPoint, Quadrant> locations = new DualHashBidiMap<>();

    Quadrant(int length, BarnesHutTree barnesHutTree) {
        this(length, 0, 0, barnesHutTree);
    }

    private Quadrant(int length, int startX, int startY, BarnesHutTree barnesHutTree) {

        super(new Square(startX, startY, length));
        this.length = (int) shape.getWidth();
        this.barnesHutTree = barnesHutTree;
    }

    private Quadrant(Quadrant parentNode, int length, int startX, int startY) {

        this(length, startX, startY, parentNode.barnesHutTree);
        this.parentNode = parentNode;
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

        /*if (location != null) {

            if (_bodyCounter == 1) {

                g.drawString(location.name(), (shape.getMinX() + shape.getMaxX()) / 2 - 10, (shape.getMinY() + shape.getMaxY()) / 2 - 10);
            } else {

                g.drawString(location.name(), shape.getX(), shape.getY());
            }
        }*/

        /*
        Quadrant node;
        for (Map.Entry<BisectorCardinalPoint, Quadrant> entry : this.locations.entrySet()) {

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
    void showOrientation(Graphics g, BisectorCardinalPoint orientation) {

        g.drawString(orientation.name(), shape.getX(), shape.getY());
    }

    public void calculateForce(Attractable body, double timePassed) {

        // No more elements in this node
        if (this._bodyCounter == 1) {

            // Calculate force to body directly
            body.calculateForce(this._body, timePassed);

        } else {

            //Calculate MAC (multipole acceptance criterion)
            if (getMAC(body) < barnesHutTree.getMacThreshold()) {

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

    private int resolveBisectorCardinalCollision(BisectorCardinalPoint origLocation, Mass body, BisectorCardinalPoint bisectorCardinalPoint, float offsetX, float offsetY) {
        return 0;
    }

    /**
     * Traverse as long as needed up the parent quadrants till child quadrant is found which may be touched by our
     * particle.
     *
     * @param sourceQuadrant      the source quadrant which called this method
     * @param breadcrumbs         the path to find a route back to the inner quadrants which may be touched by out particle
     * @param body                the particle which exceeds a boundary of its enclosing quadrant
     * @param cardinalPointOffset the violated boundary
     * @param offset              relative to the outer violated boundary
     * @return number of collisions detected
     */
    private int resolveCardinalCollision(Quadrant sourceQuadrant, Deque<BisectorCardinalPoint> breadcrumbs, Mass body, CardinalPoint cardinalPointOffset, float offset) {

        //Gets the bisector cardinal point of source quadrant
        BisectorCardinalPoint sourceBisectorCardinalPoint = locations.getKey(sourceQuadrant);

        int collisions = 0;

        //Have we come from the target direction? If yes, we must check parent quadrants because here we don't find a
        //foreign quadrant which exists on target direction.
        if (sourceBisectorCardinalPoint.containsCardinalPoint(cardinalPointOffset)) {

            //If we can not look more from the outside, there is no collision we can expect anymore and the particle
            //does not collide with anything.
            if (null == parentNode) {
                return collisions;
            }

            //Adds this to the end of the breadcrumbs to complete the path to this call
            breadcrumbs.addLast(sourceBisectorCardinalPoint);
            collisions = parentNode.resolveCardinalCollision(this, breadcrumbs, body, cardinalPointOffset, offset);
        } else {

            //The target quadrant should be located in the opposite direction of our source quadrant we are coming from.
            BisectorCardinalPoint targetBisectorCardinalPoint = sourceBisectorCardinalPoint.getCombinedWithCardinalPoint(cardinalPointOffset);
            Quadrant checkQuadrant = locations.get(targetBisectorCardinalPoint);

            if (checkQuadrant != null) {

                // Note that offset might be less than the original offset due to void quadrants we jumped over to get
                // there where we are now => the offset is relative to the quadrant we are currently checking
                collisions = checkQuadrant.searchCollisions(breadcrumbs.descendingIterator(), body, cardinalPointOffset.getOpposite(), offset);
            }

            if (parentNode != null && ((offset -= (((float) length) / 2)) > 0)) {

                //Adds this to the end of the breadcrumbs to complete the path to this call
                breadcrumbs.addLast(sourceBisectorCardinalPoint);
                collisions += parentNode.resolveCardinalCollision(this, breadcrumbs, body, cardinalPointOffset, offset);
            }
        }

        return collisions;
    }

    private int searchCollisions(Iterator<BisectorCardinalPoint> breadcrumbs, Mass body, CardinalPoint targetDirection, float offset) {

        if (breadcrumbs.hasNext()) {

            BisectorCardinalPoint nextTarget = breadcrumbs.next();
            BisectorCardinalPoint location = nextTarget.getCombinedWithCardinalPoint(targetDirection);
        }

        if (_bodyCounter == 1) {

            return body.collidesWith(_body) ? 1 : 0;
        }

        // Collect list of existing locations we have to check in this node
        //Set<Map.Entry<BisectorCardinalPoint, Quadrant>> targets = locations.entrySet();
        Map<BisectorCardinalPoint, Quadrant> targets = new HashMap<>(locations);

        // Here we check if it be valid to not check all locations in this node
        if ((((float) length) / 2) > offset) {
            //TODO the positions of particles using its shape is not accurate: init x with 266 and you will get 255!!!! FIX IT
            //targets.removeIf(e -> !e.getKey().containsCardinalPoint(targetDirection));
            targets = targets.entrySet()
                    .stream()
                    .filter(e -> e.getKey().containsCardinalPoint(targetDirection))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        int collisions = 0;
        if (!targets.isEmpty()) {

            for (Map.Entry<BisectorCardinalPoint, Quadrant> target : targets.entrySet()) {

                collisions += target.getValue().searchCollisions(breadcrumbs, body, targetDirection, offset);
            }
        }

        return collisions;
    }

    /**
     * Detects collisions of this particle with another particle of another quadrant. The check includes only quadrants
     * which are located in the provided direction (limited by the amount of offset the boundary has been exceeded).
     * <p>
     * Following steps will be executed:
     * 1. Store the source direction {@link BisectorCardinalPoint} as breadcrumbs to navigate trough the binary tree
     * 2. Step 1 level up the parent node and start searching at provided directions
     *
     * @param violatedBoundariesOffsets all violated boundaries (with their offset violation) of this quadrant to check.
     */
    void collisionDetection(Map<CardinalPoint, Float> violatedBoundariesOffsets) {

        if (_bodyCounter > 1) {

            throw new RuntimeException("Counted more than one body");
        }

        int collisionsDetected = 0;
        float offset;
        CardinalPoint violatedBoundary;
        Deque<BisectorCardinalPoint> breadcrumbs;
        //We might encounter every combination of both horizontal and vertical collision directions.
        Map<Horizontal, Float> horizontals = new HashMap<>(1);
        Map<Vertical, Float> verticals = new HashMap<>(1);
        for (Map.Entry<CardinalPoint, Float> violatedBoundaryOffset : violatedBoundariesOffsets.entrySet()) {

            violatedBoundary = violatedBoundaryOffset.getKey();
            offset = violatedBoundaryOffset.getValue();
            //Resolve collisions in the exceeded boundaries.
            //Additionally build all possible bisector cardinal points out of all violated cardinal points to iterate later trough
            if (violatedBoundary.isHorizontal()) {

                horizontals.put(violatedBoundary.getHorizontal(), offset);
            } else {

                verticals.put(violatedBoundary.getVertical(), offset);
            }

            collisionsDetected += parentNode.resolveCardinalCollision(this, new LinkedList<>(), _body, violatedBoundary, offset);
        }

        //TODO Wenn bspw. im Norden und Westen die Grenzen überschritten werden, heißt das nicht unbedingt, dass auch die Nordwestliche Ecke überschriten wurde.
        //Try all cardinal points and resolve collisions in the edges
        for (Map.Entry<Horizontal, Float> horizontal : horizontals.entrySet()) {
            for (Map.Entry<Vertical, Float> vertical : verticals.entrySet()) {

                BisectorCardinalPoint bisectorCardinalPoint = BisectorCardinalPoint.getBisectorCardinalPoint(
                        horizontal.getKey(),
                        vertical.getKey()
                );

                //collisionsDetected += parentNode.resolveBisectorCardinalCollision(location, _body, bisectorCardinalPoint, vertical.getValue(), horizontal.getValue());
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
     * Adds the mass to this quadrant and additionally checks if the matter does exceeds the boundaries
     *
     * @param body
     */
    private void addInternal(Mass body) {

        Map<CardinalPoint, Float> cardinalPointOffset = new HashMap<>(2);
        float offset;
        if ((offset = body.shape.getMinY() - shape.getMinY()) < 0) {
            //NORTH
            cardinalPointOffset.put(CardinalPoint.N, offset);
        }
        if ((offset = body.shape.getMaxX() - shape.getMaxX()) > 0) {
            //EAST
            cardinalPointOffset.put(CardinalPoint.E, offset);
        }
        if ((offset = body.shape.getMaxY() - shape.getMaxY()) > 0) {
            //SOUTH
            cardinalPointOffset.put(CardinalPoint.S, offset);
        }
        if ((offset = body.shape.getMinX() - shape.getMinX()) < 0) {
            //WEST
            cardinalPointOffset.put(CardinalPoint.W, offset);
        }

        if (!cardinalPointOffset.isEmpty()) {

            barnesHutTree.getViolatedQuadrantCardinalPoints().put(this, cardinalPointOffset);
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

            //This adds the body and takes care whether the new mass exceeds one of the 4 boundaries of this quadrant
            addInternal(body);
        } else {

            //If the particles gets putted deeper in the recursion tree (which is the case here), we reset the state of
            //exceeded boundaries because only the quadrant counts, which counts only 1 particle.
            if (doesContentExceedsBoundaries()) {

                barnesHutTree.getViolatedQuadrantCardinalPoints().remove(this);
            }

            Quadrant subQuadrant;
            if (this._bodyCounter == 1) {

                // Now the old body becomes a pseudo body (can get aggregated data)
                this.nodes.remove(this._body);

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
        BisectorCardinalPoint location = null;

        if (length == 1) {

            // Finds the first non existing quadrant in the map in order they are listed in enum class or returns the north west quadrant.
            location = Arrays.stream(BisectorCardinalPoint.values())
                    .filter(q -> !locations.containsKey(q))
                    .findFirst()
                    .orElse(BisectorCardinalPoint.NW);

            // Regardless whether the quadrant exists or not, we want the first empty quadrant to be our new tree node. In most cases this would be the NW quadrant node.
            node = locations.putIfAbsent(
                    location,
                    new Quadrant(this, 1, 1, 1)
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

                location = BisectorCardinalPoint.getBisectorCardinalPoint(horizontal, vertical);

                node = locations.putIfAbsent(
                        location,
                        new Quadrant(this, quadrantSideLength, startPosX, startPosY)
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
        return barnesHutTree.getViolatedQuadrantCardinalPoints().containsKey(this);
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
