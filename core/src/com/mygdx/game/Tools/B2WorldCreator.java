package com.mygdx.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
//import com.mygdx.game.Sprites.Bananas;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Sprites.Blank;
import com.mygdx.game.Sprites.Coin;
//import com.mygdx.game.Sprites.Fox;
import com.mygdx.game.Sprites.Ground;
import com.mygdx.game.Sprites.Obstacles;
import com.mygdx.game.Sprites.Tiger;
import com.mygdx.game.screens.PlayScreen;
//import com.mygdx.game.Sprites.Obstacle;

/**
 * Created by koushik on 10/26/17.
 */

public class B2WorldCreator {
    private Array<Tiger> tigers;
    //private Array<Fox> fox;
    private  Array<Coin> coins;

    public B2WorldCreator(PlayScreen screen) {

        World world = screen.getWorld();
        TiledMap map = screen.getMap();

        //creating body and fixture variables
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //colliding with ground
        for(MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            //rectangle shape collider around the ground
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Ground(screen, rect);
        }
        //colliding with obstacles
        for(MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Obstacles(screen, rect);
        }
        //for blank
        for(MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            //rectangle shape collider around the ground
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Blank(screen, rect);
        }
        //creating enemy
        tigers = new Array<Tiger>();

        for(MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            tigers.add(new Tiger(screen,rect.getX()/BananaKong.PPM,rect.getY()/BananaKong.PPM));
        }
        //creating coins
        coins = new Array<Coin>();
        for(MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            coins.add(new Coin(screen,rect.getX()/BananaKong.PPM,rect.getY()/BananaKong.PPM));
        }

        }

    public Array<Tiger> getTigers() {
        return tigers;
    }
    public Array<Coin> getCoins() {
        return coins;
    }
}
