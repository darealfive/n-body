package de.darealfive;

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

        double mass = 3.0 * Math.pow(10, 14);

        //normale Veränderungen
        //universe.space.addMass(new Mass( 250, 265, mass));
        //universe.space.addMass(new Mass(250, 245, mass));

        int fac = 10;
        //Nicht normale Veränderungen
        /*universe.space.addMass(new DebugMatter(0.5, -0.1, 250, 285, mass*fac));
        universe.space.addMass(new DebugMatter(-0.5, -0.1, 250, 225, mass*fac));
        universe.space.addMass(new DebugMatter(0, 0, 300, 500, 2));*/



        /* //Zwei gleiche Massen haben stets die gleichen Vectoren, auch wenn ein vector einer Masse 0 ist, so ist der andere ebenfalls 0
        universe.space.addMass(new DebugMatter(0.5, 0, 250, 285, mass*fac));
        universe.space.addMass(new DebugMatter(-0.5, 0, 250, 225, mass*fac));
        universe.space.addMass(new DebugMatter(0, 0, 300, 500, 2));
        */


        //universe.space.addMass(new Matter(0, 0, 0, 10, mass));
        //universe.space.addMass(new Mass(0, 10, mass));


        //Wenn gleiche 4 Massen umeinander rotieren, so ist die Summe der X/Y Vectoren immer gleich.
        //Dabei ist der X Vector 0 wenn die Masse an den horizontalen Extrempunkten des virtuellen Kreises positioniert ist.
        //Dabei ist der Y Vector 0 wenn die Masse an den vertikalen Extrempunkten des virtuellen Kreises positioniert ist.
        int factor = 90;
        universe.space.addMass(new DebugMatter(-0.35 * factor, 0, 100, 120, mass));
        universe.space.addMass(new DebugMatter(0.35 * factor, 0, 100, 80, mass));
        universe.space.addMass(new DebugMatter(0, -0.35 * factor, 80, 100, mass));
        universe.space.addMass(new DebugMatter(0, 0.35 * factor, 120, 100, mass));


        /*//Hier herscht gleichgewicht
        universe.space.addMass(new Mass(150, 250, mass));
        universe.space.addMass(new Mass(150, 350, mass));
        universe.space.addMass(new DebugMatter(200, 300, mass));
        universe.space.addMass(new Mass(250, 250, mass));
        universe.space.addMass(new Mass(250, 350, mass));
        */


        /*
        universe.space.addMass(new DebugMatter(0,-0.1,300, 400, mass));
        universe.space.addMass(new DebugMatter(0,0.1,300, 200, mass));
        universe.space.addMass(new DebugMatter(0,0.1,200, 300, mass));
        universe.space.addMass(new DebugMatter(0,-0.1,400, 300, mass));
        universe.space.addMass(new DebugMatter(0, 0, 300, 300, 2));
        */

        /*for (int i = 0; i <= nBodies; i++) {

            x = ThreadLocalRandom.current().nextInt(min, max + 1);
            y = ThreadLocalRandom.current().nextInt(min, max + 1);
            mass = ThreadLocalRandom.current().nextFloat();
            universe.space.bodies.addElement(new Matter(x, y, 1));
        }*/
    }
}
