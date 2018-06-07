package com.company;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import java.util.ArrayList;
import java.util.List;

class SpaceTime {

    double delta = 0;

    final double stickyAreaDimensionX, stickyAreaDimensionY;

    /**
     * The area dimension in percent at the edge of the universe is a bit sticky for every {@link Acceleratable} body.
     * So they will slow down dependent till the end of the universe to a speed of 0 (if they reach the end).
     */
    private static final short stickyArea = 5;

    private final int width, height;

    private PhysicsEngine physicsEngine;

    private List<Mass> bodies = new ArrayList<>(500);

    List<Mass> getBodies() {
        return bodies;
    }

    void addMass(Mass object) {

        bodies.add(object);
    }

    int getWidth() {
        return width;
    }

    int getHeight() {
        return height;
    }

    SpaceTime(int width, int height) {

        this.width = width;
        this.height = height;

        stickyAreaDimensionX = ((this.width / 100.0f) * (double) stickyArea);
        stickyAreaDimensionY = ((this.height / 100.0f) * (double) stickyArea);
    }

    void render(Graphics graphics) {

        if (physicsEngine != null) {
            physicsEngine.applyPhysics();
            physicsEngine.show(graphics);
            DebugMatter.maxVelocityX = 0;
            DebugMatter.maxVelocityY = 0;
        }

        // Draws the sticky area
        graphics.setColor(new Color(255, 255, 255, 100));
        graphics.drawRect((float) stickyAreaDimensionX, (float) stickyAreaDimensionY, 512 - (2 * (float) stickyAreaDimensionX), 512 - (2 * (float) stickyAreaDimensionY));
    }

    void run(double delta) {

        this.delta = delta;
        //physicsEngine = new BrutforcePhysics(this);
        physicsEngine = new BarnesHutPhysics(this, (short) 1);
        physicsEngine.run();
    }
}
