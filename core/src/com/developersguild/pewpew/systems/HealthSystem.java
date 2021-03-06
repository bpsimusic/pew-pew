package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BoundsComponent;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Thomas on 1/23/2016.
 */
public class HealthSystem extends IteratingSystem {
    private static final Family family = Family.all(TransformComponent.class,
            HealthComponent.class).get();

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    private Engine engine;
    private float healthLastFrame;

    public HealthSystem() {
        super(Family.all(TransformComponent.class, HealthComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);

        healthLastFrame = 0f;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = tm.get(entity);
        HealthComponent health = hm.get(entity);

        // Health bar follows target
        //pos.pos.x = health.targetPos.x - ((1 - pos.scale.x) * health.target.getComponent(BoundsComponent.class).bounds.width / 2); -- PINS BAR TO THE LEFT EDGE
        pos.pos.x = health.targetPos.x;
        pos.pos.y = health.targetPos.y - health.target.getComponent(BoundsComponent.class).bounds.height / 2f - 0.1f;

        if (health.target.getComponent(PlayerComponent.class) != null) {
            health.currentHealth = health.target.getComponent(PlayerComponent.class).currentHealth;
            if (health.currentHealth != healthLastFrame) updateHealthBar(entity);
        } else if (health.target.getComponent(StructureComponent.class) != null) {
            health.currentHealth = health.target.getComponent(StructureComponent.class).currentHealth;
            if (health.currentHealth != healthLastFrame) updateHealthBar(entity);
        }

        healthLastFrame = health.currentHealth;
    }

    public void updateHealthBar(Entity entity) {
        if (!family.matches(entity)) return;

        HealthComponent health = hm.get(entity);
        TransformComponent pos = tm.get(entity);

        // Update healthLength
        float healthLength = 0.0f;

        if (health.isPlayer) {
            healthLength = health.currentHealth / health.maxHealth * health.lengthRatio;
        } else if (health.isStructure) {
            healthLength = health.currentHealth / health.maxHealth * health.lengthRatio;
        }

        // Prevent health decreasing below 0
        if (health.currentHealth < 0) {
            health.currentHealth = 0;
            health.target.getComponent(PlayerComponent.class).currentHealth = health.currentHealth;
        }

        // Prevent health increasing over maxHealth
        if (health.currentHealth > health.maxHealth) {
            health.currentHealth = health.maxHealth;
            health.target.getComponent(PlayerComponent.class).currentHealth = health.currentHealth;
        }

        pos.scale.set(healthLength, health.widthRatio);
    }

    public void nextLevel(Entity entity) {
        if (!family.matches(entity)) return;

        HealthComponent health = hm.get(entity);

        health.healthMultiplier += health.LEVEL_INC;
        health.currentLvl++;
    }
}
