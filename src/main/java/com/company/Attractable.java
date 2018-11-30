package com.company;

public interface Attractable extends Locatable {

    void calculateForce(Attractable body, double timePassed);

    double getMass();
}
