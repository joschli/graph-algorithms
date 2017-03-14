package ui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuPanel extends JPanel {

	JFormattedTextField nodeCountField;
	JFormattedTextField capacityField;
	JFormattedTextField delayField;

	JComboBox<String> algorithmSelection;

	JButton backButton;
	JButton nextButton;
	JButton previousButton;
	JButton pauseButton;
	JButton playButton;
	JButton forwardButton;
	JButton backwardButton;
	JLabel stepLabel;

	JButton generateButton;
	JButton startButton;

	JPanel nodeCountPanel;
	JPanel delayPanel;

	private int currentStep = 0;
	private int maxSteps = 0;

	public MenuPanel(MainFrame parent) {
		generateButton = createButton("Generate", "generate", parent);
		startButton = createButton("Start", "start", parent);
		previousButton = createButton("<", "previous", parent);
		nextButton = createButton(">", "next", parent);
		playButton = createButton("|>", "play", parent);
		pauseButton = createButton("||", "pause", parent);
		backButton = createButton("back", "back", parent);
		forwardButton = createButton(">>>", "toEnd", parent);
		backwardButton = createButton("<<<", "toStart", parent);

		playButton.setVisible(false);
		previousButton.setVisible(false);
		backButton.setVisible(false);
		nextButton.setVisible(false);
		pauseButton.setVisible(false);
		pauseButton.setEnabled(false);
		forwardButton.setVisible(false);
		backwardButton.setVisible(false);
		backwardButton.setEnabled(false);

		String[] algorithms = { "zg", "EdmondsKarp", "FordFulkerson", "Dinic", "GoldbergTarjan" };

		algorithmSelection = new JComboBox<String>(algorithms);
		algorithmSelection.setSelectedIndex(0);
		algorithmSelection.addActionListener(parent);
		JLabel nodeCountLabel = new JLabel("Nodes:");
		nodeCountField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		nodeCountField.setValue(new Long(10));
		nodeCountField.setColumns(3);
		JLabel capacityLabel = new JLabel("Capacity:");
		capacityField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		capacityField.setValue(new Long(5));
		capacityField.setColumns(3);

		nodeCountPanel = new JPanel();
		nodeCountPanel.add(nodeCountLabel);
		nodeCountPanel.add(nodeCountField);
		nodeCountPanel.add(capacityLabel);
		nodeCountPanel.add(capacityField);

		JLabel delayLabel = new JLabel("Delay in sec:");
		delayField = new JFormattedTextField(NumberFormat.getIntegerInstance());
		delayField.setValue(new Long(3));
		delayField.setColumns(3);

		delayPanel = new JPanel();
		delayPanel.add(delayLabel);
		delayPanel.add(delayField);
		delayPanel.setVisible(false);

		stepLabel = new JLabel(this.currentStep + "/" + this.maxSteps);
		stepLabel.setVisible(false);

		this.add(nodeCountPanel);
		this.add(generateButton);
		this.add(algorithmSelection);
		this.add(startButton);

		this.add(backButton);
		this.add(delayPanel);
		this.add(backwardButton);
		this.add(previousButton);
		this.add(pauseButton);
		this.add(playButton);
		this.add(nextButton);
		this.add(forwardButton);
		this.add(stepLabel);

		this.setMinimumSize(new Dimension((int) (0.2 * parent.getWidth()), parent.getHeight()));
	}

	private JButton createButton(String label, String command, ActionListener actionListener) {
		JButton button = new JButton(label);
		button.setSize(100, 30);
		button.setActionCommand(command);
		button.addActionListener(actionListener);
		return button;
	}

	public int getCapacity() {
		return ((Long) capacityField.getValue()).intValue();
	}

	public int getNodeCount() {
		return ((Long) nodeCountField.getValue()).intValue();
	}

	public int getDelay() {
		return ((Long) delayField.getValue()).intValue();
	}

	public void disableNext() {
		nextButton.setEnabled(false);
		forwardButton.setEnabled(false);
	}

	public void enablePrevious() {
		previousButton.setEnabled(true);
		backwardButton.setEnabled(true);
	}

	public void enableNext() {
		nextButton.setEnabled(true);
		forwardButton.setEnabled(true);
	}

	public void disablePrevious() {
		previousButton.setEnabled(false);
		backwardButton.setEnabled(false);
	}

	public void start() {
		generateButton.setVisible(false);
		startButton.setVisible(false);
		nodeCountPanel.setVisible(false);
		playButton.setVisible(true);
		previousButton.setVisible(true);
		nextButton.setVisible(true);
		algorithmSelection.setVisible(false);
		delayPanel.setVisible(true);
		pauseButton.setVisible(true);
		backButton.setVisible(true);
		stepLabel.setVisible(true);
		backwardButton.setVisible(true);
		forwardButton.setVisible(true);
	}

	public void restart() {
		generateButton.setVisible(true);
		startButton.setVisible(true);
		nodeCountPanel.setVisible(true);
		playButton.setVisible(false);
		previousButton.setVisible(false);
		nextButton.setVisible(false);
		algorithmSelection.setVisible(true);
		delayPanel.setVisible(false);
		pauseButton.setVisible(false);
		backButton.setVisible(false);
		stepLabel.setVisible(false);
		backwardButton.setVisible(false);
		forwardButton.setVisible(false);
	}

	public void disableStart() {
		playButton.setEnabled(false);
	}

	public void enableStart() {
		playButton.setEnabled(true);
	}

	public void disablePause() {
		pauseButton.setEnabled(false);
	}

	public void enablePause() {
		pauseButton.setEnabled(true);
	}

	public String getAlgorithm() {
		return (String) algorithmSelection.getSelectedItem();
	}

	public int getCurrentStep() {
		return currentStep;
	}

	public void setCurrentStep(int currentStep) {
		this.currentStep = currentStep;
		updateStepLabel();
	}

	private void updateStepLabel() {
		this.stepLabel.setText(this.currentStep + "/" + this.maxSteps);
	}

	public int getMaxSteps() {
		return maxSteps;
	}

	public void setMaxSteps(int maxSteps) {
		this.maxSteps = maxSteps;
		updateStepLabel();
	}

}
