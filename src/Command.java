import java.util.List;

import javax.swing.JLabel;

public class Command {
    private int mult;
    private Weapon w;
    private UpgradeType type;

    public Command(int mult, Weapon w, UpgradeType type) {
        this.mult = mult;
        this.w = w;
        this.type = type;
    }

    public String toString() {
        String s = w.toString();
        if (type == UpgradeType.NEWWEAPON) {
            s = "NEW WEAPON: " + s;
        } else if (type == UpgradeType.AMMO){
            s += ": More Ammo";
        } else if (type == UpgradeType.DAMAGE) {
            s += ": More Damage";
        } else if (type == UpgradeType.DELAY) {
            s += ": Faster Fire";
        } else if (type == UpgradeType.RANGE){
            s += ": More Range";
        } else {
            s += ": Wider Shot";
        }
        return s;
    }

    public int getMult() {
        return mult;
    }

    public void execute(GameScreen game, Boxhead b, Message message) {
        List<Weapon> weapons = b.getWeapons();
        if (type == UpgradeType.NEWWEAPON) {
            weapons.add(w);
        } else {
            Weapon upgrade = weapons.get(weapons.indexOf(w));
            if (type == UpgradeType.AMMO) {
                upgrade.setMaxAmmo();
                upgrade.setAmmo();
            } else if (type == UpgradeType.DAMAGE) {
                upgrade.setDamage();
            } else if (type == UpgradeType.DELAY) {
                upgrade.setDelay();
                if (b.getW() == upgrade) {
                    game.getShoot().setDelay(upgrade.getDelay() - 50);
                }
            } else if (type == UpgradeType.RANGE) {
                upgrade.setMaxRange();
            } else {
                Shotgun shotgun = (Shotgun) upgrade;
                shotgun.setWidth();
            }
        }
        message.add(new JLabel(toString()));
        message.display();
    }
}
