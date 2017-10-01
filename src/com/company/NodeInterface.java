package com.company;

import java.awt.*;

public interface NodeInterface {

    void show(Graphics g);

    void calculateForce(NodeInterface body, double timePassed);

    void merge(NodeInterface body);

    double getCenterOfMassPosX();

    double getCenterOfMassPosY();

    float getMass();
}
