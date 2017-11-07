package com.mygdx.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BananaKong;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by koushik on 10/26/17.
 */

public class Ground extends InteractiveTileobject {
    public Ground(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCatagoryFilter(BananaKong.GROUND_BIT);
    }

    @Override
    public void onHeadHit() {
        Gdx.app.log("Ground", "collision");
    }
}
