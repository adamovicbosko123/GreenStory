package greenstory.game.objects.collectibles;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;
import greenstory.game.screens.PlayScreen;
import greenstory.game.screens.StageLoadingScreen;

public class Arrows extends Sprite implements Collectibles {
    private TextureRegion collectible;
    private Rectangle rectangle;
    private PlayScreen playScreen;
    private Player player;
    private boolean readyToBeCollected;
    private Sound arrowSound;

    public Arrows(PlayScreen playScreen, float x, float y) {
        this.playScreen = playScreen;
        player = playScreen.getPlayer();
        collectible = new TextureRegion(StageLoadingScreen.manager.<Texture>get("arrow.png"));
        setRegion(collectible);
        setBounds(0, 0, 16 / GreenStory.PPM, 16 / GreenStory.PPM);
        setPosition(x, y);
        setRectangle(this.getBoundingRectangle());
        readyToBeCollected = true;
        arrowSound = StageLoadingScreen.manager.get("sounds/arrow.wav");
    }

    public void update(float delta) {
        if (player.getPlayerRectangle().overlaps(rectangle)) {
            updatePlayer(player);
        }

    }

    public boolean isReadyToBeCollected() {
        return readyToBeCollected;
    }


    public void updatePlayer(Player player) {
        player.setArrowCount(player.getArrowCount() + 3);
        long id = arrowSound.play();
        arrowSound.setLooping(id, false);
        arrowSound.setVolume(id, 0.1f);
        readyToBeCollected = false;
    }

    public void dispose() {
        collectible.getTexture().dispose();
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

}
