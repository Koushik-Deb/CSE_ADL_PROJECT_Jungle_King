package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Control.Controller;
import com.mygdx.game.GameConstants;

/**
 * Created by koushik on 10/7/17.
 */

public class SettingsScreen implements Screen {

    final BananaKong game;
    private Controller controller;
    private PlayScreen playScreen;
    private Texture texture,cred;
    private Skin myskin;
    private Stage stage;
    private Label gameTitle;
    private Button btn;
    private MenuScreen ms;
    private BitmapFont fontstng;
    private Button LvlBtn,BackBtn,CreditBtn,HTPBtn,musicBtn,musiconBtn,soundBtn,soundonBtn;
    public static Integer Sounding = 0;
    public SettingsScreen(final BananaKong game)

    {
        this.game = game;
        texture = new Texture("settingbackground.jpg");
        stage = new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);
        myskin = new Skin(Gdx.files.internal("skin/gold/golden-ui-skin.json"));

        //Title set for SettingsScreen
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/stngfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 80;
        params.color = Color.GREEN;
        params.shadowColor = Color.DARK_GRAY;
        params.shadowOffsetY = 5;
        fontstng = generator.generateFont(params);
        generator.dispose();
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = fontstng;

        //Why does public ms.font80 don't work ??


        //Button and Title

        //Title
        gameTitle = new Label("Settings", labelStyle);
        gameTitle.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,GameConstants.centerY + GameConstants.centerY/4);
        gameTitle.setAlignment(Align.center);

        addButton();

        stage_create();

    }
    public void stage_create()
    {
        //System.out.println(game.setMusic);
        stage.addActor(gameTitle);
        if(Sounding==0)
        stage.addActor(soundBtn);
        else
            stage.addActor(soundonBtn);
        if(game.setMusic) {
            //System.out.println("should be music on");
            stage.addActor(musicBtn);
        }
        else {
            //System.out.println("should be music off");
            stage.addActor(musiconBtn);
        }
            stage.addActor(LvlBtn);
        stage.addActor(CreditBtn);
        stage.addActor(BackBtn);
        stage.addActor(HTPBtn);
    }
    @Override
    public void show() {

    }
    private void addButton()
    {

        //Sound BUTTON
        soundBtn = new TextButton("Sound: On", myskin , "default");
        soundBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        soundBtn.setPosition(GameConstants.centerX - soundBtn.getWidth()/2,gameTitle.getY() - soundBtn.getHeight());
        soundBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //game.gotoGameScreen();
                Sounding=1;
                stage_create();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        soundonBtn = new TextButton("Sound: Off", myskin , "default");
        soundonBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        soundonBtn.setPosition(GameConstants.centerX - soundBtn.getWidth()/2,gameTitle.getY() - soundBtn.getHeight());
        soundonBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //game.gotoGameScreen();
                Sounding=0;
                stage_create();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });


        //Music Button
        musicBtn = new TextButton("Music: on", myskin , "default");
        musicBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        musicBtn.setPosition(GameConstants.centerX - musicBtn.getWidth()/2,soundBtn.getY() - musicBtn.getHeight());
        musicBtn.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // game.gotoSettingsScreen();
                game.setMusic = false;
                stage_create();
                game.pauseMusic();

                return true;
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        musiconBtn = new TextButton("Music: off", myskin , "default");
        musiconBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        musiconBtn.setPosition(GameConstants.centerX - musiconBtn.getWidth()/2,soundBtn.getY() - musiconBtn.getHeight());
        musiconBtn.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // game.gotoSettingsScreen();
                game.setMusic = true;
                stage_create();
                game.playMusic();

                return true;
            }


            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        //Level Button
        LvlBtn = new TextButton("High-Scores", myskin , "default");
        LvlBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        LvlBtn.setPosition(GameConstants.centerX - LvlBtn.getWidth()/2,musicBtn.getY() -LvlBtn.getHeight());
        LvlBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                game.setScreen(new Statistics(game));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //Credit Button
        CreditBtn = new TextButton("Credit", myskin , "default");
        CreditBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        CreditBtn.setPosition(GameConstants.centerX - CreditBtn.getWidth()/2,LvlBtn.getY() - CreditBtn.getHeight());
        CreditBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                game.gotoCreditScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //Back Button
        BackBtn = new TextButton("Back", myskin , "default");
        BackBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        BackBtn.setPosition(0,0);
        BackBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                game.gotoMenuScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        //How to play Button
        HTPBtn = new TextButton("How To Play", myskin , "default");
        HTPBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        HTPBtn.setPosition(GameConstants.screenWidth-HTPBtn.getWidth(),0);
        HTPBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                dispose();
                game.gotoHowToPlayScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1,0,0,0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.end();
        stage.act();
        addButton();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width, height);
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
