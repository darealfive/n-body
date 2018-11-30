package de.darealfive;

import org.newdawn.slick.geom.Shape;

interface NodeInterface extends Attractable, Interactable, Showable {
    Shape getShape();
}
