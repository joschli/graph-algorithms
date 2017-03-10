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
	JButton nextButton;
	JButton previousButton;

	public MenuPanel(MainFrame parent) {
		JButton generateButton = new JButton("Generate");
		generateButton.setSize(100, 30);
		generateButton.setActionCommand("generate");
		generateButton.addActionListener(parent);
		previousButton = new JButton("Previous");
		previousButton.setSize(100, 30);
		previousButton.setEnabled(false);
		previousButton.setActionCommand("previous");
		previousButton.addActionListener(parent);
		nextButton = new JButton("Next");
		nextButton.setSize(100, 30);
		nextButton.setActionCommand("next");
		nextButton.addActionListener(parent);

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
		this.add(generateButton);
		this.add(previousButton);
		this.add(nextButton);
		this.setMinimumSize(new Dimension((int) (0.2 * parent.getWidth()), parent.getHeight()));
	}

	public int getCapacity() {
		return ((Long) capacityField.getValue()).intValue();
	}

	public int getNodeCount() {
		return ((Long) nodeCountField.getValue()).intValue();
	}

	public void disableNext() {
		nextButton.setEnabled(false);
	}

	public void enablePrevious() {
		previousButton.setEnabled(true);
	}

	public void enableNext() {
		nextButton.setEnabled(true);
	}

	public void disablePrevious() {
		previousButton.setEnabled(false);
	}

}
