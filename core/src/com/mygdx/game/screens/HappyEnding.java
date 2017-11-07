package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.mygdx.game.BananaKong;

/**
 * Created by koushik on 11/7/17.
 */

public class HappyEnding implements Screen {
    private final BananaKong game;
    private Skin myskin;
    private Stage stage;
    private Texture texture;
    private float timer=0;


    //creating the ending screen
    //the begining of the end
    public HappyEnding(final BananaKong game) {
        this.game = game;
        stage = new Stage(game.screenPort);
        texture = new Texture("happyendingmain.jpg");
        myskin = new Skin(Gdx.files.internal("skin/gold/golden-ui-skin.json"));
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        timer+=delta;
        //to show for 3 sec and then game over screen will pop up
        if(timer>3.5f)
        {
            texture.dispose();
            game.setScreen(new GameOverScreen(game));
        }
        Gdx.gl20.glClearColor(1,0,0,0);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.end();
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
