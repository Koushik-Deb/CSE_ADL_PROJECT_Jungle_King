package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.BananaKong;
import com.mygdx.game.GameConstants;
import com.sun.java_cup.internal.runtime.Scanner;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

/**
 * Created by koushik on 10/9/17.
 */

public class SplashScreen implements Screen {

    final BananaKong game;
    private Stage stage;

    //declaring variables
    private Image splash;
    private Skin myskin;
    private Button BackBtn;
    private float count;
    private int ani = 0;
    public SplashScreen(final BananaKong game)
    {
        this.game = game;
        this.stage = new Stage(new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
        count = 0;

        Gdx.input.setInputProcessor(stage);
        myskin = new Skin(Gdx.files.internal("skin/gold/golden-ui-skin.json"));

        splash = new Image(game.asset.get("splash.png", Texture.class));
        splash.setPosition(stage.getWidth()/2-72,stage.getHeight()/2 + 52);

        //animation of image
        splash.addAction(sequence(alpha(0f),scaleTo(.1f,.1f),
                parallel(fadeIn(2f, Interpolation.pow2),scaleTo(2f,2f,2.5f),
                        moveTo(stage.getWidth()/2-72,stage.getHeight()/2 - 52,2f,Interpolation.swing)),
                delay(1.5f)));

        stage.addActor(splash);
        BtnInt();
    }

    //button initialization
    private void BtnInt()
    {
        BackBtn = new TextButton("Go", myskin , "default");
        BackBtn.setSize(GameConstants.col_width*2,GameConstants.row_height );
        BackBtn.setPosition(0,0);
        BackBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.gotoMenuScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }


    @Override
    public void show() {
        System.out.println("show");

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1f,0f,0f,1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);
        stage.draw();
    }

    public void update(float delta)
    {
        stage.act();

        count += delta;
        if(count>=3.5f)
        {
            addBtn();
        }
    }

    public void addBtn()
    {
        stage.addActor(BackBtn);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
