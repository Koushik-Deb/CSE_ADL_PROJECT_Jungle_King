package com.mygdx.game.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPackerIO;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Control.Controller;
import com.mygdx.game.GameConstants;

/**
 * Created by koushik on 10/6/17.
 */

public class MenuScreen implements Screen {

    final BananaKong game;
    private Skin myskin;
    private Stage stage;
    private Button btn;
    private Texture bg;
    public BitmapFont font80;

    public MenuScreen(final BananaKong game)
    {
        this.game = game;
        stage = new Stage(game.screenPort);
        bg = new Texture("bg.png");
        myskin = new Skin(Gdx.files.internal("skin/gold/golden-ui-skin.json"));
        Gdx.input.setInputProcessor(stage);

        //font changing
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/myfont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 80;
        params.color = Color.FIREBRICK;
        params.shadowColor = Color.DARK_GRAY;
        params.shadowOffsetY = 5;
        font80 = generator.generateFont(params);
        generator.dispose();

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font80;


        //Title set for MenuScreen
        Label gameTitle = new Label("Game Menu", labelStyle);
        gameTitle.setSize(GameConstants.col_width*2,GameConstants.row_height*2);
        gameTitle.setPosition(GameConstants.centerX - gameTitle.getWidth()/2,GameConstants.centerY + GameConstants.centerY/2);
        gameTitle.setAlignment(Align.center);

        //BUTTON
        final Button stBtn = new TextButton("Start Game", myskin , "default");
        stBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        stBtn.setPosition(GameConstants.centerX - stBtn.getWidth()/2,gameTitle.getY() - stBtn.getHeight());
        stBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                game.setScreen(new StoryScreen(game));

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
//previously option button was settings button and stng = setting was then,,,next,it was modified
        Button optnBtn = new TextButton("Options", myskin , "default");
        optnBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        optnBtn.setPosition(GameConstants.centerX - optnBtn.getWidth()/2,stBtn.getY() - optnBtn.getHeight());
        optnBtn.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                game.gotoSettingsScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        Button exitBtn = new TextButton("Exit", myskin , "default");
        exitBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        exitBtn.setPosition(GameConstants.centerX - exitBtn.getWidth()/2,optnBtn.getY() - exitBtn.getHeight());
        exitBtn.addListener(new InputListener(){

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(gameTitle);
        stage.addActor(stBtn);
        stage.addActor(optnBtn);
        stage.addActor(exitBtn);

    }


    @Override
    public void show() {

    }


    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1,0,0,0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(bg,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        game.screenPort.update(width,height);
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
        game.batch.dispose();
        myskin.dispose();
        stage.dispose();

    }
}