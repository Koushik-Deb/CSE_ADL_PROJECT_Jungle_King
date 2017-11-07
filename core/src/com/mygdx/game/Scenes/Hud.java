package com.mygdx.game.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.BananaKong;
import com.mygdx.game.GameConstants;
import com.mygdx.game.screens.PlayScreen;

/**
 * Created by koushik on 10/9/17.
 */

public class Hud implements Disposable{

    private BananaKong game;

    //Scene@D.ui stage and own viewport for hud
    public Stage stage;
    private Viewport viewport;

    //score trackers
    public static Integer WorldTimer;
    private float timeCount;
    public static Integer score=0,coincount=0,booster=0;


    //widgets
    Label countdownLabel;
    Label scoreLabel;
    Label timeLabel;
    Label labelLabel;
    Label worldLabel;
    Label KongLabel;
    Label Boost;
    Label Booster;



    public Hud(SpriteBatch sb)
    {
        //initializing
        WorldTimer = 100;
        timeCount = 0;
        score = 0;

        //setting up hud and stages
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),new OrthographicCamera());
        stage = new Stage(viewport,sb);

        //defining a table to see the scores and all
        Table table = new Table();
        //aligning at the top
        table.top();
        //filling the entire stage
        table.setFillParent(true);

        //defining labels with strings,label style ,fonts etc
        countdownLabel = new Label(String.format("%03d",WorldTimer),new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        scoreLabel = new Label(String.format("%06d", PlayScreen.mainlives),new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        timeLabel = new Label("Time",new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        labelLabel = new Label(String.format("%06d", score),new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        //labelLabel = new Label("Level 1-1",new Label.LabelStyle(new BitmapFont(), Color.CYAN));
        worldLabel = new Label("Score",new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        KongLabel= new Label("Lives",new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        Boost = new Label("Boost",new Label.LabelStyle(new BitmapFont(), Color.NAVY));
        Booster = new Label(String.format("%02d", booster),new Label.LabelStyle(new BitmapFont(), Color.NAVY));

        //adding labels to table and giving equal spaces
        table.add(worldLabel).expandX().padTop(20);
        table.add(KongLabel).expandX().padTop(20);
        table.add(timeLabel).expandX().padTop(20);

        //adding another row
        table.row();
        table.add(labelLabel).expandX();
        table.add(scoreLabel).expandX();
        table.add(countdownLabel).expandX();

        table.row();
        table.add(Boost);
        table.add(Booster);

        //add table to the stage
        stage.addActor(table);
    }

    public void update(float delta)
    {
        scoreLabel.setText(String.format("%03d",PlayScreen.mainlives));
        timeCount +=delta;
        if(timeCount>=1)
        {
            WorldTimer--;
            booster++;
            countdownLabel.setText(String.format("%03d",WorldTimer));
            if(booster<=8)
            Booster.setText(String.format("%02d",booster));
            timeCount = 0;
        }
        labelLabel.setText(String.format("%07d",score));
    }

    public void dispose()
    {
        stage.dispose();
    }
}
