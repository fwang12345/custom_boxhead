import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Devil extends Vulnerable {
    private JLabel character;
    private static final int VELOC = 4;
    private Timer attack;
    private boolean ready;

    public Devil(int x, int y, int dx, int dy, Direction d, int id) {
        super(x, y, dx, dy, 1200, d, id);

        // manage layout
        setOpaque(false);
        setBounds(getPx() - 30, getPy() - 30, 60, 60);

        // add image
        ImageIcon image = new ImageIcon("files/devil" + d.name() + ".png");
        character = new JLabel(image);
        character.setPreferredSize(new Dimension(60, 60));
        add(character);

        ready = true;
        attack = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        attack.setInitialDelay(0);
    }

    private void tick() {
        ready = !ready;
        if (ready) {
            attack.stop();
        }
    }

    public boolean getReady() {
        return ready;
    }

    public void shoot() {
        attack.setInitialDelay(0);
        attack.start();
    }
    public void pauseAttack() {
        attack.stop();
        ready = false;
        attack.setInitialDelay(1000);
        attack.start();
    }

    public void paintComponent(Graphics g) {
        setLocation(new Point(getPx() - 30, getPy() - 30));
        String file = "files/devil" + getD().name() + ".png";
        character.setIcon(new ImageIcon(file));
        super.paintComponent(g);
    }

    public void changeDirection(int boxPx, int boxPy) {
        int px = getPx();
        int py = getPy();
        int dx = boxPx - px;
        int dy = boxPy - py;
        setDx(0);
        setDy(0);
        if (Math.abs(dx) >= Math.abs(dy)) {
            if (dx < 0) {
                setD(Direction.LEFT);
                setDx(-VELOC);

            } else {
                setD(Direction.RIGHT);
                setDx(VELOC);
            }
        } else {
            if (dy < 0) {
                setD(Direction.UP);
                setDy(-VELOC);

            } else {
                setD(Direction.DOWN);
                setDy(VELOC);
            }
        }
    }

    public int distanceTo(Boxhead b) {
        int dx = b.getPx() - getPx();
        int dy = b.getPy() - getPy();
        return (int) Math.sqrt(dx * dx + dy * dy);
    }
}
