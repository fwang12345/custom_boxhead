import java.awt.Graphics;

public abstract class Weapon implements Comparable<Weapon>{
    private int ammo;
    private int maxAmmo;
    private int delay;
    private int dmg;
    private int range;
    private int maxRange;
    private String name;
    private boolean weaponReady;

    public Weapon(String name, int maxAmmo, int delay, int dmg, int maxRange) {
        this.name = name;
        this.ammo = maxAmmo;
        this.maxAmmo = maxAmmo;
        this.delay = delay;
        this.dmg = dmg;
        range = 0;
        this.maxRange = maxRange;
        weaponReady = true;
    }
    public String toString() {
        return name;
    }

    public int getDelay() {
        return delay;
    }

    public int getRange() {
        return range;
    }

    public int getAmmo() {
        return ammo;
    }

    public int getMaxRange() {
        return maxRange;
    }

    public int getDmg() {
        return dmg;
    }

    public boolean isWeaponReady() {
        return weaponReady;
    }

    public void setWeaponReady() {
        weaponReady = !weaponReady;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void setAmmo() {
        ammo = maxAmmo;
    }
    public void decAmmo() {
        ammo = Math.max(-1, ammo - 1);
    }
    public int compareTo(Weapon w) {
        return name.compareTo(w.toString());
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (!(getClass() == o.getClass())) {
            return false;
        }
        Weapon w = (Weapon) o;
        return name.equals(w.toString());
    }
    public void setMaxAmmo() {
        maxAmmo *= 2;
    }

    public void setDamage() {
        dmg *= 2;
    }

    public void setDelay() {
        delay /= 2;
    }

    public void setMaxRange() {
        maxRange *= 2;
    }

    public abstract void fire(Graphics g);

    public abstract void damage(GameScreen game);

    public abstract void reset();

}
