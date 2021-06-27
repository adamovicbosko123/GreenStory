package greenstory.game.player.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.player.Player;

public class StaminaBar extends Group implements Disposable {
    private TextureRegion staminaBarBase;
    private StaminaBarSlider staminaBarSlider;
    //slice count, complete width
    public float sliderWidth;
    private float sliceWidth;
    private float stamina = 0;
    private Player player;
    private float scale = 0;

    public StaminaBar(Player player, float scale) {
        this.scale = scale;
        this.player = player;
        staminaBarSlider = new StaminaBarSlider(scale);
        staminaBarBase = new TextureRegion(new Texture("bar_base.png"));
        setWidth(50 / scale);
        setHeight(5 / scale);
        setOrigin((getWidth() / scale) * 0.5f, (getHeight() / scale) * 0.5f);
        setRotation(0);
        setBounds(0, 0, getWidth() / scale, getHeight() / scale);

        stamina = staminaBarSlider.getWidth();
        setSliderWidth(player.getMaxStrength());
        setSliceWidth();

    }


    public void update(float delta) {
        getStaminaBarSlider().setWidth(player.getStrength() * sliceWidth);
    }

    public void setSliderWidth(float sliderWidth) {
        //should be max health
        this.sliderWidth = sliderWidth;
    }

    public void setSliceWidth() {
        sliceWidth = stamina / sliderWidth;
    }

    public float getSliceWidth() {
        return this.sliceWidth;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(GreenStory.mainGame.mainColor);

        addActor(staminaBarSlider);

        Color color = batch.getColor();
        batch.draw(staminaBarBase, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);

    }

    public StaminaBarSlider getStaminaBarSlider() {
        return this.staminaBarSlider;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void dispose() {
        staminaBarBase.getTexture().dispose();
        staminaBarSlider.dispose();
    }
}

class StaminaBarSlider extends Actor implements Disposable {
    private TextureRegion staminaBar;
    private float scale = 0;

    public StaminaBarSlider(float scale) {
        this.scale = scale;
        staminaBar = new TextureRegion(new Texture("bar.png"));
        setWidth(50 / scale);
        setHeight(5 / scale);
        setOrigin((getWidth() / scale) * 0.5f, (getHeight() / scale) * 0.5f);
        setRotation(0);
        setBounds(0, 0, getWidth() / scale, getHeight() / scale);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(staminaBar, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }

    public void dispose() {
        staminaBar.getTexture().dispose();
    }
}