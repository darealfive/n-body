package com.company;

import java.awt.*;

public interface Node {

    void show(Graphics g);

    void calculateForce(Node body);

    void merge(Node body);

    float getCenterOfMassPosX();

    float getCenterOfMassPosY();

    float getMass();
}
