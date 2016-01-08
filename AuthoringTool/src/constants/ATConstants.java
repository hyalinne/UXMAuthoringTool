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
	
	public static String FILEPATH = "./workspace/file.ini";
	// Button Group
	public static enum ELabel {
		wind("바람"),
		light("빛"),
		vibration("진동"),
		smell("향기");
		
		private String name;
		private ELabel(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name;}
	}
	public static enum EWindButtons {
		noEffect("없음"),
		first("1"),
		second("2"),
		third("3");
		
		private String name;
		private EWindButtons(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
	
	public static enum ELightButtons {
		noEffect("없음"),
		red("빨강"),
		blue("파랑"),
		green("초록");
		
		private String name;
		private ELightButtons(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
	
	public static enum EVibrationButtons {
		noEffect("없음"),
		first("1"),
		second("2"),
		third("3");
		
		private String name;
		private EVibrationButtons(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
	
	public static enum ESmellButtons {
		noEffect("없음"),
		popCorn("팝콘"),
		gunPower("화약"),
		flower("꽃");
		
		private String name;
		private ESmellButtons(String name) {
			this.name = name;
		}
		
		public String getName() { return this.name; }
	}
}
