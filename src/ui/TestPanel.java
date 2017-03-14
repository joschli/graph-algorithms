package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTable;

public class TestPanel extends JPanel implements ActionListener {

	JButton startTest;
	JRadioButton radioMenu;

	JTable result;

	public TestPanel() {
		startTest = new JButton("Start");
		this.add(startTest);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

	}
}
