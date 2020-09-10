package eu.wauz.wauzcore.system.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.libs.org.apache.commons.lang3.text.WordUtils;

/**
 * An util class for stuff like unicode icons.
 * 
 * @author Wauzmons
 */
public class UnicodeUtils {
	
	/**
	 * A paragraph icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/00a7/index.htm">fileformat.info</a>
	 */
	public static final String ICON_PARAGRAPH = "\u00A7";
	
	/**
	 * A degree icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/00b0/index.htm">fileformat.info</a>
	 */
	public static final String ICON_DEGREES = "\u00B0";
	
	/**
	 * A caret icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/00bb/index.htm">fileformat.info</a>
	 */
	public static final String ICON_CARET = "\u00BB";
	
	/**
	 * A zero width space icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/200B/index.htm">fileformat.info</a>
	 */
	public static final String ICON_ZERO_WIDTH_SPACE = "\u200B";
	
	/**
	 * A bullet icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/2022/index.htm">fileformat.info</a>
	 */
	public static final String ICON_BULLET = "\u2022";
	
	/**
	 * A sun icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/2600/index.htm">fileformat.info</a>
	 */
	public static final String ICON_SUN = "\u2600";
	
	/**
	 * A diamond icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/2666/index.htm">fileformat.info</a>
	 */
	public static final String ICON_DIAMOND = "\u2666";
	
	/**
	 * A star icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/2b50/index.htm">fileformat.info</a>
	 */
	public static final String ICON_STAR = "\u2B50";
	
	/**
	 * A heart icon in unicode.
	 * <a href="https://www.fileformat.info/info/unicode/char/2764/index.htm">fileformat.info</a>
	 */
	public static final String ICON_HEART = "\u2764";
	
	/**
	 * Wraps the given text, to be better displayable in lore.
	 * 
	 * @param text The text to wrap.
	 * 
	 * @return The wrapped text.
	 */
	public static List<String> wrapText(String text) {
		String doubleParagraph = UnicodeUtils.ICON_PARAGRAPH + UnicodeUtils.ICON_PARAGRAPH;
		String[] textParts = WordUtils.wrap(text, 42, doubleParagraph, true).split(doubleParagraph);
		return Arrays.asList(textParts);
	}
	
	/**
	 * Creates a progress bar, with a percentage display.
	 * 
	 * @param progress The current value.
	 * @param goal The maximum value.
	 * @param lines The amount of vertical lines, the bar consists of.
	 * @param color The color of the bar.
	 * 
	 * @return The progress bar string.
	 */
	public static String createProgressBar(double progress, double goal, int lines, ChatColor color) {
		double precisePercantage = progress * 100.0 / goal;
		int barPercentage = (int) (progress * lines / goal);
		
		String progressBar = String.valueOf(color);
		for(int bar = 1; barPercentage > bar; bar++) {
			progressBar += "|";
		}
		progressBar += String.valueOf(ChatColor.GRAY);
		for(int bar = barPercentage; bar <= lines; bar++) {
			progressBar += "|";
		}
		return progressBar + " " + ChatColor.WHITE + ((int) precisePercantage) + "%";
	}

}
