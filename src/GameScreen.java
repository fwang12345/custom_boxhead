import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;

@SuppressWarnings("serial")
public class GameScreen extends JPanel{
    private static final String IMG_FILE = "files/box.jpg";
    private static BufferedImage img;
    private Message message;
    private JPanel parent;
    private CardLayout c;
    private JPanel startScreen;

    private Boxhead b;
    private Set<Zombie> zombies;
    private Set<Devil> devils;
    private Set<Projectile> projectiles;
    private Resource[] resources;
    private QuadTree tree;

    private List<Command> upgrades;

    private int numZombie;
    private int numDevil;
    private int numProjectile;

    private Timer shoot;
    private Timer change;
    private Timer move;
    private Timer spawn;
    private Timer mult;
    private Timer endGame;

    private int round;
    private int multiplier;
    private int maxMult;
    private int score;

    private JLabel roundText;
    private JLabel multText;
    private JLabel scoreText;

    private static final int VELOC = 6;

    public GameScreen(JPanel parent, JPanel startScreen, CardLayout c) {
        this.parent = parent;
        this.c = c;
        this.startScreen = startScreen;

        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        b = new Boxhead();
        setLayout(null);
        add(b);

        message = new Message();
        add(message);

        round = 0;
        numZombie = 0;
        numDevil = 0;
        numProjectile = 0;
        multiplier = 1;
        maxMult = 1;
        score = 0;

        upgrades = new LinkedList<Command>();
        addCommands();

        roundText = new JLabel("Round " + round);
        roundText.setBounds(450, 680, 100, 20);
        roundText.setFont(new Font(roundText.getFont().getName(), Font.PLAIN, 20));
        roundText.setHorizontalAlignment(JLabel.CENTER);
        add(roundText);

        multText = new JLabel("Multiplier: " + multiplier);
        multText.setBounds(700, 5, 300, 30);
        multText.setFont(new Font(multText.getFont().getName(), Font.PLAIN, 30));
        add(multText);

        scoreText = new JLabel("000000000000");
        scoreText.setBounds(100, 5, 300, 30);
        scoreText.setFont(new Font(scoreText.getFont().getName(), Font.PLAIN, 30));
        add(scoreText);


        // keyboard interaction
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                Direction d = b.getD();
                if (code == KeyEvent.VK_UP) {
                    b.setD(Direction.UP);
                    if (d == Direction.RIGHT || d == Direction.LEFT) {
                        b.setDx(0);
                    }
                    b.setDy(-VELOC);
                } else if (code == KeyEvent.VK_DOWN) {
                    b.setD(Direction.DOWN);
                    if (d == Direction.RIGHT || d == Direction.LEFT) {
                        b.setDx(0);
                    }
                    b.setDy(VELOC);
                } else if (code == KeyEvent.VK_LEFT) {
                    b.setD(Direction.LEFT);
                    if (d == Direction.UP || d == Direction.DOWN) {
                        b.setDy(0);
                    }
                    b.setDx(-VELOC);
                } else if (code == KeyEvent.VK_RIGHT) {
                    b.setD(Direction.RIGHT);
                    if (d == Direction.UP || d == Direction.DOWN) {
                        b.setDy(0);
                    }
                    b.setDx(VELOC);
                } else if (code == KeyEvent.VK_Z) {
                    b.prevWeapon();
                    Weapon w = b.getW();
                    int delay = w.getDelay();
                    shoot.setDelay(delay - 50);
                    shoot.stop();
                    if (!w.isWeaponReady()) {
                        w.setWeaponReady();
                    }
                } else if (code == KeyEvent.VK_X) {
                    b.nextWeapon();
                    Weapon w = b.getW();
                    int delay = w.getDelay();
                    shoot.setDelay(delay - 50);
                    shoot.stop();
                } else if (code == KeyEvent.VK_SPACE) {
                    Weapon w = b.getW();
                    w.setRange(w.getMaxRange());
                }
            }
            public void keyReleased(KeyEvent e) {
                int code = e.getKeyCode();
                Direction d = b.getD();
                if (code == KeyEvent.VK_UP && d == Direction.UP) {
                    b.setDy(0);
                } else if (code == KeyEvent.VK_DOWN && d == Direction.DOWN) {
                    b.setDy(0);
                } else if (code == KeyEvent.VK_LEFT && d == Direction.LEFT) {
                    b.setDx(0);
                } else if (code == KeyEvent.VK_RIGHT && d == Direction.RIGHT) {
                    b.setDx(0);
                } else if (code == KeyEvent.VK_SPACE) {
                    Weapon w = b.getW();
                    w.setRange(0);
                } 
            }
        });
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                System.out.println("Mouse: " + e.getX() + " " + e.getY());
            }
        });

        move = new Timer(40, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                moveTick();
            }
        });

        shoot = new Timer(b.getW().getDelay(), new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireTick();
            }
        });

        shoot.setInitialDelay(50);

        change = new Timer(500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeTick();
            }
        });

        spawn = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                spawnTick();
            }
        });

        mult = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                multTick();
            }
        });

        mult.setInitialDelay(3000);

        endGame = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                endTick();
            }
        });

        endGame.setInitialDelay(3000);

        zombies = new TreeSet<Zombie>();

        devils = new TreeSet<Devil>();

        projectiles = new TreeSet<Projectile>();

        resources = new Resource[10];
        for (int i = 0; i < 10; i++) {
            int indexX = i % 2;
            int indexY = i % 5;
            int py = 120 + indexY * 120;
            if (indexX == 0) {
                resources[i] = new Resource(375, py, i);
            } else {
                resources[i] = new Resource(625, py, i);
            }
            add(resources[i]);
        }
        tree = new QuadTree(0, 0, 1000, 720, 0);
    }

    private void addCommands() {
        upgrades.add(new Command(5, new Uzi(), UpgradeType.NEWWEAPON));
        upgrades.add(new Command(8, new Pistol(), UpgradeType.DAMAGE));
        upgrades.add(new Command(10, new Shotgun(), UpgradeType.NEWWEAPON));
        upgrades.add(new Command(13, new Uzi(), UpgradeType.DELAY));
        upgrades.add(new Command(17, new Uzi(), UpgradeType.AMMO));
        upgrades.add(new Command(18, new Shotgun(), UpgradeType.DELAY));
        upgrades.add(new Command(21, new Shotgun(), UpgradeType.AMMO));
        upgrades.add(new Command(23, new Uzi(), UpgradeType.RANGE));
        upgrades.add(new Command(31, new Shotgun(), UpgradeType.WIDTH));
        upgrades.add(new Command(35, new Shotgun(), UpgradeType.RANGE));
        upgrades.add(new Command(39, new Uzi(), UpgradeType.AMMO));
        upgrades.add(new Command(41, new Shotgun(), UpgradeType.AMMO));
        upgrades.add(new Command(43, new Shotgun(), UpgradeType.DELAY));
        upgrades.add(new Command(48, new Uzi(), UpgradeType.DAMAGE));
        upgrades.add(new Command(51, new Shotgun(), UpgradeType.WIDTH));
        upgrades.add(new Command(56, new Shotgun(), UpgradeType.DAMAGE));
        upgrades.add(new Command(61, new Uzi(), UpgradeType.RANGE));
        upgrades.add(new Command(90, new Uzi(), UpgradeType.DAMAGE));	
    }
    public void startGame() {
        newRound();
        spawn.start();
        change.start();
        move.start();
        mult.start();
    }

    public void resumeGame() {
        spawn.start();
        change.start();
        move.start();
        mult.start();
        b.resume();
    }
    public void pauseGame() {
        setFocusable(false);
        spawn.stop();
        change.stop();
        move.stop();
        mult.stop();
        b.pause();
    }

    private void newRound() {
        round++;
        spawn.setDelay(Math.max(1000 - 50 * round, 100));
        roundText.setText("Round " + round);
        numZombie = (round + 1) * 5; 
        numDevil = round / 2;
        numProjectile = 0;
        //		Zombie z = new Zombie(540, 400, Direction.RIGHT, 2);
        //		Zombie z1 = new Zombie(540, 320, Direction.RIGHT, 3);
        //		Zombie z2 = new Zombie(460, 400, Direction.RIGHT, 4);
        //		Zombie z3 = new Zombie(460, 320, Direction.RIGHT, 5);
        //		spawned.add(z);
        //		spawned.add(z1);
        //		spawned.add(z2);
        //		spawned.add(z3);
        //		this.add(z);
        //		this.add(z1);
        //		this.add(z2);
        //		this.add(z3);		
    }

    private void moveTick() {
        QuadTree temp = new QuadTree(0, 0, 1000, 720, 0);
        int px = b.getPx();
        int py = b.getPy();
        b.moveUnit();
        temp.insert(b);
        for (Zombie z : zombies) {
            z.moveUnit();
            temp.insert(z);
        }
        for (Devil d : devils) {
            int distance = d.distanceTo(b);
            if (distance <= 200 && d.getReady()) {
                d.shoot();
                Projectile p = new Projectile(d.getPx(), d.getPy(), px, py, 8, numProjectile++);
                projectiles.add(p);
                add(p);
            } else if (distance > 200){
                d.moveUnit();
            }
            temp.insert(d);
        }

        tree = temp;
        tree.detectCollisions();

        Iterator<Projectile> iter = projectiles.iterator();
        while (iter.hasNext()) {
            Projectile p = iter.next();
            p.moveUnit();
            if (p.offScreen()) {
                iter.remove();
                remove(p);
            } else {
                if (p.intersect(b.getPx(), b.getPy())) {
                    b.setHealth(b.getHealth() - 25);
                    iter.remove();
                    remove(p);
                }
            }
        }

        //		tree.recurse(0, -1);
        //		System.out.println();
        for (int i = 0; i < 10; i++) {
            resources[i].generateAmmo(b, message);
        };
        revalidate();
        repaint();

        if (b.getHealth() <= 0) {
            pauseGame();
            setFocusable(false);
            endGame.start();
        }
    }

    private void changeTick() {
        int px = b.getPx();
        int py = b.getPy();
        for (Zombie z : zombies) {
            z.changeDirection(px, py);
        }
        for (Devil d : devils) {
            d.changeDirection(px, py);
        }
    }

    private void spawnTick() {
        //		System.out.println(numLeft + " " + spawned.size());
        if (numZombie == 0 && zombies.isEmpty() && numDevil == 0 && devils.isEmpty()) {
            newRound();
        } else {
            if (zombies.size() < 25 && numZombie > 0) {
                int indexX = numZombie % 5;
                int indexY = numZombie % 2;
                int px = 420 + indexX * 40;
                if (indexY == 0) {
                    Zombie z = new Zombie(px, 20, 0, 2, Direction.DOWN, (round + 1) * 5 - numZombie);
                    zombies.add(z);
                    add(z);
                } else {
                    Zombie z = new Zombie(px, 700, 0, -2, Direction.UP, (round + 1) * 5 - numZombie);
                    zombies.add(z);
                    add(z);
                }
                numZombie--;
            } 
            if (devils.size() < 3 && numDevil > 0) {
                int indexX = numDevil % 2;
                int indexY = numDevil % 5;
                int py = 120 + indexY * 120;
                if (indexX == 0) {
                    Devil d = new Devil(20, py, 4, 0, Direction.RIGHT, (round / 3 - numDevil));
                    devils.add(d);
                    add(d);
                } else {
                    Devil d = new Devil(980, py, -4, 0, Direction.LEFT, (round / 3 - numDevil));
                    devils.add(d);
                    add(d);
                }
                numDevil--;
            } 
            revalidate();
            repaint();
        }
    }
    private void fireTick() {
        Weapon w = b.getW();
        w.setWeaponReady();
        if (w.isWeaponReady()) {
            shoot.stop();
        } 
    }

    private void multTick() {
        multiplier = Math.max(1, multiplier - 1);
        mult.stop();
        mult.setInitialDelay(Math.max(500, 3000 - 30 * multiplier));
        multText.setText("Multiplier: " + multiplier);
        multText.repaint();
        mult.start();
    }

    private void endTick() {
        reset();
        c.show(parent, "1");
        startScreen.requestFocusInWindow();
        endGame.stop();
    }

    private void shoot(Graphics g) {
        Weapon w = b.getW();
        if (w.isWeaponReady()) {
            int range = w.getRange();
            int ammo = w.getAmmo();
            if (range != 0 && ammo != 0) {
                if (!shoot.isRunning()) {
                    w.decAmmo();
                    shoot.start();
                    w.damage(this);
                }
                w.fire(g);
            }
        }
    }

    public void incMult() {
        mult.stop();
        multiplier++;
        if (multiplier > maxMult) {
            maxMult++;
            if (maxMult <= 90) {
                Command c = upgrades.get(0);
                if (maxMult >= c.getMult()) {
                    c.execute(this, b, message);
                    upgrades.remove(0);
                }
            }
        }
        mult.setInitialDelay(Math.max(500, 3000 - 50 * multiplier));
        multText.setText("Multiplier: " + multiplier);
        multText.repaint();
        mult.start();
    }

    public int getMult() {
        return multiplier;
    }

    public int getMaxMult() {
        return maxMult;
    }
    public void incScore(int amount) {
        score += multiplier * amount;
        String s = "" + score;
        while (s.length() < 12) {
            s = "0" + s;
        }
        scoreText.setText(s);
        scoreText.repaint();
    }

    public void reset() {
        remove(b);
        b = new Boxhead();
        add(b);

        upgrades = new LinkedList<Command>();

        addCommands();

        shoot.setDelay(b.getW().getDelay() - 50);
        round = 0;
        numZombie = 0;
        numDevil = 0;
        numProjectile = 0;
        multiplier = 1;
        maxMult = 1;
        score = 0;

        roundText.setText("Round " + round);
        multText.setText("Multiplier: " + multiplier);
        scoreText.setText("000000000000");

        for (Zombie z : zombies) {
            remove(z);
        }
        zombies = new TreeSet<Zombie>();

        for (Devil d : devils) {
            remove(d);
        }
        devils = new TreeSet<Devil>();

        for (Projectile p : projectiles) {
            remove(p);
        }
        projectiles = new TreeSet<Projectile>();

        for (int i = 0; i < 10; i++) {
            resources[i].reset();
        }

        tree = new QuadTree(0, 0, 1000, 720, 0);

        pauseGame();
        revalidate();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
        shoot(g);
    }

    public Timer getShoot() {
        return shoot;
    }
    public Set<Zombie> getZombies() {
        return zombies;
    }
    public Set<Devil> getDevils() {
        return devils;
    }
    public Boxhead getBoxhead() {
        return b;
    }
    public QuadTree getQuadTree() {
        return tree;
    }

}