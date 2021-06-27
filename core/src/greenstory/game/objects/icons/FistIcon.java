package greenstory.game.objects.icons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.screens.StageLoadingScreen;

public class FistIcon extends Actor implements Disposable {
    private TextureRegion fist;
    private AssetManager manager = StageLoadingScreen.manager;

    public FistIcon() {
        fist = new TextureRegion((Texture) manager.get("fist.png"));
        setWidth(16);
        setHeight(16);
        setRotation(0);
        setBounds(0, 0, getWidth(), getHeight());
    }

    public void act(float delta) {
        super.act(delta);
    }

    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.draw(fist, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void dispose() {
        fist.getTexture().dispose();
    }
}
