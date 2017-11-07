package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BananaKong;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by mob on 05/11/17.
 */

public class Partner extends PartnerAbstract {

    private float partnerTime;
    private Animation standAnimation;
    private Array<TextureRegion> frames;
    private boolean setPartnerToDestroy;
    private boolean partnerDestroyed;

    public Partner(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

            for (int i = 1; i < 3; i++) {
                if (i == 1)
                    frames.add(new TextureRegion(screen.getAtlaspartner().findRegion("rsz_ladymonkey"), 0, 0, 300, 285));
                if (i == 2)
                    frames.add(new TextureRegion(screen.getAtlaspartner().findRegion("rsz_ladymonkey"), 305, 0, 304, 287));
            }


        standAnimation = new Animation<TextureRegion>(0.4f, frames);
        partnerTime = 0;
        setBounds(getX(), getY(), 40/BananaKong.PPM, 40/BananaKong.PPM);
        partnerDestroyed = false;
        setPartnerToDestroy = false;
    }

    TextureRegion region;

    public void update(float dt) {
        partnerTime += dt;



            setPosition((b2body.getPosition().x - getWidth() / 2), b2body.getPosition().y - getHeight() / 2);
            setRegion(getBird(dt));

    }

    public TextureRegion getBird(float dt) {
        region = (TextureRegion)standAnimation.getKeyFrame(partnerTime, true);
        return region;
    }


    @Override
    public void definePartner() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        //to create the physical shape of the main character
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10/BananaKong.PPM);
        fdef.filter.categoryBits = BananaKong.Nothing_Bit;
           //bananas will be added later


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {

            super.draw(batch);
    }


    /*public void hitBird() {
        setBirdToDestroy = true;
    }*/
}
