/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.platformQuest.Entities;

import br.com.platformQuest.Entities.Box2DEntity;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends Box2DEntity{

    public Player(float x, float y, World world, Texture tex) {
        super(x, y, world, tex);
        this.bodyDef = new BodyDef();
        this.bodyDef.type = BodyDef.BodyType.DynamicBody;
        this.bodyDef.position.set(x,y);
        createBody();
        createFixture();
    }

    @Override
    protected void createBody() {
        this.body = this.world.createBody(bodyDef);
    }

    @Override
    protected void createFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 1);  // Defina o tamanho do corpo

        // Definindo as propriedades do fixture
        this.fixture = new FixtureDef();
        fixture.shape = shape;
        fixture.density = 0.5f;
        fixture.friction = 0.4f;
        fixture.restitution = 0.1f;

        // Criando o fixture e aplicando no corpo
        body.createFixture(fixture);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(texture, body.getPosition().x * Constants.PPM, body.getPosition().y * Constants.PPM);
    }

    public void dispose() {
        texture.dispose();
    }

    public void jump() {
        // Adiciona um impulso vertical (pular)
        Vector2 velocity = body.getLinearVelocity();
        if (Math.abs(velocity.y) < 0.01f) { // Verifica se está no chão (sem velocidade vertical)
            body.applyLinearImpulse(new Vector2(0, 25f), body.getWorldCenter(), true);
        }
    }

    public void moveLeft() {
        // Aplica uma força para mover à esquerda
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x > -3) { // Limita a velocidade para -2 unidades/s
            body.applyLinearImpulse(new Vector2(-1f, 0), body.getWorldCenter(), true);
        }
        if (body.getPosition().x <= 0.1) {
            // Reduz a velocidade (desacelera)
            body.setLinearVelocity(new Vector2(Math.max(velocity.x, 0), velocity.y));
        }
    }

    public void moveRight() {
        // Aplica uma força para mover à direita
        Vector2 velocity = body.getLinearVelocity();
        if (velocity.x < 3) { // Limita a velocidade para 2 unidades/s
            body.applyLinearImpulse(new Vector2(1f, 0), body.getWorldCenter(), true);
        }
        if (body.getPosition().x >= 21) {
            // Reduz a velocidade (desacelera)
            body.setLinearVelocity(new Vector2(Math.min(velocity.x, 0), velocity.y));
        }
    }
}