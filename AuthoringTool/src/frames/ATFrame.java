package frames;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import constants.ATConstants;
import constants.ATConstants.ELabel;
import constants.ATConstants.ELightButtons;
import constants.ATConstants.EScentButtons;
import constants.ATConstants.EVibrationButtons;
import constants.ATConstants.EWindButtons;

public class ATFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private BufferedWriter out;
	private ATPanel videoPanel;
	
	private int pts;

	public ATFrame() {
		this.setSize(ATConstants.FRAME_WIDTH, ATConstants.FRAME_HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(null);
		
		this.videoPanel = new ATPanel();
		
		// XML 저장
		try {
			this.out = new BufferedWriter(new FileWriter(ATConstants.FILEPATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// 메뉴바 만들기
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu fileMenu = new JMenu("파일");
		JMenu editMenu = new JMenu("편집");
		menuBar.add(fileMenu);
        menuBar.add(editMenu);
        JMenuItem openAction = new JMenuItem("파일오픈");
        JMenuItem exitAction = new JMenuItem("종료");
        fileMenu.add(openAction);
        fileMenu.add(exitAction);
		
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
		
		ButtonGroup scentButtonGroup = new ButtonGroup();
		posX = ATConstants.BUTTON_POSX;
		posY = ATConstants.BUTTON_POSY + ATConstants.BUTTON_Y_SPACE * 3;
		for(EScentButtons eButton: EScentButtons.values()) {
			JRadioButton button = new JRadioButton(eButton.getName());
			this.add(button);
			button.setBounds(posX, posY, width, height);
			button.addActionListener(actionListener);
			button.setActionCommand(eButton.name());
			scentButtonGroup.add(button);
			posX += ATConstants.BUTTON_X_SPACE;
		}
		
		int i = 0;
		for(ELabel eLabel : ELabel.values()) {
			JLabel label = new JLabel(eLabel.getName());
			label.setBounds(ATConstants.LABEL_POSX, ATConstants.LABEL_POSY + ATConstants.LABEL_SPACE*i, ATConstants.LABEL_WIDTH, ATConstants.LABEL_HEIGHT);
			this.add(label);
			i++;
		}
		
		JButton xmlSaveButton = new JButton("Save");
		xmlSaveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				saveXML();
			}
		});
		xmlSaveButton.setBounds(800, 600, 100, 50);
		this.add(xmlSaveButton);
	}
	
	public void init() {
		this.setVisible(true);
		this.pts = 0;
		try {
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<SEM xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:mpeg:mpeg-v:2010:01-SEDL-NS\" xmlns:sev=\"urn:mpeg:mpeg-v:2010:01-SEV-NS\" xmlns:mpeg7=\"urn:mpeg:mpeg7:schema:2004\" xmlns:ct=\"urn:mpeg:mpeg-v:2010:01-CT-NS\" xmlns:si=\"urn:mpeg:mpeg21:2003:01-DIA-XSI-NS\" xsi:schemaLocation=\"urn:mpeg:mpeg-v:2010:01-SEV-NS MPEG-V-SEV.xsd\" si:timeScale=\"1\">\n\n");
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	private void write(String command) {
		try {
			if(command.equals("wind0")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("wind1")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"" + 4.0 + "\" intensity-range=\"0.00001 12.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("wind2")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"" + 8.0 + "\" intensity-range=\"0.00001 12.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("wind3")) {
				out.write("\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"" + 12.0 + "\" intensity-range=\"0.00001 12.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("noLight")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("red")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" color=\"" + "red" + "\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("blue")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" color=\"" + "blue" + "\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("green")) {
				out.write("\t<Effect xsi:type=\"sev:LightType\" color=\"" + "green" + "\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib0")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib1")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\"" + 10.0 + "\" intensity-range=\"0.00001 50.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib2")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\"" + 30.0 + "\" intensity-range=\"0.00001 50.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("vib3")) {
				out.write("\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\"" + 50.0 + "\" intensity-range=\"0.00001 50.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("noScent")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" active=\"false\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("popcorn")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("gunpowder")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			} else if(command.equals("flower")) {
				out.write("\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" active=\"true\" si:pts=\"" + pts + "\"/>\n");
			}
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	private void saveXML() {
		try {
			out.write("\n</SEM>");
			out.close();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}
	
	private class ActionHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			write(e.getActionCommand());
		}	
	}
}
