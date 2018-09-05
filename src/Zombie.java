
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Zombie extends Vulnerable {
    private JLabel character;
    private Timer attack;
    private static final int VELOC = 2;
    private boolean ready;

    public Zombie(int x, int y, int dx, int dy, Direction d, int id) {
        super(x, y, dx, dy, 120, d, id);

        // manage layout
        setOpaque(false);
        setBounds(getPx() - 30, getPy() - 30, 60, 60);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // add image
        ImageIcon image = new ImageIcon("files/zombie" + d.name() + ".png");
        character = new JLabel(image);
        character.setPreferredSize(new Dimension(60, 60));
        add(character);
        character.setAlignmentX(Component.CENTER_ALIGNMENT);

        ready = true;

        attack = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        attack.setInitialDelay(1000);
    }

    private void tick() {
        ready = true;
        attack.stop();
    }

    public void damage(Boxhead b) {
        if (ready) {
            b.setHealth(b.getHealth() - 20);
            ready = false;
            attack.start();
        } 
    }
    public void paintComponent(Graphics g) {
        setLocation(new Point(getPx() - 30, getPy() - 30));
        String file = "files/zombie" + getD().name() + ".png";
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
            if (px >= 420 && px <= 580 && !(py > 59 && py < 660)) {
                if (dy < 0) {
                    setD(Direction.UP);
                    setDy(-VELOC);

                } else {
                    setD(Direction.DOWN);
                    setDy(VELOC);
                }
            } else if (dx < 0) {
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

}
