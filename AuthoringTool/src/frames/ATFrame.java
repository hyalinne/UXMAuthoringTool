package frames;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import constants.ATConstants;
import constants.ATConstants.ELabel;
import constants.ATConstants.ELightButtons;
import constants.ATConstants.ESmellButtons;
import constants.ATConstants.EVibrationButtons;
import constants.ATConstants.EWindButtons;

public class ATFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public ATFrame() {
		this.setSize(ATConstants.FRAME_WIDTH, ATConstants.FRAME_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		//메뉴바 만들기
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("파일");
		JMenu editMenu = new JMenu("편집");
		menuBar.add(fileMenu);
        menuBar.add(editMenu);
        JMenuItem openAction = new JMenuItem("파일오픈");
        JMenuItem exitAction = new JMenuItem("종료");
        fileMenu.add(openAction);
        fileMenu.add(exitAction);
		this.add(menuBar);
		
		int posX = ATConstants.BUTTON_POSX, posY = ATConstants.BUTTON_POSY, width = ATConstants.BUTTON_WIDTH, height = ATConstants.BUTTON_HEIGHT;
		ActionHandler actionListener = new ActionHandler();
		ButtonGroup windButtonGroup = new ButtonGroup();
		for(EWindButtons eButton: EWindButtons.values()) {
			JRadioButton button = new JRadioButton(eButton.getName());
			this.add(button);
			button.setBounds(posX, posY, width, height);
			button.addActionListener(actionListener);
			button.setActionCommand(eButton.name());
			windButtonGroup.add(button);
			posX += ATConstants.BUTTON_X_SPACE;
		}
		
		ButtonGroup lightButtonGroup = new ButtonGroup();
		posX = ATConstants.BUTTON_POSX;
		posY = ATConstants.BUTTON_POSY + ATConstants.BUTTON_Y_SPACE * 1;
		for(ELightButtons eButton: ELightButtons.values()) {
			JRadioButton button = new JRadioButton(eButton.getName());
			this.add(button);
			button.setBounds(posX, posY, width, height);
			button.addActionListener(actionListener);
			button.setActionCommand(eButton.name());
			lightButtonGroup.add(button);
			posX += ATConstants.BUTTON_X_SPACE;
		}
		
		ButtonGroup vibrationButtonGroup = new ButtonGroup();
		posX = ATConstants.BUTTON_POSX;
		posY = ATConstants.BUTTON_POSY + ATConstants.BUTTON_Y_SPACE * 2;
		for(EVibrationButtons eButton: EVibrationButtons.values()) {
			JRadioButton button = new JRadioButton(eButton.getName());
			this.add(button);
			button.setBounds(posX, posY, width, height);
			button.addActionListener(actionListener);
			button.setActionCommand(eButton.name());
			vibrationButtonGroup.add(button);
			posX += ATConstants.BUTTON_X_SPACE;
		}
		
		ButtonGroup smellButtonGroup = new ButtonGroup();
		posX = ATConstants.BUTTON_POSX;
		posY = ATConstants.BUTTON_POSY + ATConstants.BUTTON_Y_SPACE * 3;
		for(ESmellButtons eButton: ESmellButtons.values()) {
			JRadioButton button = new JRadioButton(eButton.getName());
			this.add(button);
			button.setBounds(posX, posY, width, height);
			button.addActionListener(actionListener);
			button.setActionCommand(eButton.name());
			smellButtonGroup.add(button);
			posX += ATConstants.BUTTON_X_SPACE;
		}
		
		int i = 0;
		for(ELabel eLabel : ELabel.values()) {
			JLabel label = new JLabel(eLabel.getName());
			label.setBounds(ATConstants.LABEL_POSX, ATConstants.LABEL_POSY + ATConstants.LABEL_SPACE*i, ATConstants.LABEL_WIDTH, ATConstants.LABEL_HEIGHT);
			this.add(label);
			i++;
		}
		
	}
	
	public void init() {
		this.setVisible(true);
	}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(ATConstants.FILEPATH));
				out.write(e.getActionCommand().toString());
			} catch (IOException err) {
				err.printStackTrace();
			}
		}	
	}
}
