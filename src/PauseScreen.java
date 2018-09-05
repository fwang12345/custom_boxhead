import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class PauseScreen extends JPanel {
    private UpgradeTable table1;
    private UpgradeTable table2;
    private GameScreen game;
    public class UpgradeTable extends JTable {
        public UpgradeTable(TableModel dm) {
            super(dm);
        }
        public boolean isCellEditable(int data, int columns) {
            return false;
        }
        public Component prepareRenderer(TableCellRenderer r, int row, int column) {
            Component c = super.prepareRenderer(r, row, column);
            JComponent jc = (JComponent) c;
            if ((int) getModel().getValueAt(row, 0) <= game.getMaxMult()) {
                jc.setBorder(new LineBorder(Color.GREEN));
            } else {
                jc.setBorder(new LineBorder(Color.BLACK));
            }
            return c;
        }
    }

    public PauseScreen(GameScreen game) {
        this.game = game;
        Object[] columnNames = {"Multipler", "Upgrade"};
        Object[][] data1 = {{5, "New Weapon: Uzi"}, {8, "Pistol: Double Damage"}, 
                {10, "New Weapon: Shotgun"}, {13, "Uzi: Rapid Fire"}, 
                {17, "Uzi: Double Ammo"}, {18, "Shotgun: Fast Fire"}, 
                {21, "Shotgun: Double Ammo"}, {23, "Uzi: Long Shot"}, 
                {31, "Shotgun: Wide Shot"}};
        Object[][] data2 = {{35, "Shotgun: Long Shot"}, {39, "Uzi: Quad Ammo"}, 
                {41, "Shotgun: Quad Ammo"}, {43, "Shotgun: Rapid Fire"}, 
                {48, "Uzi: Double Damage"}, {51, "Shotgun: Wider Shot"}, 
                {56, "Shotgun: Double Damage"}, {61, "Uzi: Infinite Range"}, 
                {90, "Uzi: Quad Damage"}};
        DefaultTableModel model1 = new DefaultTableModel(data1, columnNames);
        DefaultTableModel model2 = new DefaultTableModel(data2, columnNames);

        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Upgrade List");
        label.setPreferredSize(new Dimension(200, 200));
        add(label);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);

        table1 = new UpgradeTable(model1);
        table2 = new UpgradeTable(model2);
        table1.setPreferredSize(new Dimension(400, 195));
        table2.setPreferredSize(new Dimension(400, 195));

        panel.add(table1);
        panel.add(table2);
        panel.setPreferredSize(new Dimension(800, 195));
        add(panel);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }
}
