package greenstory.game.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;
import greenstory.game.player.Player;

public abstract class AbstractScreen extends ScreenAdapter {
    @Override
    public void show() {

    }

    public abstract Player getPlayer();

    public abstract boolean isEndOfStage();

    public abstract Batch getBatch();

    public abstract World getWorld();

    public abstract TiledMap getMap();

    public int getCountOfEnemies() {
        return 0;
    }

    @Override
    public void render(float delta) {

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
