package com.mygdx.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.screens.PlayScreen;
import com.mygdx.game.screens.SettingsScreen;

/**
 * Created by koushik on 11/5/17.
 */

public class Fox extends Enemy {
    private King king;
    private Hud hud;

    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion foxdead, foxeating;
    private boolean setToDestroy, eating;
    private boolean destroyed;

    public Fox(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        setToDestroy = false;
        eating = false;
        destroyed = false;
        for (int i = 1; i <= 4; i++) {
            if (i == 1)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"), 6, 15, 113, 45));
            if (i == 2)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"), 126, 15, 113, 45));
            if (i == 3)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"), 382, 15, 113, 45));
            if (i == 4)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"), 625, 15, 113, 45));
        }
        walkAnimation = new Animation<TextureRegion>(0.08f, frames);
        frames.clear();
        stateTime = 0;
        setBounds(getX(), getY(), 40 / BananaKong.PPM, 40 / BananaKong.PPM);
    }

    TextureRegion region, region1;

    public void update(float dt) {
        stateTime += dt;
        if (eating && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            foxeating = new TextureRegion(screen.getAtlas().findRegion("tiger"), 0, 102, 103, 46);
            if (velocity.x <= 0 && !foxeating.isFlipX()) {
                foxeating.flip(true, false);
            }
            if (velocity.x >= 0 && foxeating.isFlipX()) {

                foxeating.flip(true, false);
            }
            setRegion(foxeating);
            setBounds(getX(), getY(), 40 / BananaKong.PPM, 40 / BananaKong.PPM);
            stateTime = 0;
        } else if (setToDestroy && !destroyed) {
            world.destroyBody(b2body);
            destroyed = true;
            foxdead = new TextureRegion(screen.getAtlas().findRegion("tiger"), 205, 296, 140, 36);

            if (velocity.x >= 0 && !foxdead.isFlipX()) {
                foxdead.flip(true, false);
            }
            if (velocity.x <= 0 && foxdead.isFlipX()) {
                System.out.printf("okkkk");
                foxdead.flip(true, false);
            }
            setRegion(foxdead);
            setBounds(getX(), getY() - .16f, 40 / BananaKong.PPM, 40 / BananaKong.PPM);
            stateTime = 0;
            hud.score += 400;
        } else if (!destroyed && !eating) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(gettiger(dt));

        }


    }

    public TextureRegion gettiger(float dt) {
        region = (TextureRegion) walkAnimation.getKeyFrame(stateTime, true);
        if (velocity.x <= 0 && !region.isFlipX()) {
            region.flip(true, false);
        }
        if (velocity.x >= 0 && region.isFlipX()) {
            region.flip(true, false);
        }

        return region;
    }

    @Override
    protected void defineEnemy() {
        //to set the position and create and imaginary body
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        //to create the physical shape of the main character
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(16 / BananaKong.PPM, 20 / BananaKong.PPM);
        fdef.filter.categoryBits = BananaKong.ENEMY_BIT;
        fdef.filter.maskBits = BananaKong.GROUND_BIT | BananaKong.OBSTACLE_BIT | BananaKong.ENEMY_BIT | BananaKong.NULL_BIT | BananaKong.KING_BIT | BananaKong.BLANK_BIT; //bananas will be added later


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //create the head
        EdgeShape tigerup = new EdgeShape();
        tigerup.set(new Vector2(-8 / BananaKong.PPM, 26 / BananaKong.PPM), new Vector2(8 / BananaKong.PPM, 26 / BananaKong.PPM));
        fdef.shape = tigerup;
        fdef.restitution = .5f;
        fdef.filter.categoryBits = BananaKong.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch) {
        if (!destroyed || (destroyed && eating) || stateTime < 1) {
            super.draw(batch);
        }
    }

    @Override
    public void hitOnHead() {
        setToDestroy = true;
        if (SettingsScreen.Sounding == 0)
            BananaKong.manager.get("audio/yahoo.wav", Sound.class).play();
    }

    @Override
    public void eat() {
        eating = true;
        if (SettingsScreen.Sounding == 0)
            BananaKong.manager.get("audio/Tiger6.mp3", Sound.class).play();
    }
}


