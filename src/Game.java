import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
    public void run() {
        // NOTE : recall that the 'final' keyword notes immutability even for local variables.

        // Top-level frame in which game components live
        // Be sure to change "TOP LEVEL FRAME" to the name of your game
        final JFrame frame = new JFrame("Boxhead");
        frame.setPreferredSize(new Dimension(1010, 758));

        // highest level panel
        final JPanel panel = new JPanel();

        CardLayout c = new CardLayout();
        panel.setLayout(c);
        frame.add(panel);

        // starting screen
        final JPanel startScreen = new JPanel();
        panel.add(startScreen, "1");
        startScreen.setLayout(new BoxLayout(startScreen, BoxLayout.Y_AXIS));
        startScreen.setBackground(Color.WHITE);

        final JPanel instructionScreen = new JPanel();
        panel.add(instructionScreen, "4");
        instructionScreen.setLayout(new BoxLayout(instructionScreen, BoxLayout.Y_AXIS));
        instructionScreen.setBackground(Color.WHITE);

        JLabel[] label = new JLabel[10];

        label[0] = new JLabel("Instructions");
        label[1] = new JLabel("Keys");
        label[2] = new JLabel("Space: Shoot");
        label[3] = new JLabel("Arrow Keys: Move");
        label[4] = new JLabel("Z: Previous Weapon");
        label[5] = new JLabel("X: Next Weapon");
        label[6] = new JLabel("Objects");
        label[7] = new JLabel("Zombies deal 20% HP on contact");
        label[8] = new JLabel("Devils deal 25% HP through projectiles");
        label[9] = new JLabel("Orange boxes can give you ammo or health");

        for (int i = 0; i < label.length; i++) {
            instructionScreen.add(label[i]);
            label[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            if (i == 0) {
                label[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 30));
                label[i].setPreferredSize(new Dimension(300, 40));
            }
            else {
                label[i].setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
                label[i].setPreferredSize(new Dimension(300, 20));
            }
        }

        instructionScreen.setOpaque(false);

        final JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.show(panel, "1");
                startScreen.requestFocusInWindow();
            }
        });
        instructionScreen.add(back);
        back.setAlignmentX(Component.CENTER_ALIGNMENT);

        // game screen
        final GameScreen gameScreen = new GameScreen(panel, startScreen, c);
        panel.add(gameScreen, "2");

        final PauseScreen pauseScreen = new PauseScreen(gameScreen);
        panel.add(pauseScreen, "3");

        pauseScreen.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_P) {
                    c.show(panel, "2");
                    gameScreen.setFocusable(true);
                    gameScreen.requestFocusInWindow();
                    gameScreen.resumeGame();
                } 
            }
        });
        // quit Button
        final JButton quit = new JButton("Quit");
        quit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.show(panel, "1");
                startScreen.requestFocusInWindow();
                gameScreen.reset();
            }
        });
        quit.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseScreen.add(quit);

        gameScreen.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_P) {
                    c.show(panel, "3");
                    gameScreen.pauseGame();
                    pauseScreen.setFocusable(true);
                    pauseScreen.requestFocusInWindow();
                } 
            }
        });

        // boxhead image
        ImageIcon image = new ImageIcon("files/start.jpg");
        JLabel logo = new JLabel(image);
        startScreen.add(logo);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // start button
        final JButton single = new JButton("Single Play");
        single.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.show(panel, "2");
                gameScreen.setFocusable(true);
                gameScreen.requestFocusInWindow();
                gameScreen.startGame();
            }
        });

        // instruction button
        final JButton instruction = new JButton("Instructions");
        instruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.show(panel, "4");
                instructionScreen.requestFocusInWindow();
            }
        });

        startScreen.add(single);
        single.setAlignmentX(Component.CENTER_ALIGNMENT);

        startScreen.add(instruction);
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * Main method run to start and run the game. Initializes the GUI elements specified in Game and
     * runs it. IMPORTANT: Do NOT delete! You MUST include this in your final submission.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Game());
    }
}