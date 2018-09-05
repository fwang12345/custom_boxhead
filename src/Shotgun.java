import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Shotgun extends Weapon {
    private Vulnerable[] target;
    private LineSeg[] line;
    private int angle;
    public Shotgun() {
        super("Shotgun", 20, 400, 60, 200);
        target = new Vulnerable[3];
        line = new LineSeg[3];
        angle = 3;
    }

    private static double toRadian(double degree) {
        return degree * Math.PI / 180;
    }

    public void setWidth() {
        line = new LineSeg[line.length + 2];
        target = new Vulnerable[target.length + 2];
    }
    public void damage(GameScreen game) {
        Set<Zombie> zombies = game.getZombies();
        Set<Devil> devils = game.getDevils();
        Boxhead boxhead = game.getBoxhead();
        QuadTree tree = game.getQuadTree();
        int boxX = boxhead.getPx();
        int boxY = boxhead.getPy();
        int range = getRange();
        Direction d = boxhead.getD();
        if (d == Direction.LEFT) {
            for (int i = 0; i < target.length; i++) {
                int targetX = boxX;
                int targetY = boxY;
                if (i < target.length - 1) {
                    int index = i / 2 + 1;
                    double sign = Math.pow(-1, i % 2);
                    targetX -= (int) (range * Math.cos(toRadian(angle * index)));
                    targetY += (int) (sign * range * Math.sin(toRadian(angle * index)));
                } else {
                    targetX -= range;
                }
                line[i] = new LineSeg(boxX, boxY, targetX, targetY);
            }
        } else if (d == Direction.RIGHT) {
            for (int i = 0; i < target.length; i++) {
                int targetX = boxX;
                int targetY = boxY;
                if (i < target.length - 1) {
                    int index = i / 2 + 1;
                    double sign = Math.pow(-1, i % 2);
                    targetX += (int) (range * Math.cos(toRadian(angle * index)));
                    targetY += (int) (sign * range * Math.sin(toRadian(angle * index)));
                } else {
                    targetX += range;
                }
                line[i] = new LineSeg(boxX, boxY, targetX, targetY);
            }
        } else if (d == Direction.UP) {
            for (int i = 0; i < target.length; i++) {
                int targetX = boxX;
                int targetY = boxY;
                if (i < target.length - 1) {
                    int index = i / 2 + 1;
                    double sign = Math.pow(-1, i % 2);
                    targetX += (int) (sign * range * Math.sin(toRadian(angle * index)));
                    targetY -= (int) (range * Math.cos(toRadian(angle * index)));
                } else {
                    targetY -= range;
                }
                line[i] = new LineSeg(boxX, boxY, targetX, targetY);
            }
        } else {
            for (int i = 0; i < target.length; i++) {
                int targetX = boxX;
                int targetY = boxY;
                if (i < target.length - 1) {
                    int index = i / 2 + 1;
                    double sign = Math.pow(-1, i % 2);
                    targetX += (int) (sign * range * Math.sin(toRadian(angle * index)));
                    targetY += (int) (range * Math.cos(toRadian(angle * index)));
                } else {
                    targetY += range;
                }
                line[i] = new LineSeg(boxX, boxY, targetX, targetY);
            }
        }
        for (int i = 0; i < target.length; i++) {
            List<Vulnerable> hit = new ArrayList<Vulnerable>();
            if (line[i] != null) {
                tree.detectBulletCollision(hit, line[i]);
            }
            target[i] = null;
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

                line[i].intersect(lines);

                target[i] = closest;
                int hp = closest.getHealth();
                hp -= getDmg();
                if (closest instanceof Devil) {
                    Devil devil = (Devil) closest;
                    devil.pauseAttack();
                }
                if (hp <= 0) {
                    if (closest instanceof Zombie) {
                        game.incScore(100);
                        if (zombies.remove(closest)) {
                            game.incMult();
                            game.remove(closest);
                        };
                    } else {
                        game.incScore(1000);
                        if (devils.remove(closest)) {
                            game.incMult();
                            game.remove(closest);
                        };
                    }
                    game.revalidate();
                    game.repaint();
                } else {
                    closest.setHealth(hp);
                }
            }
        }
    }
    public void fire(Graphics g) {
        for (int i = 0; i < target.length; i++) {
            if (target[i] == null) {
                g.setColor(Color.YELLOW);
            } else {
                g.setColor(Color.RED);
            }
            if (line[i] != null) {
                g.drawLine(line[i].getStartX(), line[i].getStartY(), line[i].getEndX(), line[i].getEndY());
            }
        }
    }

    public void reset() {
        for (int i = 0; i < target.length; i++) {
            target[i] = null;
            line[i] = null;
        }
    }

}
