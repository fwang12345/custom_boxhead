
@SuppressWarnings("serial")
public class Vulnerable extends Unit{
    private Direction d;
    private int health;

    public Vulnerable(int px, int py, int dx, int dy, int health, Direction d, int id) {
        super(px, py, dx, dy, id);
        this.health = health;
        this.d = d;
    }

    public void setD(Direction d) {
        this.d = d;
    }

    public void setHealth(int hp) {
        this.health = hp;
    }

    public Direction getD() {
        return d;
    }

    public int getHealth() {
        return  health;
    }


}
