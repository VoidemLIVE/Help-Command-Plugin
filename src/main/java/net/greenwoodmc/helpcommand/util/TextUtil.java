package net.greenwoodmc.helpcommand.util;

import net.greenwoodmc.helpcommand.util.colour.IridiumColorAPI;
import org.bukkit.Color;

public class TextUtil {
    public TextUtil() {
    }

    public static String color(String string) {
        return IridiumColorAPI.process(string);
    }

    public static Color getColor(String s) {
        String var1 = s.toUpperCase();
        byte var2 = -1;
        switch(var1.hashCode()) {
            case -2027972496:
                if (var1.equals("MAROON")) {
                    var2 = 7;
                }
                break;
            case -1955522002:
                if (var1.equals("ORANGE")) {
                    var2 = 10;
                }
                break;
            case -1923613764:
                if (var1.equals("PURPLE")) {
                    var2 = 11;
                }
                break;
            case -1848981747:
                if (var1.equals("SILVER")) {
                    var2 = 13;
                }
                break;
            case -1680910220:
                if (var1.equals("YELLOW")) {
                    var2 = 16;
                }
                break;
            case 81009:
                if (var1.equals("RED")) {
                    var2 = 12;
                }
                break;
            case 2016956:
                if (var1.equals("AQUA")) {
                    var2 = 0;
                }
                break;
            case 2041946:
                if (var1.equals("BLUE")) {
                    var2 = 2;
                }
                break;
            case 2196067:
                if (var1.equals("GRAY")) {
                    var2 = 4;
                }
                break;
            case 2336725:
                if (var1.equals("LIME")) {
                    var2 = 6;
                }
                break;
            case 2388918:
                if (var1.equals("NAVY")) {
                    var2 = 8;
                }
                break;
            case 2570844:
                if (var1.equals("TEAL")) {
                    var2 = 14;
                }
                break;
            case 63281119:
                if (var1.equals("BLACK")) {
                    var2 = 1;
                }
                break;
            case 68081379:
                if (var1.equals("GREEN")) {
                    var2 = 5;
                }
                break;
            case 75295163:
                if (var1.equals("OLIVE")) {
                    var2 = 9;
                }
                break;
            case 82564105:
                if (var1.equals("WHITE")) {
                    var2 = 15;
                }
                break;
            case 198329015:
                if (var1.equals("FUCHSIA")) {
                    var2 = 3;
                }
        }

        switch(var2) {
            case 0:
                return Color.AQUA;
            case 1:
                return Color.BLACK;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.FUCHSIA;
            case 4:
                return Color.GRAY;
            case 5:
                return Color.GREEN;
            case 6:
                return Color.LIME;
            case 7:
                return Color.MAROON;
            case 8:
                return Color.NAVY;
            case 9:
                return Color.OLIVE;
            case 10:
                return Color.ORANGE;
            case 11:
                return Color.PURPLE;
            case 12:
                return Color.RED;
            case 13:
                return Color.SILVER;
            case 14:
                return Color.TEAL;
            case 15:
                return Color.WHITE;
            case 16:
                return Color.YELLOW;
            default:
                return null;
        }
    }
}