package br.com.platformQuest;

import br.com.platformQuest.Entities.BasePlatform;
import br.com.platformQuest.Entities.EntitiesManager;
import br.com.platformQuest.Entities.Player;
import br.com.platformQuest.Helper.CollisionsHandler;
import br.com.platformQuest.Helper.Constants;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Viewport view;
    private OrthographicCamera camera;
    private Texture background;
    private World world;
    private Player player;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        world = new World(new Vector2(0, -12), true);
        world.setContactListener(new CollisionsHandler());
        background = new Texture("background.png");
        camera = new OrthographicCamera(720, 1280);
        view = new FitViewport(720, 1280, camera);
        view.apply();
        camera.position.set(720 / 2f, 1280 / 2f, 0);
        batch = new SpriteBatch();
        debugRenderer = new Box2DDebugRenderer();
        EntitiesManager.getInstance().setWrld(world);
        createStartEntities();
    }

    private void createStartEntities() {
        player = new Player(15, 10, world, new Texture("player/players.png"));
        player.createFixture(0.5f, 1);

        BasePlatform basePlatform = new BasePlatform(Constants.WIDTH / Constants.PPM / 2, 0, world, new Texture("bigPlatform.png"));
        basePlatform.createFixture(Constants.WIDTH / 2 / Constants.PPM, 0.5f);

        EntitiesManager eManager = EntitiesManager.getInstance();
        eManager.addEntity(player);
        eManager.addEntity(basePlatform);
        EntitiesManager.getInstance().setPlayer(player);

        EntitiesManager.getInstance().createPlatforms(5,11);
    }

    @Override
    public void render() {
        if (player.isAlive()) {
            update();
            input();
            draw();
        } else {
            drawGameOverScreen();
        } 
    }

    public void draw() {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        view.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(background, 0, 0);
        EntitiesManager.getInstance().drawEntities(batch);
        batch.end();

        world.step(1 / 60f, 6, 2);
        EntitiesManager.getInstance().executeScheduledActions();
        debugRenderer.render(world, camera.combined.scl(32));
    }

    @Override
    public void resize(int width, int height) {
        view.update(width, height); // Atualiza o viewport para o novo tamanho da janela
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void input() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            player.moveRight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            player.moveLeft();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player.jump();
        }
    }

    private void update() {
        EntitiesManager.getInstance().updateEntities();
    }

    private void drawGameOverScreen() {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();

        // Desenha uma mensagem no centro da tela
        BitmapFont font = new BitmapFont();
        font.setColor(Color.WHITE);
        String gameOverText = "Game Over!";
        String restartText = "Press R to Restart";
        float textWidth = font.getRegion().getRegionWidth();

        font.draw(batch, gameOverText, camera.position.x - textWidth / 2, camera.position.y + 50);
        font.draw(batch, restartText, camera.position.x - textWidth / 2, camera.position.y - 50);

        batch.end();

    }

}
