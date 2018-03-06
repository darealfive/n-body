package com.company;

import java.util.ArrayList;
import java.util.List;

import static com.company.CardinalPoint.Horizontal.*;
import static com.company.CardinalPoint.Vertical.*;

/**
 * Aggregates the two locations components "Horizontal" and "Vertical" to the 4 cardinal points
 */
public enum CardinalPoint {

    N(NORTH),
    E(EAST),
    S(SOUTH),
    W(WEST);

    public CardinalPoint getOpposite() {
        Horizontal horizontal = null;
        Vertical vertical = null;

        if (isHorizontal()) {
            horizontal = getHorizontal().getOpposite();
        } else {
            vertical = getVertical().getOpposite();
        }

        return getCardinalPoint(horizontal, vertical);
    }

    public Horizontal getHorizontal() {
        return horizontal;
    }

    public Vertical getVertical() {
        return vertical;
    }

    public final boolean isHorizontal() {
        return horizontal != null;
    }

    public final boolean isVertical() {
        return vertical != null;
    }

    public static List<CardinalPoint> getHorizontals() {
        List<CardinalPoint> horizontals = new ArrayList<>(2);
        horizontals.add(N);
        horizontals.add(S);
        return horizontals;
    }

    public static List<CardinalPoint> getVerticals() {
        List<CardinalPoint> verticals = new ArrayList<>(2);
        verticals.add(E);
        verticals.add(W);
        return verticals;
    }

    enum BisectorCardinalPoint {
        NE(NORTH, EAST),
        SE(SOUTH, EAST),
        SW(SOUTH, WEST),
        NW(NORTH, WEST);

        private final Horizontal horizontal;
        private final Vertical vertical;

        BisectorCardinalPoint(Horizontal horizontal, Vertical vertical) {
            this.horizontal = horizontal;
            this.vertical = vertical;
        }

        /**
         * Gets the bisector cardinal point for the locational combination of Vertical and Horizontal
         *
         * @param horizontal
         * @param vertical
         * @return
         */
        public static BisectorCardinalPoint getBisectorCardinalPoint(Horizontal horizontal, Vertical vertical) {

            for (BisectorCardinalPoint location : BisectorCardinalPoint.values()) {

                if (location.horizontal == horizontal && location.vertical == vertical) {

                    return location;
                }
            }

            throw new RuntimeException("There is no such BisectorCardinalPoint available");
        }
    }

    /**
     * Defines all possible horizontal directions
     */
    enum Horizontal {
        NORTH, SOUTH;

        public Horizontal getOpposite() {
            if (equals(NORTH)) {
                return SOUTH;
            } else {
                return NORTH;
            }
        }
    }

    /**
     * Defines all possible vertical directions
     */
    enum Vertical {
        EAST, WEST;

        public Vertical getOpposite() {
            if (equals(EAST)) {
                return WEST;
            } else {
                return EAST;
            }
        }
    }

    /**
     * Gets the cardinal point for the locational combination of Vertical and Horizontal
     *
     * @param horizontal
     * @param vertical
     * @return
     */
    public static CardinalPoint getCardinalPoint(Horizontal horizontal, Vertical vertical) {

        for (CardinalPoint location : CardinalPoint.values()) {

            if (location.horizontal == horizontal && location.vertical == vertical) {

                return location;
            }
        }

        throw new RuntimeException("There is no such BisectorCardinalPoint available");
    }

    private final Horizontal horizontal;
    private final Vertical vertical;

    CardinalPoint(Horizontal horizontal) {
        this(horizontal, null);
    }

    CardinalPoint(Vertical vertical) {
        this(null, vertical);
    }

    CardinalPoint(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }
}
