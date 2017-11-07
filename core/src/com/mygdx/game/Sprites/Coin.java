package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
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
 * Created by mob on 03/11/17.
 */

public class Coin extends CoinAbstract {
    private Hud hud;
    private float coinTime;
    private Animation spinAnimation;
    private Array<TextureRegion> frames;

    private boolean setCoinToDestroy;
    public boolean coinDestroyed;

    public Coin(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();

            for (int i = 1; i < 7; i++) {
                if (i == 0)
                    //frames.add(new TextureRegion(screen.getAtlas().findRegion("coin"),))
                    if (i == 1)
                        frames.add(new TextureRegion(screen.getAtlasCoin().findRegion("coin"), 16, 20, 98, 92));
                if (i == 2)
                    frames.add(new TextureRegion(screen.getAtlasCoin().findRegion("coin"), 116, 16, 98, 97));
                if (i == 3)
                    frames.add(new TextureRegion(screen.getAtlasCoin().findRegion("coin"), 215, 18, 90, 95));
                if (i == 4)
                    frames.add(new TextureRegion(screen.getAtlasCoin().findRegion("coin"), 318, 18, 78, 95));
                if (i == 5)
                    frames.add(new TextureRegion(screen.getAtlasCoin().findRegion("coin"), 425, 18, 58, 95));
                if (i == 6)
                    frames.add(new TextureRegion(screen.getAtlasCoin().findRegion("coin"), 530, 16, 38, 99));
            }

        spinAnimation = new Animation<TextureRegion>(0.2f, frames);
        coinTime = 0;
        setBounds(getX(), getY(), 12/BananaKong.PPM, 22/BananaKong.PPM);
        setCoinToDestroy = false;
        coinDestroyed = false;
    }

    TextureRegion region;
    public void update(float dt) {
        coinTime+= dt;
        if(setCoinToDestroy && !coinDestroyed) {
            world.destroyBody(b2body);
            coinDestroyed = true;
            coinTime = 0;

        }
        else if(!coinDestroyed) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            // setRegion((Texture) spinAnimation.getKeyFrame(coinTime, true));
            setRegion(getCoin(dt));
        }
    }

    public TextureRegion getCoin(float dt) {
        region = (TextureRegion)spinAnimation.getKeyFrame(coinTime, true);
        return region;
    }
    @Override
    protected void defineCoin() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6.2f/BananaKong.PPM);
        fdef.filter.categoryBits = BananaKong.COIN_BIT;
        fdef.filter.maskBits = BananaKong.GROUND_BIT |  BananaKong.KING_BIT | BananaKong.BLANK_BIT | BananaKong.OBSTACLE_BIT; //bananas will be added later


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
    }

    public void draw(Batch batch) {
        if(!coinDestroyed)
            super.draw(batch);

    }
    @Override
    public void hitCoin() {
        setCoinToDestroy = true;
        hud.score += 25;
        hud.coincount++;
    }
}
