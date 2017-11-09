package com.company;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class God {

    public static void main(String[] args) {

        try {

            AppGameContainer app = new AppGameContainer(new Game("Particle simulation"));

            app.setDisplayMode(1024, 768, false);
            app.setTargetFrameRate(60);
            app.setShowFPS(true);
            app.setVSync(false);
            app.start();


        } catch (SlickException e) {
            e.printStackTrace();
        }
    }
}
