package com.company;

import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

public class God {

    public static void main(String[] args) {

        Universe universe = new Universe((int)Math.pow(2,9), (int)Math.pow(2,9));

        generateBodies(universe, 1000);

        universe.live();
    }

    private static void generateBodies(Universe universe, int nBodies) {

        int min = 0, max = universe.getWidth(), x, y;
        float mass;
        universe.space.bodies = new Vector<>(nBodies);

        universe.space.bodies.addElement(new Matter(64, 64, 3));
        universe.space.bodies.addElement(new Matter(64, 65, 3));

        /*universe.space.bodies.addElement(new Matter(100, 300, 1));
        universe.space.bodies.addElement(new Matter(100, 100, 3));
        universe.space.bodies.addElement(new Matter(100, 110, 3));
        universe.space.bodies.addElement(new Matter(100, 120, 3));*/

        /*universe.space.bodies.addElement(new Matter(0, 0, 1));
        universe.space.bodies.addElement(new Matter(100, 100, 1));
        universe.space.bodies.addElement(new Matter(300, 200, 3));
        universe.space.bodies.addElement(new Matter(300, 220, 3));
        universe.space.bodies.addElement(new Matter(200, 180, 3));
        universe.space.bodies.addElement(new Matter(100, 180, 3));
        universe.space.bodies.addElement(new Matter(200, 150, 3));*/
        /*for (int i = 0; i <= nBodies; i++) {

            x = ThreadLocalRandom.current().nextInt(min, max + 1);
            y = ThreadLocalRandom.current().nextInt(min, max + 1);
            mass = ThreadLocalRandom.current().nextFloat();
            universe.space.bodies.addElement(new Matter(x, y, 1));
        }*/
    }
}
