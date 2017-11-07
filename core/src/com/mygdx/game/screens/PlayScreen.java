package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.BananaKong;
import com.mygdx.game.Control.Controller;
import com.mygdx.game.GameConstants;
import com.mygdx.game.Scenes.Hud;
import com.mygdx.game.Sprites.Bird;
import com.mygdx.game.Sprites.CoinAbstract;
import com.mygdx.game.Sprites.Enemy;
import com.mygdx.game.Sprites.King;
import com.mygdx.game.Sprites.Partner;
import com.mygdx.game.Sprites.Tiger;
import com.mygdx.game.Sprites.multipleking;
import com.mygdx.game.Tools.B2WorldCreator;
import com.mygdx.game.Tools.WorldContactListener;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/**
 * Created by mob on 07/10/17.
 */

public class PlayScreen implements Screen {

    //used to set screens
    final BananaKong game;
    private final float UPDATE_TIME = 1/60f;
    float timer;
    private Controller controller;
    private TextureAtlas atlas, atlascoin,atlas1,atlaspartner, atlasbird;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private Hud hud;


    //Game world, tiled map variables
    private TmxMapLoader maploader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Stage stage;
    private Skin myskin;
    private Button playBtn,MusicOnBtn,MenuBtn,MusicOffBtn,soundBtn,soundonBtn;
    public static boolean backtoplay = false;
    private Texture pauseImg;
    private float xyz= 0;  //to count a specific amount of time to draw a screen over others
    private int pausemove = -800,Sounding;
    private float timecount = 0,screencount=0;
    private boolean world1 = false,world2 = false;
    public static int worldno = 0,checkpoint = 0,Lives = 5,cont = 0,mainlives=3;
    public static int tigercollision= 0;


    private static int score;
    private static int distance;
    private static int  coins;



    //box2D variables
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private String id;
    //sprite
    private King player,Mplayer;
    private multipleking mking,mking2;
    private Socket socket;
    private Texture single,multi;
    HashMap<String,multipleking> Oppositions;

    private Bird bird;
    private Partner partner;

    public PlayScreen(final BananaKong game) {
        this.game = game;
        hud.score=0;
        hud.coincount=0;
        hud.booster=0;

//to connect to the server
        connectSocket();
        configSocketEvent();

        //using hashmap to count other players
        Oppositions = new HashMap<String, multipleking>();

        atlas = new TextureAtlas("sheet/kings_and_all.pack");
        atlas1 = new TextureAtlas("sheet/trump_and_wolf.pack");
        atlascoin = new TextureAtlas("Coin.pack");
        atlaspartner = new TextureAtlas("sheet/monkeymulti.pack");
        atlasbird = new TextureAtlas("sheet/bird_rsz.pack");

        //used to follow king through cam world
        gamecam = new OrthographicCamera();

        //Fitviewport to maintain a virtual aspect ratio despite screen size
        gameport = new FitViewport(300/BananaKong.PPM, 352/BananaKong.PPM, gamecam);

        //game hud for scores/times/levels
        hud = new Hud(game.batch);
        controller = new Controller(game.batch);

        //to load the map and map renderer
        maploader = new TmxMapLoader();
        map = maploader.load("GameWorld/gameworld12.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1/BananaKong.PPM);

        //initially set our gamecam to be centered correctly ar the start of the map
        gamecam.position.set(gameport.getWorldWidth()/2, gameport.getWorldHeight()/2, 0);

        pauseImg = new Texture("pauseImg.jpg");

        // to apply gravity to the game world,box2D world and allows bodies to sleep
        world = new World(new Vector2(0,-10), true);

        // objects colliding with the main character
        //allows for debug lines of our box2D world
        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        //create king in game world
        player = new King(this);
        bird = new Bird(this, 120f, 1.64f);
        //partner = new Partner(this, 3f,.64f );

        //tiger = new Tiger(this, 2.56f,.32f);
        world.setContactListener(new WorldContactListener());
    }

    public void connectSocket()
	{
		try {
			socket = IO.socket("http://192.168.122.1:8080");
			socket.connect();
		} catch (Exception e)
		{
			System.out.println(e);
		}
	}

    public void configSocketEvent(){
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIo","Connected");
                singleplayer();
			}
		}).on("socketID", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
					id = data.getString("id");
					Gdx.app.log("SocketIO","My ID: " + id);
				} catch (JSONException e) {
					Gdx.app.log("Socket","Error getting ID");
				}
			}
		}).on("newPlayer", new Emitter.Listener() {
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try {
                    String playerId = data.getString("id");
					Gdx.app.log("SocketIO","New Player Connect: " + id);
                    oppose(playerId);
				} catch (JSONException e) {
					Gdx.app.log("Socket","Error getting New PlayerID");
				}
			}
		}).on("playerDisconnected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    id = data.getString("id");
                   Oppositions.remove(id);
                } catch (JSONException e) {
                    Gdx.app.log("Socket","Error getting Player Disconneced");
                }
            }
        }).on("playerMoved", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try {
                    String playerId = data.getString("id");
                    Double x = data.getDouble("x");
                    Double y = data.getDouble("y");
                    if(Oppositions.get(playerId) != null)
                    {
                        Oppositions.get(playerId).setPosition(x.floatValue(),y.floatValue());
                    }
                } catch (JSONException e) {
                    Gdx.app.log("Socket","Error getting Player Disconneced");
                }
            }
        }).on("getPlayers", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONArray objects = (JSONArray) args[0];
                try {
                    for(int i = 0 ; i<objects.length();i++)
                    {
                        Vector2 position = new Vector2();
                        position.x = ((Double) objects.getJSONObject(i).getDouble("x")).floatValue();

                        position.y = ((Double) objects.getJSONObject(i).getDouble("y")).floatValue();

                        id = objects.getJSONObject(i).getString("id");
                        coop(position.x,position.y,id);
                    }
                }catch(JSONException e)
                {
                    System.out.println(e);
                }
            }
        });

	}
	//create an object for the main player
	public void singleplayer()
    {
        mking = new multipleking(this);
    }


    //to show all others in every one's screen
	public void coop(float x,float y,String id)
    {
        this.id = id;
        multipleking mkingall = new multipleking(this);
        mkingall.setPosition(x,y);
        Oppositions.put(id,mkingall);
    }

    //to show the oppositions
	public void oppose(String id)
    {
        //this.id = id;
        mking2 = new multipleking(this);
        Oppositions.put(id,mking2);
    }

    //world creator
    public void worldcreator()
    {
        map.dispose();
        if(world1 == true && world2 == false)
        {
        map = maploader.load("GameWorld/world2.tmx");

        worldno = 1;
        }
        else {
            map = maploader.load("GameWorld/gameWorldt.tmx");
            worldno = 2;
            partner = new Partner(this, 121.3f,.64f );
        }
        renderer = new OrthogonalTiledMapRenderer(map, 1/BananaKong.PPM);
        world = new World(new Vector2(0,-10), true);

        b2dr = new Box2DDebugRenderer();
        creator = new B2WorldCreator(this);
        //create king in game world
        player = new King(this);

        bird = new Bird(this, 120f, 1.64f);

        world.setContactListener(new WorldContactListener());
    }

    //pause
    public void pausemenu()
    {
        stage =new Stage(game.screenPort);
        Gdx.input.setInputProcessor(stage);
        myskin = new Skin(Gdx.files.internal("skin/gold/golden-ui-skin.json"));

        //Button initialization
        BtnInt();

        if(xyz>=0.5f) {
            stage.addActor(playBtn);
            if(SettingsScreen.Sounding==0)
                stage.addActor(soundBtn);
            else
                stage.addActor(soundonBtn);
            if(game.setMusic)
                stage.addActor(MusicOnBtn);
            else
                stage.addActor(MusicOffBtn);
            stage.addActor(MenuBtn);
        }

    }

    private void BtnInt()
    {

        playBtn = new TextButton("Play", myskin , "default");
        playBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        playBtn.setPosition(GameConstants.screenWidth-playBtn.getWidth(),GameConstants.centerY);
        playBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rmv();
                controller.pause();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        MusicOnBtn = new TextButton("Music: On", myskin , "default");
        MusicOnBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        MusicOnBtn.setPosition(GameConstants.screenWidth-playBtn.getWidth(),playBtn.getY() - MusicOnBtn.getHeight());
        MusicOnBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backtoplay = true;
                game.setMusic = false;
                game.pauseMusic();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        MusicOffBtn = new TextButton("Music: Off", myskin , "default");
        MusicOffBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        MusicOffBtn.setPosition(GameConstants.screenWidth-playBtn.getWidth(),playBtn.getY() - MusicOnBtn.getHeight());
        MusicOffBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                backtoplay = true;
                game.setMusic = true;
                game.playMusic();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        soundBtn = new TextButton("Sound: On", myskin , "default");
        soundBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        soundBtn.setPosition(GameConstants.screenWidth - playBtn.getWidth(),MusicOffBtn.getY() - soundBtn.getHeight());
        soundBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //game.gotoGameScreen();
                SettingsScreen.Sounding=1;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

        soundonBtn = new TextButton("Sound: Off", myskin , "default");
        soundonBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        soundonBtn.setPosition(GameConstants.screenWidth - soundBtn.getWidth(),MusicOffBtn.getY() - soundBtn.getHeight());
        soundonBtn.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //game.gotoGameScreen();
                SettingsScreen.Sounding=0;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });




        MenuBtn = new TextButton("Menu", myskin , "default");
        MenuBtn.setSize(GameConstants.col_width*2,GameConstants.row_height);
        MenuBtn.setPosition(GameConstants.screenWidth-playBtn.getWidth(),soundBtn.getY() - MenuBtn.getHeight());
        MenuBtn.addListener(new InputListener()
        {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                controller.pause();
                cont = 0;
                Lives = 5;
                mainlives = 3;
                worldno = 0;
                game.gotoMenuScreen();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
            }
        });

    }

    public void rmv(){
        stage.dispose();
        controller = new Controller(game.batch);
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }
    public TextureAtlas getAtlasCoin() { return atlascoin; }
    public TextureAtlas getAtlas1() {return atlas1;}
    public TextureAtlas getAtlaspartner(){return atlaspartner;}
    public TextureAtlas getAtlasbird() { return atlasbird; }

    @Override
    public void show() {

    }

    public void handleInput(float dt) {
        //control king by taking input from keyboard
        if (!player.KingIsDead) {
            if ((controller.isRightPressed() || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) && player.b2body.getLinearVelocity().x <= 2) {
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
                if(mking!=null)
                {
                    mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
                }

            }
            if ((controller.isLeftPressed() || Gdx.input.isKeyPressed(Input.Keys.LEFT))&& player.b2body.getLinearVelocity().x >= -2) {
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
                if(mking!=null)
                {
                    mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
                }
            }

            if ((controller.isUpPressed() ||Gdx.input.isKeyJustPressed(Input.Keys.UP)) && player.b2body.getLinearVelocity().y == 0)
            {player.b2body.applyLinearImpulse(new Vector2(0, 5f), player.b2body.getWorldCenter(), true);
                if(mking!=null)
                {
                    mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
                }
            }
            if ((Gdx.input.isKeyJustPressed(Input.Keys.A) || controller.isBoostPressed()) && (player.b2body.getLinearVelocity().y == 0 || player.b2body.getLinearVelocity().y < 0 || player.b2body.getLinearVelocity().y > 0))
            {      if(Hud.booster>=8) {
                Hud.booster=0;
                player.b2body.applyLinearImpulse(new Vector2(4f, 0), player.b2body.getWorldCenter(), true);
            }
                if(mking!=null)
            {
                mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
            }
            }
            if(controller.isBoostPressed() && (player.b2body.getLinearVelocity().y == 0 || player.b2body.getLinearVelocity().y < 0 || player.b2body.getLinearVelocity().y > 0))
            {
                if(Hud.booster>=8)
            {
                System.out.println("whyyyy");
                Hud.booster=0;
                player.b2body.setLinearVelocity(4f,0f);
            }
            if(mking!=null)
            {
                mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
            }
        }

            if(Gdx.input.isKeyPressed(Input.Keys.Q) && player.b2body.getLinearVelocity().y > 0) {
                player.b2body.applyLinearImpulse(new Vector2(.2f, -.35f), player.b2body.getWorldCenter(),true );
                if(mking!=null)
                {
                    mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
                }
            }

            /*if(mking!=null)
            {
                System.out.println("whatttt and why");
                if(Gdx.input.isKeyPressed(Input.Keys.L)){
                    mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
                }
                else if(Gdx.input.isKeyPressed(Input.Keys.K)){
                    mking.setPosition(mking.getX() + (-.2f*dt),mking.getY());
                }
            }*/
        }
    }

    public void reposition()
    {
        if(player.b2body.getPosition().x<40 && (player.b2body.getPosition().y<0 || tigercollision==1) && Lives>1)
        {
            mainlives--;
            tigercollision=0;
            checkpoint = 0;
            Lives--;
            player = new King(this);
        }
        else if(player.b2body.getPosition().x>40 && player.b2body.getPosition().x<80 && (player.b2body.getPosition().y<0 || tigercollision==1) && Lives>1)
        {
            mainlives--;
            tigercollision=0;
            checkpoint = 1;
            Lives--;
            player = new King(this);
        }

        else if(player.b2body.getPosition().x>80 &&(player.b2body.getPosition().y<0 || tigercollision==1)&& Lives>1)
        {
            mainlives--;
            tigercollision=0;
            checkpoint = 2;
            Lives--;
            player = new King(this);
        }
    }
    public void scorecounter()
    {
        hud.score += hud.WorldTimer*100;
        hud.WorldTimer=100;
    }

    public void updateServer(float dt)
    {
        timer += dt;
        if(timer>= UPDATE_TIME && mking!=null && mking.hasMoved())
        {
            JSONObject data = new JSONObject();
            try {
                data.put("x",player.getX());
                data.put("y",player.getY());
                socket.emit("playerMoved",data);
            }catch (JSONException e)
            {
                Gdx.app.log("Socket.IO","Error sending update data");
            }
        }
    }

    public  void update(float dt) {
        if(mking!=null)
        {
            mking.setPosition(player.b2body.getPosition().x,player.b2body.getPosition().y);
        }
        if(mainlives>1)
       reposition();

        //for creating pause button
        if(cont==0) {
         cont = 1;
            controller = new Controller(game.batch);
        }
        //to see any touch movement - handling user input
        handleInput(dt);
        player.update(dt);
        bird.update(dt);
        if(partner!=null)
        partner.update(dt);
        for(Enemy enemy: creator.getTigers())
        {
            enemy.update(dt);
        }
        for(CoinAbstract coinAbstract: creator.getCoins())
            coinAbstract.update(dt);

        if(mainlives<1)
        {
            screencount +=dt;
            if(screencount>4)
                game.setScreen(new GameOverScreen(game));
        }
        if(player.b2body.getPosition().x>121 && world1 == true && world2 == true)
        {
            scorecounter();
            game.setScreen(new HappyEnding(game));
            //game.setScreen(new GameOverScreen(game));
        }
        if(player.b2body.getPosition().y<0)
        {
            game.setScreen(new GameOverScreen(game));
        }

        //update gamecam with correct coordinates after changes
        gamecam.update();

        //telling gamecam to draw only camera can see in our game world
        renderer.setView(gamecam);
        hud.update(dt);
        world.step(1/60f, 6, 2);
        gamecam.position.x = player.b2body.getPosition().x + 100/BananaKong.PPM;

    }

    @Override
    public void render(float delta) {
        //when the game is on
        if(!controller.isPause) {

            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            xyz = 0;
            pausemove = -800;
            timecount += delta;

            //to change the world
            if(player.b2body.getPosition().x>120 && world1 == false && world2 == false)
            {
                world1 = true;
                scorecounter();
                worldcreator();
            }
            else if(player.b2body.getPosition().x>120 && world1 == true && world2 == false)
            {
                world2 = true;
                scorecounter();
                worldcreator();
            }
            update(delta);
            updateServer(delta);
            //rendering game map
            renderer.render();

            //rendering box2D debug lines
            //b2dr.render(world, gamecam.combined);

            game.batch.setProjectionMatrix(gamecam.combined);

            game.batch.begin();

            for(CoinAbstract coinAbstract: creator.getCoins()) {

                coinAbstract.draw(game.batch);
            }

            player.draw(game.batch);
            bird.draw(game.batch);
            if(partner!=null)
            partner.draw(game.batch);


            for(HashMap.Entry<String,multipleking> entry : Oppositions.entrySet()){

                entry.getValue().draw(game.batch);
            }

            for(Enemy enemy: creator.getTigers())
            {
                enemy.draw(game.batch);
            }

            game.batch.end();

            //setting our batch to now draw what the hud camera sees
            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();
            controller.stage.draw();

        }
        //if game is paused
        if(controller.isPause)
        {

            //clear the game screen with black
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            xyz += delta;
            System.out.println(xyz);
            if(pausemove<0)
            {
                pausemove += 100;
            }
            //calling the method for the pause menu
            pausemenu();

            game.batch.begin();
            game.batch.draw(pauseImg,pausemove,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            game.batch.end();
            stage.draw();
        }


    }

    public TiledMap getMap()
    {
        return map;
    }
    public World getWorld()
    {
        return world;
    }

    @Override
    public void resize(int width, int height) {

        //updating game viewport

        gameport.update(width,height);
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

        //to dispose all the open resources
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }

    private boolean gameOver () {
        return true;
    }

    public static int getScore () {
        return score = Hud.score;
    }

    public static int getDistance() {
        return distance = 120;
    }

    public static int getCoins() {
        return coins =Hud.coincount/2;
    }
}
