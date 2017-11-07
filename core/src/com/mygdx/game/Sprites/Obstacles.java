package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.BananaKong;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by koushik on 10/26/17.
 */

public class Obstacles extends InteractiveTileobject {
    public Obstacles(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCatagoryFilter(BananaKong.OBSTACLE_BIT);
    }

    @Override
    public void onHeadHit() {
    }
}
