package com.mygdx.game.Control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.BananaKong;

/**
 * Created by koushik on 10/15/17.
 */


//To add the controlling button
public class Controller {
    private BananaKong game;

    public Stage stage;
    private Viewport viewport;
    public static boolean isPause = false;
    boolean upPressed, downPressed, leftPressed, rightPressed, boostPressed;
            ;
    OrthographicCamera cam;


    public Controller(SpriteBatch sb){

        cam = new OrthographicCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),cam);
        stage = new Stage(viewport,sb);


        Gdx.input.setInputProcessor(stage);

        //adding table
        Table table = new Table();
        table.right().top();
        table.setFillParent(true);



        //image button
        Image img = new Image(new Texture("pause.png"));
        img.setSize(90,90);
        img.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!isPause)
                    isPause = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        Table tableleft = new Table();
        tableleft.left().bottom();

        Table tableright = new Table();
        tableright.right().bottom();
        tableright.setFillParent(true);
        //aligning at the top


        Image right = new Image(new Texture("right.png"));
        right.setSize(90,90);
        right.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;
            }
        });



        Image left = new Image(new Texture("left.png"));
        left.setSize(90,90);
        left.addListener(new InputListener()
        {


            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                System.out.println(leftPressed);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });

        Image up = new Image(new Texture("up.png"));
        up.setSize(110, 110);
        up.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;
            }
        });

        Image boost = new Image(new Texture("boost.png"));
        boost.setSize(90, 90);
        boost.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                boostPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                boostPressed = false;
            }
        });


        //tableleft.add();
        tableleft.add(left).size(left.getWidth(), left.getHeight());
        table.add();
        tableleft.add(right).size(right.getWidth(),right.getHeight());

        tableright.add(boost).size(boost.getWidth(), boost.getHeight());
        tableright.add();
        tableright.add(up).size(up.getWidth(), up.getHeight());

        table.add(img).size(img.getWidth(),img.getHeight());
        stage.addActor(table);
        stage.addActor(tableleft);
        stage.addActor(tableright);
    }

    public boolean pause()
    {
        return isPause = false;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isBoostPressed() {
        return boostPressed;
    }

    public void resize(int width, int height){
        viewport.update(width, height);
    }



    public void dispose() {stage.dispose();}
}