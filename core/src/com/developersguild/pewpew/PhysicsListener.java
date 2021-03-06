package com.developersguild.pewpew;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.systems.PlayerSystem;
import com.developersguild.pewpew.systems.StructureSystem;

/**
 * Created by Vihan on 1/30/2016.
 */
public class PhysicsListener implements ContactListener, EntityListener {
    /**
     * Called when two fixtures begin to touch.
     *
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log(this.getClass().getSimpleName(), "beginContact() called");
    }

    /**
     * Called when two fixtures cease to touch.
     *
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
        Gdx.app.log(this.getClass().getSimpleName(), "endContact() called");

    }

    /**
     * Called every time step while the fixtures are overlapping
     * Gives you a chance to alter the contact before it is processed by the collision response
     *
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //Gdx.app.log(this.getClass().getSimpleName(), "preSolve() called");
    }

    /**
     * Called every time step while the fixtures are overlapping
     * Gives you a chance to find out what impulses were caused by the collision response after it has been applied
     *
     * @param contact
     * @param impulse
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //Gdx.app.log(this.getClass().getSimpleName(), "postSolve() called");

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        // If a is a player
        if (a.getUserData().getClass() == PlayerSystem.class) {
            // If b is a structure
            if (b.getUserData().getClass() == StructureSystem.class) {
                a.setUserData(BodyComponent.PLAYER_STRUCTURE_COLLISION);
            }
        }

        // If b is a player
        if (b.getUserData().getClass() == PlayerSystem.class) {
            // If a is a structure
            if (a.getUserData().getClass() == StructureSystem.class) {
                b.setUserData(BodyComponent.PLAYER_STRUCTURE_COLLISION);
            }
        }
    }

    /**
     * Called whenever an {@link Entity} is added to {@link Engine} or a specific {@link Family} See
     * {@link Engine#addEntityListener(EntityListener)} and {@link Engine#addEntityListener(Family, EntityListener)}
     *
     * @param entity
     */
    @Override
    public void entityAdded(Entity entity) {
        Gdx.app.log(this.getClass().getSimpleName(), "entityAdded() called");
    }

    /**
     * Called whenever an {@link Entity} is removed from {@link Engine} or a specific {@link Family} See
     * {@link Engine#addEntityListener(EntityListener)} and {@link Engine#addEntityListener(Family, EntityListener)}
     *
     * @param entity
     */
    @Override
    public void entityRemoved(Entity entity) {
        Gdx.app.log(this.getClass().getSimpleName(), "entityRemoved() called");
    }
}
