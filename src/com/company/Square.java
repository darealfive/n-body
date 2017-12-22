package com.company;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Square extends Rectangle {
    Square(float x, float y, float length) {
        super(x, y, length, length);
    }

    public boolean contains(Shape other) {
        if (!other.intersects(this)) {
            return false;
        } else {
            for(int i = 0; i < other.getPointCount(); ++i) {
                float[] pt = other.getPoint(i);
                if (!this.contains(pt[0], pt[1])) {
                    return false;
                }
            }

            return true;
        }
    }
}
