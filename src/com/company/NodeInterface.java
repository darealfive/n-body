package com.company;

import org.newdawn.slick.Graphics;

public interface NodeInterface {

    void show(Graphics g);

    void calculateForce(NodeInterface body, double timePassed);

    void merge(NodeInterface body);

    double getCenterOfMassPosX();

    double getCenterOfMassPosY();

    long getMass();
}
