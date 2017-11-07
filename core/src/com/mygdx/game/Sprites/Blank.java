package com.mygdx.game.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.BananaKong;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by koushik on 10/28/17.
 */

public class Blank extends InteractiveTileobject {
    public Blank(PlayScreen screen, Rectangle bounds) {
        super(screen, bounds);
        fixture.setUserData(this);
        setCatagoryFilter(BananaKong.BLANK_BIT);
    }

    @Override
    public void onHeadHit() {

    }
}
