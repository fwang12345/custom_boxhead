import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Projectile extends Unit {
    private JLabel picture;

    public Projectile(int shooterX, int shooterY, int targetX, int targetY, int v, int id) {
        super(shooterX, shooterY, 0, 0, id);
        int dx = targetX - shooterX;
        int dy = targetY - shooterY;
        double d = Math.sqrt(dx * dx + dy * dy);
        int vx = (int) (v * dx / d);
        int vy = (int) (v * dy / d);
        setDx(vx);
        setDy(vy);
        // manage layout
        setOpaque(false);
        setBounds(getPx() - 20, getPy() - 20, 40, 40);

        // add image
        ImageIcon image = new ImageIcon("files/projectile.png");
        picture = new JLabel(image);
        picture.setPreferredSize(new Dimension(20, 20));
        add(picture);
    }
    public void paintComponent(Graphics g) {
        setLocation(new Point(getPx() - 20, getPy() - 20));
        super.paintComponent(g);
    }

    public boolean intersect(int x, int y) {
        int px = getPx();
        int py = getPy();
        return (px + 30 > x && py + 30 > y
                && x + 30 > px && y + 30 > py);
    }
    public void moveUnit() {
        setPx(getPx() + getDx());
        setPy(getPy() + getDy());
    }

    public boolean offScreen() {
        int px = getPx();
        int py = getPy();
        if (px <= 9 || px >= 991) {
            return true;
        }
        else if (px <= 409 || px >= 591) {
            if (py <= 49 || py >= 671 ) {
                return true;
            } else {
                return false;
            }
        } else {
            if (py <= 9 || py >= 711 ) {
                return true;
            } else {
                return false;
            }
        }
    }
}
