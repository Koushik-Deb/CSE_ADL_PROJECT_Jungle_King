package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.mygdx.game.BananaKong;
import com.mygdx.game.GameConstants;

/**
 * Created by koushik on 11/7/17.
 */

public class StoryScreen implements Screen {
    private final BananaKong game;
    private Skin myskin;
    private Stage stage;
    private Button btn;
    private Texture texture;


    //the begining of everything
    //this is the story which the game is based on
    public StoryScreen(final BananaKong game) {
        this.game = game;
        stage = new Stage(game.screenPort);
        texture = new Texture("storyscreenmain.jpg");
        myskin = new Skin(Gdx.files.internal("skin/gold/golden-ui-skin.json"));
        Gdx.input.setInputProcessor(stage);

        btn = new TextButton("Let's Go!!!", myskin , "default");
        btn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        btn.setPosition(GameConstants.centerX,0);
        btn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                stage.dispose();
                game.gotoPlayScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });
        stage.addActor(btn);
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

        stage.act();
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
