package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import test.Test;
import test.TestFactory;

public class TestPanel extends JPanel implements ActionListener {

	JButton startTest;
	JComboBox<String> menu;
	JButton openFile;
	MainFrame parent;
	JTextArea result;
	final JFileChooser fc = new JFileChooser("./logs");

	public TestPanel(MainFrame parent) {
		String[] testsets = { "verysmall", "small", "medium", "large" };
		menu = new JComboBox<>(testsets);
		startTest = new JButton("Start");
		openFile = new JButton("Open log");
		openFile.addActionListener(this);
		openFile.setActionCommand("openfile");
		startTest.addActionListener(this);
		result = new JTextArea("");
		result.setEditable(false);
		result.setBorder(null);
		JScrollPane pane = new JScrollPane(result);
		pane.setPreferredSize(new Dimension(800, 600));
		this.add(startTest);
		this.add(menu);
		this.add(openFile);
		this.add(pane);
		this.parent = parent;

		parent.packAndCenterFrame();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("openfile")) {
			int returnVal = fc.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				openLog(file.toPath());
			} else {
			}
			return;
		}
		result.setText("");
		String testset = (String) menu.getSelectedItem();
		result.setText("Waiting on results...");
		parent.packAndCenterFrame();
		runTest(testset);
	}

	private void runTest(String testset) {
		Test test = TestFactory.createVerySmallTestCase();
		switch (testset) {
		case "small":
			test = TestFactory.createSmallTestCase();
			break;
		case "medium":
			test = TestFactory.createMediumTestCase();
			break;
		case "large":
			test = TestFactory.createBigTestCase();
			break;
		default:
			test = TestFactory.createVerySmallTestCase();
			break;
		}
		String file = "test" + dateString(new Date()) + ".txt";

		test.run(file);
		openLog(Paths.get("./logs/" + file));
	}

	private void openLog(Path file) {
		try {
			String s = Files.readAllLines(file).stream().reduce("", (a, b) -> a + "\n" + b);
			Color background = Color.green;
			if (s.contains("NOT VALID") || s.contains("FAILURE")) {
				background = Color.red;
			}
			result.setText("Test saved in: " + file + "\n" + s);
			result.setBackground(background);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String dateString(Date date) {
		DateFormat df = new SimpleDateFormat("ddMMyyyy-HHmm");
		return df.format(new Date(System.currentTimeMillis()));
	}
}
