import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

@SuppressWarnings("serial")
public class Boxhead extends Vulnerable {
    private int maxHealth;
    private List<Weapon> weapons;
    private Weapon w;
    private JProgressBar healthBar;
    private JLabel weapon;
    private JLabel character;
    Timer regen;

    public Boxhead() {
        super(500, 360, 0, 0, 100, Direction.RIGHT, 0);

        maxHealth = 100;
        // manage weapons
        weapons = new ArrayList<Weapon>();
        Weapon w1 = new Pistol();
        w = w1;
        weapons.add(w1);
        // manage layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBounds(getPx() - 30, getPy() - 60, 60, 90);

        // add health bar
        healthBar = new JProgressBar(0, maxHealth);
        healthBar.setValue(maxHealth);
        healthBar.setStringPainted(true);
        healthBar.setPreferredSize(new Dimension(60, 15));

        // add image
        ImageIcon image = new ImageIcon("files/boxheadRIGHT.png");
        character = new JLabel(image);
        character.setPreferredSize(new Dimension(60, 60));

        // add weapon name
        weapon = new JLabel(w.toString());
        weapon.setPreferredSize(new Dimension(60, 15));
        Font font = weapon.getFont();
        weapon.setFont(new Font(font.getName(), Font.PLAIN, 10));

        // add to panel
        add(weapon);
        weapon.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(healthBar);
        healthBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(character);
        character.setAlignmentX(Component.CENTER_ALIGNMENT);

        regen = new Timer (1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        regen.start();
    }

    public void tick() {
        setHealth(Math.min(100, getHealth() + 2));
    }
    public Weapon getW() {
        return w;
    }

    public List<Weapon> getWeapons() {
        return weapons;
    }
    public void nextWeapon() {
        int index = weapons.indexOf(w);
        if (!w.isWeaponReady()) {
            w.setWeaponReady();
        }
        if (index == weapons.size() - 1) {
            w = weapons.get(0);
        } else {
            w = weapons.get(index + 1);
        }
        repaint();
    }
    public void prevWeapon() {
        int index = weapons.indexOf(w);
        if (!w.isWeaponReady()) {
            w.setWeaponReady();
        }
        if (index == 0) {
            w = weapons.get(weapons.size() - 1);
        } else {
            w = weapons.get(index - 1);
        }
        repaint();
    }
    public void paintComponent(Graphics g) {
        setLocation(new Point(getPx() - 30, getPy() - 60));
        String file = "files/boxhead" + getD().name() + ".png";
        character.setIcon(new ImageIcon(file));
        String s = w.toString();
        if (w.getAmmo() >= 0) {
            s += ": " + w.getAmmo();
        }
        weapon.setText(s);
        healthBar.setValue(getHealth());
        super.paintComponent(g);
    }
    public void pause() {
        regen.stop();
    }
    public void resume() {
        regen.start();
    }
}
