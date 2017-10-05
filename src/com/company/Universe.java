package com.company;

import org.newdawn.slick.Graphics;

public class Universe {

    int width, height;
    public SpaceTime space;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Universe(int width, int height) {

        this.width = width;
        this.height = height;
        space = new SpaceTime(width, height);
    }

    public void live(double delta) {

        space.run(delta);
    }

    public void render(Graphics graphics) {

        space.render(graphics);
    }
}
