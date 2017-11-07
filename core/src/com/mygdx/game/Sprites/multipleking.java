package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.game.BananaKong;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by koushik on 11/4/17.
 */

public class multipleking extends Sprite {
    Vector2 previousPosition;
    private TextureRegion mkingstand;
    private World world;

    public multipleking(PlayScreen screen)
    {
        super(screen.getAtlas().findRegion("fox"));
        this.world = screen.getWorld();

        mkingstand = new TextureRegion(getTexture(),68,92,57,33);
        if(!mkingstand.isFlipX())
        {
            mkingstand.flip(true,false);
        }
        setBounds(32/BananaKong.PPM,32/BananaKong.PPM,40/BananaKong.PPM,40/BananaKong.PPM);
        setRegion(mkingstand);
        previousPosition = new Vector2(getX(),getY());
    }
    public boolean hasMoved(){
        if(previousPosition.x != getX() || previousPosition.y!=getY())
        {
            previousPosition.x = getX();
            previousPosition.y = getY();
            return true;
        }
        return false;
    }

    public void update(float delta)
    {

    }
}
