package com.company;

import org.newdawn.slick.Graphics;

/**
 * Base class to solve calculation problems in our space-time.
 */
abstract class PhysicsEngine {
    SpaceTime spaceTime;

    /**
     * Runs necessary calculations since last iteration.
     */
    abstract void run();

    /**
     * Shows the contents of its space time.
     *
     * @param g the graphics used to draw the result
     */
    abstract void show(Graphics g);

    PhysicsEngine(SpaceTime spaceTime) {
        this.spaceTime = spaceTime;
    }

    /**
     * Applies all previously calculated data on all objects in space-time.
     * It is the last step before the next calculations can start and it prepares the environment for it.
     *
     * @see PhysicsEngine#run() as a step before where the data gets calculated
     */
    void applyPhysics() {

        for (Mass body : spaceTime.getBodies()) {

            applyPhysics(body);
            body.resetDeltas();
        }
    }

    /**
     * Applies a quantum measurement to concretize the actual location based on actual data of bodies in its space-time.
     * Think of the superposition of an object.
     *
     * @param body the Mass which gets affected by the measurement
     */
    private void applyPhysics(Mass body) {

        updateVelocityX(body);
        updateVelocityY(body);

        updatePosX(body);
        updatePosY(body);
    }

    /**
     * Updates the velocity vector X of given Mass dependent on the space-time its located in.
     *
     * @param body the Mass whose velocity X has to be updated
     */
    private void updateVelocityX(Mass body) {
        body.setVelocityX(determineVelocityX(body));
    }

    /**
     * Updates the velocity vector Y of given Mass dependent on the space-time its located in.
     *
     * @param body the Mass whose velocity Y has to be updated
     */
    private void updateVelocityY(Mass body) {
        body.setVelocityY(determineVelocityY(body));
    }

    /**
     * Updates the position X of given Mass dependent on the space-time its located in.
     *
     * @param body the Mass whose position X has to be updated
     */
    private void updatePosX(Mass body) {

        body.setCenterOfMassPosX(determinePosX(body));
    }

    /**
     * Updates the position Y of given Mass dependent on the space-time its located in.
     *
     * @param body the Mass whose position Y has to be updated
     */
    private void updatePosY(Mass body) {

        body.setCenterOfMassPosY(determinePosY(body));
    }

    /**
     * Determines the position X of given Acceleratable dependent on the space-time its located in.
     *
     * @param body the Acceleratable whose position X has to be updated
     * @return the determined position
     */
    private double determinePosX(Acceleratable body) {

        return determinePosition(body.getCenterOfMassPosX() + (body.getVelocityX() * spaceTime.delta), spaceTime.getWidth());
    }

    /**
     * Determines the position Y of given Acceleratable dependent on the space-time its located in.
     *
     * @param body the Acceleratable whose position Y has to be updated
     * @return the determined position
     */
    private double determinePosY(Acceleratable body) {

        return determinePosition(body.getCenterOfMassPosY() + (body.getVelocityY() * spaceTime.delta), spaceTime.getHeight());
    }

    /**
     * Determines the velocity vector X of given Acceleratable dependent on the space-time its located in.
     *
     * @param body the Acceleratable whose velocity X has to be updated
     */
    private double determineVelocityX(Acceleratable body) {
        return determineVector(body.getCenterOfMassPosX(), body.getVelocityX(), body.getDeltaVelocityX(), spaceTime.getWidth(), spaceTime.stickyAreaDimensionX);
    }

    /**
     * Determines the velocity vector Y of given Acceleratable dependent on the space-time its located in.
     *
     * @param body the Acceleratable whose velocity X has to be updated
     */
    private double determineVelocityY(Acceleratable body) {
        return determineVector(body.getCenterOfMassPosY(), body.getVelocityY(), body.getDeltaVelocityY(), spaceTime.getHeight(), spaceTime.stickyAreaDimensionY);
    }

    private double determineVector(double position, double vector, double offset, double limit, double sticky) {
        // If vector summary points to the opposite direction of the edge of the universe, return the new vector without
        // any changes.
        double vectorSum = vector + offset;

        // The hypothetically position the particle would be if we do not do anything
        double vectorPosition = position + vectorSum * spaceTime.delta;

        //Is vector pointing to the right or bottom edge of the universe?
        if (vectorSum > 0) {

            //The position where the sticky part begins
            double stickyPosition = limit - sticky;

            //Right or bottom edge

            //Calculate the sticky penetration length trough the object
            double stickyPenetrationLength = vectorPosition - stickyPosition;

            //Check whether the new position is in sticky area. Otherwise the vector does not touch the sticky areas
            if (stickyPenetrationLength > 0) {

                vectorSum -= getVectorCorrection(position, stickyPenetrationLength, stickyPosition, sticky);
            }

        } else {

            //The position where the sticky part begins
            double stickyPosition = 0 + sticky;

            //Left or top edge

            //Calculate the sticky penetration length trough the object
            double stickyPenetrationLength = vectorPosition - stickyPosition;

            //Check whether the new position is in sticky area. Otherwise the vector does not touch the sticky areas
            if (stickyPenetrationLength < 0) {

                vectorSum += getVectorCorrection(position, stickyPenetrationLength, stickyPosition, sticky);
            }
        }

        return vectorSum;
    }

    private double getVectorCorrection(double bodyPosition, double stickyPenetrationLength, double stickyPosition, double stickyDimension) {

        double alreadyStickedLength = bodyPosition - stickyPosition;
        stickyPenetrationLength -= alreadyStickedLength;

        double factor = (stickyPenetrationLength % stickyDimension) / stickyDimension;
        //Slow down the vector by the hypothetically sticky area penetration
        double reducedPenetrationLength = stickyPenetrationLength * (1 - factor);

        return (stickyPenetrationLength - reducedPenetrationLength) / spaceTime.delta;
    }

    private double determinePosition(double position, int limited) {

        if (position > limited) {

            position = position % limited;
        } else if (position < 0) {

            position = limited + position;
        }

        return position;
    }
}
