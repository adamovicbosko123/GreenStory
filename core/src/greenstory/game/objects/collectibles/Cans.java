package greenstory.game.objects.collectibles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

import java.util.Random;

public class Cans extends Sprite implements Collectibles {
    private TextureRegion collectible;
    private Rectangle rectangle;
    private PlayScreen playScreen;
    private Player player;
    private boolean readyToBeCollected = false;
    private Random random = new Random();
    private int style = 0;
    private Sound drinkSound;

    public Cans(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.player = playScreen.getPlayer();
        style = random.nextInt(3) + 1;
        collectible = new TextureRegion(StageLoadingScreen.manager.<Texture>get("can" + style + ".png"));
        setRegion(collectible);
        setBounds(0, 0, 16 / GreenStory.PPM, 16 / GreenStory.PPM);
        setPosition(x, y);
        setRectangle(this.getBoundingRectangle());
        readyToBeCollected = true;
        drinkSound = StageLoadingScreen.manager.get("sounds/drink.wav");

    }


    public void update(float dt) {
        if (player.getPlayerRectangle().overlaps(rectangle)) {
            updatePlayer(player);
        }
    }

    public Rectangle getRectangle() {
        return this.rectangle;
    }


    public void updatePlayer(Player player) {
        player.setEnergyTime(player.getEnergyTime() + 30);
        long id = drinkSound.play();
        drinkSound.setLooping(id, false);
        drinkSound.setVolume(id, 0.1f);
        readyToBeCollected = false;
    }

    public boolean isReadyToBeCollected() {
        return readyToBeCollected;
    }

    public void dispose() {
        collectible.getTexture().dispose();
        drinkSound.dispose();
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }


}