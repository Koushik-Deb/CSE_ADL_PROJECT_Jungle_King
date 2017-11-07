package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by mob on 03/11/17.
 */

public abstract class CoinAbstract extends Sprite {

    protected World world;
    protected PlayScreen screen;
    public Body b2body;

    public CoinAbstract(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x, y);
        defineCoin();
    }

    protected abstract void defineCoin();
    public abstract void update(float dt);
    public abstract void hitCoin();
}
