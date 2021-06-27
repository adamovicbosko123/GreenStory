package greenstory.game.objects.harmlesobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import greenstory.game.GreenStory;
import greenstory.game.screens.StageLoadingScreen;

public class Arrow extends Sprite implements Harmles {
    private TextureRegion arrow;

    public Arrow() {
        arrow = new TextureRegion(StageLoadingScreen.manager.<Texture>get("arrow.png"));
        setRegion(arrow);
        setBounds(0, 0, 32f / GreenStory.PPM, 32f / GreenStory.PPM);
        setPosition(2, 4);
    }

    public void update(float dt) {
    }

    public void dispose() {
        arrow.getTexture().dispose();
    }
}
