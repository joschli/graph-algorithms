package ui;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class ModeSelection extends JPanel {

	JComboBox<String> mode;
	JButton start;

	public ModeSelection(MainFrame parent) {
		String[] modes = { "run", "test" };
		mode = new JComboBox<>(modes);
		start = new JButton("Start");
		start.setActionCommand("mode");
		start.addActionListener(parent);
		this.add(mode);
		this.add(start);
	}

	public String getMode() {
		return (String) this.mode.getSelectedItem();
	}

}
