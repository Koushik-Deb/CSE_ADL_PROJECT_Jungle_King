package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.mygdx.game.GameConstants;

/**
 * Created by inevitable on 10/10/17.
 */

public class GameOverScreen implements Screen {
    private Texture texture;
    private Viewport viewport;
    private Stage stage;
    private boolean isGameOver;
    private int score;
    private Button statsButton, playAgainButton;
    private Skin skin;
    private PlayScreen playScreen;
    private String name;
    private final BananaKong game;

    public GameOverScreen(final BananaKong game) {

        this.game = game;
        PlayScreen.cont = 0;
        PlayScreen.Lives = 5;
        PlayScreen.mainlives = 3;
        PlayScreen.worldno = 0;

        texture = new Texture("GameoverFinal.jpg");
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Gdx.input.setInputProcessor(this.stage);
        skin = new Skin(Gdx.files.internal("skin/ski/uiskin.json"));
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.CYAN);

        // Creating table to hold the TextFields.

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        TextField scoreTextField = new TextField("Your Score", skin);
        setSize(scoreTextField);
        scoreTextField.setDisabled(true);
        table.add(scoreTextField).expandX();
        TextField showScoreTextField = new TextField(Integer.toString(playScreen.getScore()), skin);
        setSize(showScoreTextField);
        showScoreTextField.setDisabled(true);
        table.add(showScoreTextField).expandX().padTop(10f);
        table.row();


        TextField coinsTextField = new TextField("Coins  ", skin);
        setSize(coinsTextField);
        coinsTextField.setDisabled(true);
        table.add(coinsTextField).expandX();
        TextField showCoinsTextField = new TextField(Integer.toString(playScreen.getCoins()), skin);
        setSize(showCoinsTextField);
        showCoinsTextField.setDisabled(true);
        table.add(showCoinsTextField).expandX().padTop(10f);
        table.row();


        //Creates two textField if currentScore is highScore to take name input

        if (playScreen.getScore() >= game.getHighScore()) {
            TextField NameTextField = new TextField("Name  ", skin);
            setSize(NameTextField);
            NameTextField.setDisabled(true);
            table.add(NameTextField).expandX();

            final TextField textField = new TextField("", skin);
            setSize(textField);
            textField.setOnscreenKeyboard(new TextField.OnscreenKeyboard() {
                @Override
                public void show(boolean visible) {
                    Gdx.input.setOnscreenKeyboardVisible(true);
                    Gdx.input.getTextInput(new Input.TextInputListener() {
                        @Override
                        public void input(String text) {
                            textField.setText(text);
                            game.setHighScorer(text);
                            textField.setDisabled(true);
                        }

                        @Override
                        public void canceled() {
                            System.out.println("Cancelled.");
                        }
                    }, "New HighScore", "", "Type Your Name....");
                }
            });
            table.add(textField).expandX().padTop(10f);

        }

        updateFile(playScreen.getScore(), playScreen.getCoins(), playScreen.getDistance());

        statsButton = new TextButton("Statistics", skin);
        statsButton.setSize(GameConstants.col_width * 2, GameConstants.row_height);
        statsButton.setPosition(Gdx.graphics.getWidth() - statsButton.getWidth(), 0);
        statsButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new Statistics(game));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.setSize(GameConstants.col_width * 2, GameConstants.row_height);
        playAgainButton.setPosition(0, 0);
        playAgainButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                game.setScreen(new StoryScreen(game));
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        stage.addActor(table);
        stage.addActor(playAgainButton);
        stage.addActor(statsButton);
    }


    private void setSize(TextField textField) {
        textField.setSize(textField.getPrefWidth()*2, textField.getPrefHeight()*2);
        textField.setAlignment(Align.center);
    }

    //to update statistics
    public void updateFile(int score, int coins, int distance) {
        game.setGameCount();
        game.setTotalDistance(distance);
        game.setTotalCoins(coins);
        game.setHighScore(score);
        game.setMaxCoins(coins);
        game.setMaxDistance(distance);
    }

    public String getName() {
        return name;
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl20.glClearColor(1, 0, 0, 0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        stage.dispose();
    }
}