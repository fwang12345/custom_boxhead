import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Uzi extends Weapon {
    private Unit target;
    private LineSeg line;

    public Uzi() {
        super("Uzi", 100, 200, 40, 250);
    }

    public void damage(GameScreen game) {
        Set<Zombie> zombies = game.getZombies();
        Set<Devil> devils = game.getDevils();
        Boxhead boxhead = game.getBoxhead();
        QuadTree tree = game.getQuadTree();
        int boxX = boxhead.getPx();
        int boxY = boxhead.getPy();
        int targetX = boxX;
        int targetY = boxY;
        int range = getRange();
        Direction d = boxhead.getD();
        if (d == Direction.LEFT) {
            targetX -= range;
        } else if (d == Direction.RIGHT) {
            targetX += range;
        } else if (d == Direction.UP) {
            targetY -= range;
        } else {
            targetY += range;
        }
        line = new LineSeg(boxX, boxY, targetX, targetY);
        List<Vulnerable> hit = new ArrayList<Vulnerable>();
        tree.detectBulletCollision(hit, line);
        target = null;
        if (!hit.isEmpty()) {
            int minDistance = Integer.MAX_VALUE;
            Vulnerable closest = null;
            for (Vulnerable u : hit) {
                int hitX = u.getPx();
                int hitY = u.getPy();
                int distance = (boxX - hitX) * (boxX - hitX) + (boxY - hitY) * (boxY - hitY);
                if (distance < minDistance) {
                    minDistance = distance;
                    closest = u;
                }
            }

            int closestX = closest.getPx();
            int closestY = closest.getPy();

            LineSeg[] lines = new LineSeg[4];
            lines[0] = new LineSeg(closestX - 20, closestY - 20, closestX + 19, closestY - 20);
            lines[1] = new LineSeg(closestX + 19, closestY - 20, closestX + 19, closestY + 19);
            lines[2] = new LineSeg(closestX + 19, closestY + 19, closestX - 20, closestY + 19);
            lines[3] = new LineSeg(closestX - 20, closestY + 19, closestX - 20, closestY - 20);

            line.intersect(lines);

            target = closest;
            int hp = closest.getHealth();
            hp -= getDmg();
            if (closest instanceof Devil) {
                Devil devil = (Devil) closest;
                devil.pauseAttack();
            }
            if (hp <= 0) {
                if (closest instanceof Zombie) {
                    game.incScore(100);
                    zombies.remove(closest);
                } else {
                    game.incScore(1000);
                    devils.remove(closest);
                }
                game.incMult();
                game.remove(closest);
                game.revalidate();
                game.repaint();
            } else {
                closest.setHealth(hp);
            }
        }

    }
    public void fire(Graphics g) {
        if (target == null) {
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.RED);
        }
        g.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
    }

    public void reset() {
        target = null;
        line = null;
    }
}
