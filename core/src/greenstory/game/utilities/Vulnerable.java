package greenstory.game.utilities;

public interface Vulnerable {
    void setHealth(int health);

    void setMaxHealth(int maxHealth);

    int getHealth();

    int getMaxHealth();

    void damageHealth();
}
