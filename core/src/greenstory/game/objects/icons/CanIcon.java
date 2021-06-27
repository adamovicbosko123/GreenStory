package greenstory.game.objects.icons;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.screens.StageLoadingScreen;

import java.util.Random;

public class CanIcon extends Actor implements Disposable {
    private TextureRegion can;
    private AssetManager manager = StageLoadingScreen.manager;
    private Random random = new Random();
    private int style = 0;

    public CanIcon() {
        style = random.nextInt(3) + 1;
        can = new TextureRegion((Texture) manager.get("can" + style + ".png"));
        setWidth(16);
        setHeight(16);
        setRotation(0);
        setBounds(0, 0, getWidth(), getHeight());
    }

    public void draw(Batch batch, float parentAlpha) {
        Color color = batch.getColor();
        batch.draw(can, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
    }

    public void dispose() {
        can.getTexture().dispose();
    }
}
