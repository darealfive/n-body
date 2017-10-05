package com.company;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class God {

    public static void main(String[] args) {

        try {

            AppGameContainer app = new AppGameContainer(new Game("Particle simulation"));

            int dimension = (int) Math.pow(2, 9);
            dimension = 768;

            app.setDisplayMode(dimension, dimension, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(false);
            app.setVSync(true);
            app.start();


        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
