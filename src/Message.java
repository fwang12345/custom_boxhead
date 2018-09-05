import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Message extends JPanel {
    Timer timer;

    public Message() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setBounds(500, 360, 200, 400);
        timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.setInitialDelay(2000);
    }

    private void tick() {
        setVisible(false);
        removeAll();
        timer.stop();
    }

    public void display() {
        timer.stop();
        setVisible(true);
        timer.start();
    }
    public Component add(Component comp) {
        super.add(comp);
        JLabel label = (JLabel) comp;
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 15));
        label.setPreferredSize(new Dimension(200, 20));
        return label;
    }


    public void paintComponent(Graphics g) {
        setLocation(400, 680 - getPreferredSize().height);
        super.paintComponent(g);
    }
}
