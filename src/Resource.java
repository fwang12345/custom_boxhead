import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Resource extends Unit {
    private JLabel picture;
    private Timer spawn;
    boolean ready;

    public Resource(int px, int py, int id) {
        super(px, py, 0, 0, id);

        // manage layout
        setOpaque(false);
        setBounds(getPx() - 20, getPy() - 20, 40, 40);

        // add image
        ImageIcon image = new ImageIcon("files/resource.png");
        picture = new JLabel(image);
        picture.setPreferredSize(new Dimension(20, 20));
        add(picture);

        ready = true;
        spawn = new Timer (5000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        spawn.setInitialDelay(0);
    }

    private void tick() {
        ready = !ready;
        if (ready) {
            setVisible(true);
            spawn.stop();
        } else {
            setVisible(false);
        }
    }
    public void paintComponent(Graphics g) {
        setLocation(new Point(getPx() - 20, getPy() - 20));
        super.paintComponent(g);
    }

    private boolean intersect(int x, int y) {
        int px = getPx();
        int py = getPy();
        return (px + 30 > x && py + 30 > y
                && x + 30 > px && y + 30 > py);
    }
    public void generateAmmo(Boxhead b, Message message) {
        if (intersect(b.getPx(), b.getPy()) && ready) {
            List<Weapon> weapons = b.getWeapons();
            double random = Math.random();
            int health = b.getHealth();
            boolean needHealth = health < 100 && health > 0;
            boolean found = false;
            int numWeapons = weapons.size() - 1;
            if (needHealth) {
                numWeapons++;
            }
            if (numWeapons != 0) {
                double partition = 1.0 / numWeapons;
                for (int i = 0; i < numWeapons && !found; i++) {
                    if (random < (i + 1) * partition) {
                        if (i + 1 == weapons.size()) {
                            b.setHealth(100);
                            message.add(new JLabel("Health Up"));
                        } else {
                            Weapon w = weapons.get(i + 1);
                            w.setAmmo();
                            found = true;
                            message.add(new JLabel(w.toString()));
                        }
                        message.display();
                    }
                }
            }
            spawn.start();
        }
    }

    public void reset() {
        setVisible(true);
        ready = true;
        spawn.stop();
    }
}
