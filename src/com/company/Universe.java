package com.company;

import org.newdawn.slick.Graphics;

public class Universe {

    private int width, height;
    SpaceTime space;

    int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    Universe(int width, int height) {

        this.width = width;
        this.height = height;
        space = new SpaceTime(width, height);
    }

    void live(double delta) {

        space.run(delta);
    }

    void render(Graphics graphics) {

        space.render(graphics);
    }
}
