package ui;

import java.awt.Dimension;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

	JFormattedTextField nodeCountField;
	JFormattedTextField capacityField;

	public MenuPanel(MainFrame parent) {
		JButton button = new JButton("Generate");
		button.setSize(100, 30);
		button.setActionCommand("generate");
		button.addActionListener(parent);
		JLabel nodeCountLabel = new JLabel("Nodes:");
		nodeCountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		nodeCountField.setValue(new Long(5));
		nodeCountField.setColumns(3);
		JLabel capacityLabel = new JLabel("Capacity:");
		capacityField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		capacityField.setValue(new Long(5));
		capacityField.setColumns(3);
		JPanel nodeCountPanel = new JPanel();
		nodeCountPanel.add(nodeCountLabel);
		nodeCountPanel.add(nodeCountField);
		nodeCountPanel.add(capacityLabel);
		nodeCountPanel.add(capacityField);

		this.add(nodeCountPanel);
		this.add(button);
		this.setMinimumSize(new Dimension((int) (0.2 * parent.getWidth()), parent.getHeight()));
	}

	public int getCapacity() {
		return ((Long) capacityField.getValue()).intValue();
	}

	public int getNodeCount() {
		return ((Long) nodeCountField.getValue()).intValue();
	}

}
