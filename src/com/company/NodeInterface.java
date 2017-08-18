package com.company;

import java.awt.*;

public interface NodeInterface {

    void show(Graphics g);

    void calculateForce(NodeInterface body);

    void merge(NodeInterface body);

    float getCenterOfMassPosX();

    float getCenterOfMassPosY();

    float getMass();
}
