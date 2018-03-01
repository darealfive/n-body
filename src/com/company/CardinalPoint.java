package com.company;

/**
 * Aggregates the two locations components "Horizontal" and "Vertical" to the 4 cardinal points
 */
public enum CardinalPoint {

    NE(Horizontal.NORTH, Vertical.EAST),
    NW(Horizontal.NORTH, Vertical.WEST),
    SE(Horizontal.SOUTH, Vertical.EAST),
    SW(Horizontal.SOUTH, Vertical.WEST);

    private final Horizontal horizontal;
    private final Vertical vertical;

    CardinalPoint(Horizontal horizontal, Vertical vertical) {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    /**
     * Gets the cardinal point for the locational combination of Vertical and Horizontal
     *
     * @param horizontal
     * @param vertical
     * @return
     * @throws Exception
     */
    public static CardinalPoint getCardinalPoint(Horizontal horizontal, Vertical vertical) throws Exception {

        for (CardinalPoint location : CardinalPoint.values()) {

            if (location.horizontal == horizontal && location.vertical == vertical) {

                return location;
            }
        }

        throw new Exception("There is no such CardinalPoint available");
    }

    /**
     * Gets the horizontal component of the location.
     *
     * @return the horizontal component
     */
    public Horizontal getHorizontal() {

        return horizontal;
    }

    /**
     * Gets the vertical component of the location.
     *
     * @return the vertical component
     */
    public Vertical getVertical() {

        return vertical;
    }
}

/**
 * Defines all possible horizontal directions
 */
enum Horizontal {
    NORTH, SOUTH
}

/**
 * Defines all possible vertical directions
 */
enum Vertical {
    EAST, WEST
}
