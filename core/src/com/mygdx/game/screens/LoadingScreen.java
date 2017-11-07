package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.BananaKong;

/**
 * Created by koushik on 10/9/17.
 */

public class LoadingScreen implements Screen {

    final BananaKong game;
    private ShapeRenderer shapeRenderer;
    private float progress;
    private Texture texture;

    public LoadingScreen(final BananaKong game)
    {
        this.game = game;
        shapeRenderer = new ShapeRenderer();
        progress = 0;
        texture = new Texture("load.jpg");
        //loading image for the next screen
        queueAsset();
    }

    private void queueAsset(){
        game.asset.load("splash.png", Texture.class);
    }

    @Override
    public void show() {
        System.out.println("ok,let's load");

    }

    private void update(float delta)
    {
        progress = MathUtils.lerp(progress,game.asset.getProgress(),.05f);
        if(game.asset.update()  && progress >= game.asset.getProgress()-.01f)
        {
            game.gotoSplashScreen();
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl20.glClearColor(1f,1f,1f,1f);
        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(texture,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        game.batch.end();

        update(delta);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      /*  shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(32,32,game.WIDTH - 70, 16);*/
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(32,32,progress*(Gdx.graphics.getWidth()-64) , 16);

        shapeRenderer.end();
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
        shapeRenderer.dispose();
    }
}
