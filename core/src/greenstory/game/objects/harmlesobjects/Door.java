package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Door extends Sprite implements Harmles {
    private TextureRegion platform;
    private TextureRegion opendDoors, closedDoors;
    private Rectangle doorRectangle;
    //this boolean variable has been made to avoid sharing playscreen object with door object
    private boolean playerCameToDoor = false;
    private PlayScreen playScreen;

    public Door(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        platform = new TextureRegion(StageLoadingScreen.manager.<Texture>get("platform.png"));
        TextureRegion[][] tiles = platform.split(96, 1696);
        Array<TextureRegion> frames = new Array<>();
        opendDoors = tiles[1][4];
        opendDoors.setRegionHeight(96);

        closedDoors = tiles[1][3];
        closedDoors.setRegionHeight(96);
        setRegion(closedDoors);
        setBounds(0, 0, 96 / GreenStory.PPM, 96 / GreenStory.PPM);
        setPosition(x, y);
        doorRectangle = this.getBoundingRectangle();
    }

    private float timer = 0;

    @Override
    public void update(float delta) {
      //  if (playScreen.getCountOfEnemies() <= 0) {
            if (playerCameToDoor) {
                setRegion(opendDoors);
                if ((timer += delta) >= 3) {
                    StageLoadingScreen.manager.dispose();
                    StageLoadingScreen.enemyAssetManager.dispose();
                    timer = 0;
                    playScreen.dispose();

                    GreenStory.mainGame.setScreen(new StageLoadingScreen("happyend"));
                }
            }
            doorRectangle.set(getBoundingRectangle().x + 0.7f, getBoundingRectangle().y, getBoundingRectangle().width - 1.5f, getBoundingRectangle().height - 1);
      //  }
    }

    public boolean playerOnTheDoor(Player player) {
        return player.getPlayerRectangle().overlaps(doorRectangle);
    }

    public void setPlayerCameToDoor(boolean cameToDoor) {
        this.playerCameToDoor = cameToDoor;
    }

    //private ShapeRenderer renderer = new ShapeRenderer();

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void dispose() {
        platform.getTexture().dispose();
        opendDoors.getTexture().dispose();
        closedDoors.getTexture().dispose();
    }
}
