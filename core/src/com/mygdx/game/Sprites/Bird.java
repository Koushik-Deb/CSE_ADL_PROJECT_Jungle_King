package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by mob on 05/11/17.
 */

public class Bird extends BirdAbstract {

    private float birdTime;
    private Animation flyAnimation;
    private Array<TextureRegion> frames;
    private boolean setBirdToDestroy;
    private boolean birdDestroyed;

    public Bird(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i=0;i<3;i++) {
            if(i == 0)
                frames.add(new TextureRegion(screen.getAtlasbird().findRegion("rsz_bird"), 18, 176, 76, 37 ));
            if(i == 1)
                frames.add(new TextureRegion(screen.getAtlasbird().findRegion("rsz_bird"), 125, 176, 68, 30 ));
            if(i == 2)
                frames.add(new TextureRegion(screen.getAtlasbird().findRegion("rsz_bird"), 221, 158, 71, 46 ));
            /*if(i==0)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"),6,15,113,45));
            if(i==1)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"),126,15,113,45));
            if(i==2)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("tiger"),382,15,113,45));*/

        }
        flyAnimation = new Animation<TextureRegion>(0.4f, frames);
        birdTime = 0;
        setBounds(getX(), getY(), 40/BananaKong.PPM, 40/BananaKong.PPM);
        birdDestroyed = false;
        setBirdToDestroy = false;
    }

    TextureRegion region;

    public void update(float dt) {
        birdTime += dt;
        if(setBirdToDestroy && !birdDestroyed) {
            world.destroyBody(b2body);

            birdDestroyed = true;
            birdTime = 0;
        }
        else if(!birdDestroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition((b2body.getPosition().x - getWidth() / 2), b2body.getPosition().y - getHeight() / 2);
            setRegion(getBird(dt));
        }
    }

    public TextureRegion getBird(float dt) {
        region = (TextureRegion)flyAnimation.getKeyFrame(birdTime, true);
        return region;
    }


    @Override
    protected void defineBird() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.KinematicBody;
        b2body = world.createBody(bdef);

        //to create the physical shape of the main character
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10/BananaKong.PPM);
        fdef.filter.categoryBits = BananaKong.BIRD_BIT;
        fdef.filter.maskBits = BananaKong.KING_BIT | BananaKong.GROUND_BIT;   //bananas will be added later


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if(!birdDestroyed)
            super.draw(batch);
    }

    @Override
    public void hitBird() {
        setBirdToDestroy = true;
        Hud.score += 1000;
    }
}
