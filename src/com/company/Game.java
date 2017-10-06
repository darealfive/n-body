package com.company;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class Game extends BasicGame {

    private Universe universe;

    Game(String title) {
        super(title);
    }

    @Override
    public void init(GameContainer gameContainer) throws SlickException {

        int universeSideLength = Integer.highestOneBit((gameContainer.getWidth() > gameContainer.getHeight()) ?
                gameContainer.getHeight() :
                gameContainer.getWidth());
        universe = new Universe(universeSideLength, universeSideLength);
        generateBodies(universe, 4);
    }

    @Override
    public void update(GameContainer gameContainer, int delta) throws SlickException {

        universe.live(((double) delta) / 1000);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {

        universe.render(graphics);
    }

    private static void generateBodies(Universe universe, int nBodies) {

        int min = 0, max = universe.getWidth(), x, y;

        //universe.space.addMass(new Mass(190, 280, 5.972 * Math.pow(10,24)));
        //universe.space.addMass(new DebugMatter(0,0,190, 280 + 6_371_000, 1));

        double mass = 5.972 * Math.pow(10, 14);
        universe.space.addMass(new Matter(0.5, 0, 250, 260, mass));
        universe.space.addMass(new Matter(-0.5, 0, 250, 250, mass));

        universe.space.addMass(new DebugMatter(-0.1, 0, 250, 359, 5.972 * Math.pow(10, 12)));

        //universe.space.addMass(new DebugMatter(0.1, 0, 200, 560, 6000));

        /*
        universe.space.bodies.addElement(new Matter(200, 450, 6000));
        universe.space.bodies.addElement(new Matter(50, 300, 6000));
        universe.space.bodies.addElement(new Matter( 200, 310, 999999999));
        universe.space.bodies.addElement(new Matter(350, 300, 6000));
        universe.space.bodies.addElement(new Matter(200, 150, 6000));
*/

        //universe.space.bodies.addElement(new Matter(0,0, 140, 190, 200));

        //universe.space.bodies.addElement(new Matter(64, 64, 3));
        //universe.space.bodies.addElement(new Matter(66, 66, 3));

        //universe.space.bodies.addElement(new Matter(1, 1, 3));
        // universe.space.bodies.addElement(new Matter(500, 500, 3));
        // universe.space.bodies.addElement(new Matter(490, 490, 3));

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
