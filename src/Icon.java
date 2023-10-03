import javax.swing.ImageIcon;
public class Icon{
    private static final String DIRECTORY = "icon/",
                                EXTENSION = ".png";

    public static final ImageIcon   GAMEOVER = get("GameOver"),
                                    GARBAGE = get("Garbage"),
                                    GARBAGE_ICON = get("GarbageIcon"),
                                    ICON = get("Icon"),
                                    MAP_1 = get("Map1"),
                                    NO_GARBAGE_ICON = get("NoGarbageIcon"),
                                    SHIELD = get("Shield"),
                                    STATUS_BAR = get("StatusBar"),
                                    TITLE_SCREEN = get("TitleScreen");
    public static final ImageIcon[] CHARACTER = {get("Character1"), get("Character2"), get("Character3"), get("Character4")},
                                    HOME = {get("Home1"), get("Home2"), get("Home3"), get("Home4")},
                                    PORTAL = {get("Portal1"), get("Portal2")},
                                    RULE = {get("Rule1"), get("Rule2")},
                                    RULE_BUTTONS = {get("Previous"), get("OK"), get("Next")},
                                    TITLE_BUTTONS = {get("StartButton"), get("RuleButton"), get("ExitButton")},
                                    TITLE_BUTTONS_ROLLOVER = {get("StartButtonRollOver"), get("RuleButtonRollOver"), get("ExitButtonRollOver")};
    private static ImageIcon get(String fileName){
        return new ImageIcon((Icon.class.getResource("").getPath() + "../" + DIRECTORY).replaceAll("\\\\", "/") + fileName + EXTENSION);
    }
}