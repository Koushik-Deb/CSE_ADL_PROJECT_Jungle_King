package com.mygdx.game.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.BananaKong;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by mob on 22/10/17.
 */

public class King extends Sprite {
    Vector2 previousPosition;
    public enum State {FALLING,JUMPING,STANDING,RUNNING,DEAD};
    public State currentState,previousState;
    private Animation kingRun,kingJump;
    private TextureRegion KingDead;
    private float stateTimer;
    private boolean runningRight;
    public boolean KingIsDead = false,moving = true,multipleindex=false;

    public World world;
    public Body b2body;
    private TextureRegion kingstand;
    private PlayScreen playScreen;

    public King(Texture multi)
    {
        super(multi);

        previousPosition = new Vector2(getX(),getY());
    }

    public King(PlayScreen screen) {

        //initializing default values
        super(screen.getAtlas().findRegion("chrctr"));
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.JUMPING;
        stateTimer = 0;
        runningRight = true;
        //System.out.println("not");


        Array<TextureRegion> frames = new Array<TextureRegion>();


        //getting run animation frames and adding them to king's animation
        for(int i = 4 ; i <= 7 ; i++)
        {
            if(i==4)
            frames.add(new TextureRegion(getTexture(),224,603,70,100));
            if(i==5)
                frames.add(new TextureRegion(getTexture(),294,603,70,100));
            if(i==6)
                frames.add(new TextureRegion(getTexture(),368,603,70,100));
            if(i==7)
                frames.add(new TextureRegion(getTexture(),443,603,70,100));

        }
        kingRun = new Animation(.08f,frames);
        frames.clear();

        //getting jump animation frames and adding them to king's animation
        for(int i = 1 ; i <=3  ; i++)
        {if(i==1)
            frames.add(new TextureRegion(getTexture(),342,828,80,104));
            if(i==2)
                frames.add(new TextureRegion(getTexture(),428,832,70,100));
            if(i==3)
            frames.add(new TextureRegion(getTexture(),507,830,70,100));
        }
        kingJump = new Animation(.5f,frames);
        frames.clear();

        //creating texture region for king's standing
        kingstand = new TextureRegion(getTexture(),513,471,70,110);



        //defining king in box2D

            defineKing();

        setBounds(64/BananaKong.PPM,64/BananaKong.PPM,40/BananaKong.PPM,40/BananaKong.PPM);
        setRegion(kingstand);
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
        //to update every movement of the king
        if(!KingIsDead && moving) {
            setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y + .05f) - getHeight() / 2);
            setRegion(getFrame(delta));
        }
        else if(KingIsDead && moving && playScreen.mainlives<=1)
        {

            playScreen.mainlives--;
            world.destroyBody(b2body);
            KingIsDead=false;
            moving = false;
            KingDead = new TextureRegion(getTexture(),186,354,80,106);
            setRegion(KingDead);
            setBounds(getX(),getY(),20/BananaKong.PPM,20/BananaKong.PPM);
        }
        else if(KingIsDead && moving && playScreen.mainlives>1) {
        //if the king is dead, then the body will get destroyed
            world.destroyBody(b2body);
            KingIsDead = false;
            playScreen.tigercollision=1;
            setPosition(b2body.getPosition().x - getWidth() / 2, (b2body.getPosition().y + .05f) - getHeight() / 2);
            setRegion(getFrame(delta));
        }
    }

    public TextureRegion getFrame(float dt)
    {
        //getting king's current state(jumping,running ...)
        currentState = getState();
        TextureRegion region;

        //depending on the state, get corresponding animation keyframe
        switch (currentState)
        {
            case JUMPING:
                region = (TextureRegion) kingJump.getKeyFrame(stateTimer,true);
                break;
            case RUNNING:
                region = (TextureRegion) kingRun.getKeyFrame(stateTimer,true);
                break;
            case FALLING:
            case STANDING:
                default:
                    region = kingstand;
                    break;
        }

        //fliping the texture if king is running right and texture facing left
        if((b2body.getLinearVelocity().x<0 || !runningRight) && !region.isFlipX())
        {
            region.flip(true,false);
            runningRight = false;
        }

        //fliping the texture if king is running left and texture facing right
        else if((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX())
        {
            region.flip(true,false);
            runningRight = true;
        }

        //if the current state is the same as the previous state increase the state timer
        //otherwise the state has changed and resetting timer is needed
        stateTimer = currentState == previousState ? stateTimer + dt : 0 ;
        previousState = currentState;
        return region;
    }

    public State getState()
    {
        //test to box2D for the velocity on the x and y axis
        //returning king's position
         if(b2body.getLinearVelocity().y>0 || (b2body.getLinearVelocity().y<0 && previousState == State.JUMPING))
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y<0)
            return State.FALLING;
        else if(b2body.getLinearVelocity().x !=0 )
            return State.RUNNING;
        else return State.STANDING;
    }

    public void defineKing() {

        //to set the position and create and imaginary body
        BodyDef bdef = new BodyDef();
        if(PlayScreen.checkpoint==0){
            PlayScreen.checkpoint=0;
            bdef.position.set(64/ BananaKong.PPM, 64/BananaKong.PPM);

        }
        else if(PlayScreen.checkpoint==1 && PlayScreen.worldno!=2){
            PlayScreen.checkpoint=0;
            bdef.position.set(41,256/BananaKong.PPM);

        }
        else if(PlayScreen.checkpoint==2 && PlayScreen.worldno!=2) {
            PlayScreen.checkpoint=0;
            bdef.position.set((float) 80.9, 256 / BananaKong.PPM);
        }
        else if(PlayScreen.checkpoint==1 && PlayScreen.worldno==2)
        {
            PlayScreen.checkpoint=0;
            bdef.position.set(27.7f,256/BananaKong.PPM);
        }

        else if(PlayScreen.checkpoint==2 && PlayScreen.worldno==2)
        {
            PlayScreen.checkpoint=0;
            bdef.position.set(105f,256/BananaKong.PPM);
        }
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        //to create the physical shape of the main character
        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10/BananaKong.PPM);
        fdef.filter.categoryBits = BananaKong.KING_BIT;
        fdef.filter.maskBits = BananaKong.GROUND_BIT | BananaKong.OBSTACLE_BIT | BananaKong.COIN_BIT | BananaKong.ENEMY_BIT | BananaKong.ENEMY_HEAD_BIT | BananaKong.BIRD_BIT; //bananas will be added later


        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        CircleShape head = new CircleShape();
        head.setRadius(10/BananaKong.PPM);
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void hit()
    {
        KingIsDead = true;
    }
}
