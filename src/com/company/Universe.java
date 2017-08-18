package com.company;

import javax.swing.*;

public class Universe extends JFrame {

    public SpaceTime space;

    public Universe(int width, int height) {

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(width, height);
        this.space = new SpaceTime();
    }

    public void live() {

        this.add(this.space);
        this.setVisible(true);
        this.space.run();
    }
}
