package net.greenwoodmc.helpcommand.util.colour.patterns;

import net.greenwoodmc.helpcommand.util.colour.IridiumColorAPI;
import java.util.regex.Matcher;

public class SolidPattern implements Pattern {
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("<SOLID:([0-9A-Fa-f]{6})>|#\\{([0-9A-Fa-f]{6})}");

    public SolidPattern() {
    }

    public String process(String string) {
        String color;
        for(Matcher matcher = this.pattern.matcher(string); matcher.find(); string = string.replace(matcher.group(), IridiumColorAPI.getColor(color) + "")) {
            color = matcher.group(1);
            if (color == null) {
                color = matcher.group(2);
            }
        }

        return string;
    }
}
