package com.mygdx.game.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Sprites.Bird;
import com.mygdx.game.Sprites.Coin;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.InteractiveTileobject;
import com.mygdx.game.Sprites.King;
import com.mygdx.game.Sprites.Tiger;
import com.mygdx.game.screens.PlayScreen;


/**
 * Created by koushik on 10/26/17.
 */

public class WorldContactListener implements ContactListener {
    private PlayScreen playScreen;
    private Integer lifecount=0;
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if(fixA.getUserData()=="head" || fixB.getUserData()=="head")
        {
            Fixture head = fixA.getUserData() == "head"? fixA:fixB;
            Fixture object = head == fixA? fixB : fixA;

            if(object.getUserData()!= null && InteractiveTileobject.class.isAssignableFrom(object.getUserData().getClass()))
            {
                ((InteractiveTileobject) object.getUserData()).onHeadHit();
            }
        }

        //to identify which object is which and to decide the collision :3 :)
        switch (cdef) {
            case BananaKong.ENEMY_HEAD_BIT | BananaKong.KING_BIT:
                if (fixA.getFilterData().categoryBits == BananaKong.ENEMY_HEAD_BIT) {
                    ((Enemy) fixA.getUserData()).hitOnHead();
                } else if(fixB.getFilterData().categoryBits == BananaKong.ENEMY_HEAD_BIT)
                {
                    ((Enemy) fixB.getUserData()).hitOnHead();
        }
                break;
            case BananaKong.ENEMY_BIT | BananaKong.OBSTACLE_BIT:
                if(fixA.getFilterData().categoryBits==BananaKong.ENEMY_BIT) {
                    Tiger.flipping = true;
                    Tiger.walkflipping = true;

                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    Tiger.walkflipping = true;
                    Tiger.flipping = true;

                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;
            case BananaKong.KING_BIT | BananaKong.ENEMY_BIT:
                    if (fixA.getFilterData().categoryBits == BananaKong.KING_BIT) {
                        if(playScreen.mainlives<=1)
                            ((Enemy) fixB.getUserData()).eat();
                        ((King) fixA.getUserData()).hit();
                        System.out.println("King");
                    } else if (fixB.getFilterData().categoryBits == BananaKong.KING_BIT) {
                        System.out.println("King1");
                        ((King) fixB.getUserData()).hit();
                        if(playScreen.mainlives<=1)
                        ((Enemy) fixA.getUserData()).eat();
                    }
                                BananaKong.manager.get("audio/punch.mp3", Sound.class).play();

                    lifecount++;
                    if(lifecount%4==0)
                    {
                        playScreen.Lives--;
                    }
              //  }

                break;
            case BananaKong.ENEMY_BIT | BananaKong.ENEMY_BIT:
                ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case BananaKong.ENEMY_BIT | BananaKong.BLANK_BIT:

                if(fixA.getFilterData().categoryBits==BananaKong.ENEMY_BIT) {

                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                }
                else {
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                }
                break;

            case BananaKong.KING_BIT | BananaKong.COIN_BIT:
                if(fixA.getFilterData().categoryBits == BananaKong.COIN_BIT)
                    ((Coin)fixA.getUserData()).hitCoin();
                else if(fixB.getFilterData().categoryBits == BananaKong.COIN_BIT)
                    ((Coin)fixB.getUserData()).hitCoin();
                break;

            case BananaKong.KING_BIT | BananaKong.BIRD_BIT:
                if(fixA.getFilterData().categoryBits == BananaKong.BIRD_BIT)
                    ((Bird)fixA.getUserData()).hitBird();
                else if(fixB.getFilterData().categoryBits == BananaKong.BIRD_BIT)
                    ((Bird)fixB.getUserData()).hitBird();
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
