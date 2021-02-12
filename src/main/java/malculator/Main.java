package malculator;

import malculator.ui.UI;

public class Main {
    public static void main(String[] args) {
        UI.setup();
        UI.loop();
        UI.shutdown();
    }
}
