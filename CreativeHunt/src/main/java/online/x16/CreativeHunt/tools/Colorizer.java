package online.x16.CreativeHunt.tools;

public class Colorizer {

	public static String colorize(String str) {
		return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
	}
}
