package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModeSelection extends JPanel {

	JComboBox<String> mode;
	JButton quit;
	JButton start;
	JLabel label = new JLabel("graph");
	JLabel label2 = new JLabel("algorithms");

	public ModeSelection(MainFrame parent) {
		String[] modes = { "run", "test" };
		JPanel panel = new JPanel();
		JPanel panel2 = new JPanel();
		JPanel panel3 = new JPanel();
		mode = new JComboBox<>(modes);
		start = new JButton("Start");
		quit = new JButton("Quit");
		quit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});
		mode.setSize(new Dimension(300, 50));
		panel.setLayout(new FlowLayout());
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		start.setActionCommand("mode");
		start.addActionListener(parent);
		start.setMaximumSize(new Dimension(100, 50));
		quit.setMaximumSize(new Dimension(100, 50));
		mode.setMaximumSize(new Dimension(100, 30));
		panel.setMaximumSize(new Dimension(800, 50));
		panel3.add(label);
		panel3.add(label2);
		panel.add(mode);
		panel2.add(start);
		panel2.add(quit);
		this.add(panel3);
		this.add(panel);
		this.add(panel2);
	}

	public String getMode() {
		return (String) this.mode.getSelectedItem();
	}

}
