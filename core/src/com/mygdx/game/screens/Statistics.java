package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Control.Controller;
import com.mygdx.game.GameConstants;

/**
 * Created by inevitable on 10/9/17.
 */

public class Statistics implements Screen {
    private final BananaKong game;
    private Controller controller;
    private Texture texture;
    private Skin skin;
    private Viewport viewport;
    private Stage stage;
    private Button button;
    private BitmapFont font;

    //same as high-score screen which is basically game over screen
    public Statistics(final BananaKong game) {
        this.game = game;
        texture = new Texture("highscore.jpg");
        skin = new Skin(Gdx.files.internal("skin/ski/uiskin.json"));
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(stage);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.CYAN);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        TextField highScoreTextField = new TextField("HighScore ",skin);

        table.add(highScoreTextField).expandX();
        Disable(highScoreTextField);
        TextField showHighScoreTextField = new TextField(Integer.toString(game.getHighScore()),skin);
        Disable(showHighScoreTextField);
        table.add(showHighScoreTextField).expandX().padTop(10f);
        table.row();

        TextField coinsTextField = new TextField("Coins  ",skin);
        Disable(coinsTextField);
        table.add(coinsTextField).expandX();
        TextField showCoinsTextField = new TextField(Integer.toString(game.getMaxCoins()),skin);
        Disable(showCoinsTextField);
        table.add(showCoinsTextField).expandX().padTop(10f);
        table.row();

        TextField gameCountTextField = new TextField("Games Played  ",skin);
        Disable(gameCountTextField);
        table.add(gameCountTextField).expandX();
        TextField showGameCountTextField = new TextField(Integer.toString(game.getGameCount()),skin);
        Disable(showGameCountTextField);
        table.add(showGameCountTextField).expandX().padTop(10f);
        table.row();

        TextField highScorerTextField = new TextField("Name ",skin);
        Disable(highScorerTextField);
        table.add(highScorerTextField).expandX();
        TextField showHighScorerTextField = new TextField(game.getHighScorer(),skin);
        Disable(showHighScorerTextField);
        table.add(showHighScorerTextField).expandX().padTop(10f);

        stage.addActor(table);

        Button MenuBtn = new TextButton("Main Menu", skin , "default");
        MenuBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        MenuBtn.setPosition(GameConstants.centerX-MenuBtn.getWidth()/2,0);
        MenuBtn.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PlayScreen.cont = 0;
                PlayScreen.Lives = 5;
                PlayScreen.mainlives = 3;
                PlayScreen.worldno = 0;
                game.gotoMenuScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        stage.addActor(MenuBtn);

    }



    private void Disable(TextField textField) {
        textField.setDisabled(true);
        textField.setAlignment(Align.center);
        textField.setSize(textField.getPrefWidth()*3, textField.getPrefHeight()*3);
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1,0,0,0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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

    }
}