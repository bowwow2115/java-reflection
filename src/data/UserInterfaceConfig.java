package data;

import java.util.Arrays;

public class UserInterfaceConfig {
    private String [] titleFonts;
    private long [] textSizes;
    private String titleColor;
    private String footerText;

    public String[] getTitleFonts() {
        return this.titleFonts;
    }

    public long[] getTextSize() {
        return this.textSizes;
    }

    public String getTitleColor() {
        return this.titleColor;
    }

    public long[] getTextSizes() {
        return this.textSizes;
    }

    public String getFooterText() {
        return this.footerText;
    }

    @Override
    public String toString() {
        return "UserInterfaceConfig{" +
                "titleFonts=" + Arrays.toString(titleFonts) +
                ", textSizes=" + Arrays.toString(textSizes) +
                ", titleColor='" + titleColor + '\'' +
                ", footerText='" + footerText + '\'' +
                '}';
    }
}
