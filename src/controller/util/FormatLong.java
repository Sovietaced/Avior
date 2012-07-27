package controller.util;

public class FormatLong {

	/* Formats longs for proper representation in the paint */
	public static String format(long paramLong) {
		if (paramLong >= 1000000000L) {
			return paramLong / 1000000000L + " GB";
		}
		if (paramLong >= 10000000L) {
			return paramLong / 1000000L + " MB";
		}
		if (paramLong >= 1000L) {
			return paramLong / 1000L + " KB";
		}
		return "" + paramLong;
	}
}
