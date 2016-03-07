package constants;

public class ATConstants {

	public static int FRAME_WIDTH = 1200;
	public static int FRAME_HEIGHT = 800;
	
	public static int BUTTON_WIDTH = 90;
	public static int BUTTON_HEIGHT = 50;
	public static int BUTTON_POSX = 800;
	public static int BUTTON_POSY = 150;
	public static int BUTTON_X_SPACE = 100;
	public static int BUTTON_Y_SPACE = 80;
	
	public static int LABEL_WIDTH = 80;
	public static int LABEL_HEIGHT = 40;
	public static int LABEL_POSX = 800;
	public static int LABEL_POSY = 120;
	public static int LABEL_SPACE = 80;
	
	public static String FILEPATH = "./workspace/SEM.xml";
	
	public static String STARTTAG = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n<SEM xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"urn:mpeg:mpeg-v:2010:01-SEDL-NS\" xmlns:sev=\"urn:mpeg:mpeg-v:2010:01-SEV-NS\" xmlns:mpeg7=\"urn:mpeg:mpeg7:schema:2004\" xmlns:ct=\"urn:mpeg:mpeg-v:2010:01-CT-NS\" xmlns:si=\"urn:mpeg:mpeg21:2003:01-DIA-XSI-NS\" xsi:schemaLocation=\"urn:mpeg:mpeg-v:2010:01-SEV-NS MPEG-V-SEV.xsd\" si:timeScale=\"1\">\n\n";
	public static String ENDTAG = "\n</SEM>";
	
	// Toggle Button	
	public static String CLOSE = "\"/>\n";
	public static String WIND_FALSE = "\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"12.0\" intensity-range=\"0.00001 12.0\" activate=\"false\" si:pts=\"";
	public static String WIND_TRUE = "\t<Effect xsi:type=\"sev:WindType\" intensity-value=\"12.0\" intensity-range=\"0.00001 12.0\" activate=\"true\" si:pts=\"";
	
	public static String LIGHT_FALSE = "\t<Effect xsi:type=\"sev:LightType\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" activate=\"false\" si:pts=\"";
	public static String LIGHT_RED = "\t<Effect xsi:type=\"sev:LightType\" color=\"red\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" activate=\"true\" si:pts=\"";
	public static String LIGHT_BLUE = "\t<Effect xsi:type=\"sev:LightType\" color=\"blue\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" activate=\"true\" si:pts=\"";
	public static String LIGHT_GREEN = "\t<Effect xsi:type=\"sev:LightType\" color=\"green\" intensity-value=\"16000.0\" intensity-range=\"0.00001 32000.0\" activate=\"true\" si:pts=\"";
	
	public static String VIB_FALSE = "\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\" 10.0 \" intensity-range=\"0.00001 50.0\" activate=\"false\" si:pts=\"";
	public static String VIB_TRUE = "\t<Effect xsi:type=\"sev:VibrationType\" intensity-value=\" 10.0 \" intensity-range=\"0.00001 50.0\" activate=\"true\" si:pts=\"";
	
	public static String SCENT_FALSE = "\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" activate=\"false\" si:pts=\"";
	public static String SCENT_TRUE = "\t<Effect xsi:type=\"sev:ScentType\" intensity-value=\"100.0\" intensity-range=\"0.00001 100.0\" activate=\"true\" si:pts=\"";
			
	
	public static String STOPWIND = "wind0";
	public static String STARTWIND = "wind1";
	
	public static String STOPVIB = "vib0";
	public static String STARTVIB = "vib1";
	
	public static String STOPSCENT = "scent0";
	public static String STARTSCENT = "scent1";
	
	public static enum ELightButtons {
		red("RED"),
		blue("BLUE"),
		green("GREEN"),
		noLight("||");
		
		private String name;
		private ELightButtons(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
	
	
}
