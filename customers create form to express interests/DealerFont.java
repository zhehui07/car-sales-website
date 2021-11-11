package group6;

import java.awt.*;

public class DealerFont {

    private static final String FONT_NAME = "Helvetica";
    private static final int TITLE_FONT_SIZE = 30;
    private static final int HEADER_FONT_SIZE = 20;
    private static final int TITLE_2_FONT_SIZE = 18;
    private static final int TITLE_3_FONT_SIZE = 16;
    private static final int NORMAL_FONT_SIZE = 14;
    private static final int DESCRIPTION_FONT_SIZE = 12;

    public static Font getHeaderFont() {
        return new Font(FONT_NAME, Font.PLAIN, HEADER_FONT_SIZE);
    }

    public static Font getTitleFont() {
        return new Font(FONT_NAME, Font.BOLD, TITLE_FONT_SIZE);
    }

    public static Font getSubTitleFont() {
        return new Font(FONT_NAME, Font.PLAIN, TITLE_2_FONT_SIZE);
    }

    public static Font getSubSubTitleFont() {
        return new Font(FONT_NAME, Font.PLAIN, TITLE_3_FONT_SIZE);
    }

    public static Font getDescriptionFont() {
        return new Font(FONT_NAME, Font.PLAIN, DESCRIPTION_FONT_SIZE);

    }

    public static Font getNormalFont() {
        return new Font(FONT_NAME, Font.PLAIN, NORMAL_FONT_SIZE);

    }
}
