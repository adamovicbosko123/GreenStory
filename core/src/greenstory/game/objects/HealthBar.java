package greenstory.game.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;
import greenstory.game.GreenStory;
import greenstory.game.utilities.Vulnerable;

public class HealthBar extends Group implements Disposable {
    private TextureRegion healthBarBase;
    private HealthBarSlider healthBarSlider;
    public float sliceCount;
    private float sliceWidth;
    private float health = 0;
    private Vulnerable vulnerable;
    private float scale = 0;

    public HealthBar(Vulnerable vulnerable, float scale) {
        this.scale = scale;
        this.vulnerable = vulnerable;
        healthBarSlider = new HealthBarSlider(scale);
        healthBarBase = new TextureRegion(new Texture("bar_base.png"));
        setWidth(50 / scale);
        setHeight(5 / scale);
        setOrigin((getWidth() / scale) * 0.5f, (getHeight() / scale) * 0.5f);
        setRotation(0);
        setBounds(0, 0, getWidth() / scale, getHeight() / scale);

        health = healthBarSlider.getWidth();
        setSliceCount(vulnerable.getMaxHealth());
        setSliceWidth();

    }


    public void update(float delta) {
        getHealthBarSlider().setWidth(vulnerable.getHealth() * sliceWidth);
    }

    public void setSliceCount(int sliceCount) {
        //should be max health
        this.sliceCount = sliceCount;
    }

    public void setSliceWidth() {
        sliceWidth = health / sliceCount;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(GreenStory.mainGame.mainColor);

        addActor(healthBarSlider);

        Color color = batch.getColor();
        batch.draw(healthBarBase, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);

    }

    public HealthBarSlider getHealthBarSlider() {
        return this.healthBarSlider;
    }

    public Vulnerable getVulnerable() {
        return this.vulnerable;
    }

    public void setVulnerable(Vulnerable vulnerable) {
        this.vulnerable = vulnerable;
    }

    public void dispose() {
        healthBarBase.getTexture().dispose();
        healthBarSlider.dispose();
    }

}

class HealthBarSlider extends Actor implements Disposable {
    private TextureRegion healthBar;
    private float scale = 0;

    public HealthBarSlider(float scale) {
        this.scale = scale;
        healthBar = new TextureRegion(new Texture("bar.png"));
        setWidth(50 / scale);
        setHeight(5 / scale);
        setOrigin((getWidth() / scale) * 0.5f, (getHeight() / scale) * 0.5f);
        setRotation(0);
        setBounds(0, 0, getWidth() / scale, getHeight() / scale);
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(healthBar, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
                getHeight(), getScaleX(), getScaleY(), getRotation());
        super.draw(batch, parentAlpha);
    }

    public void dispose() {
        healthBar.getTexture().dispose();
    }
}