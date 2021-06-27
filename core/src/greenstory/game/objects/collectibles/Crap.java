package greenstory.game.objects.collectibles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.player.PlayerStatesWrapper;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

import java.util.Random;

public class Crap extends Sprite implements Collectibles {
    private TextureRegion collectible;
    private Rectangle rectangle;
    private PlayScreen playScreen;
    private Player player;
    private boolean readyToBeCollected = false;
    private Random random = new Random();
    private int style = 0;
    private Sound crapSound;

    public Crap(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        this.player = playScreen.getPlayer();
        style = random.nextInt(7) + 1;
        collectible = new TextureRegion(StageLoadingScreen.manager.<Texture>get("crap" + style + ".png"));
        setRegion(collectible);
        setBounds(0, 0, 16 / GreenStory.PPM, 16 / GreenStory.PPM);
        setPosition(x, y);
        setRectangle(this.getBoundingRectangle());
        readyToBeCollected = true;
        crapSound = StageLoadingScreen.manager.get("sounds/crap.wav");
    }


    public void update(float dt) {
        if (player.getPlayerRectangle().overlaps(rectangle)) {
            updatePlayer(player);
        }
    }


    public void updatePlayer(Player player) {
        long id = crapSound.play();
        crapSound.setLooping(id, false);
        crapSound.setVolume(id, 0.1f);
        readyToBeCollected = false;
        player.getFSM().changeState(PlayerStatesWrapper.PlayerStates.ITEM);
    }

    public boolean isReadyToBeCollected() {
        return readyToBeCollected;
    }

    public void dispose() {
        collectible.getTexture().dispose();
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }


}