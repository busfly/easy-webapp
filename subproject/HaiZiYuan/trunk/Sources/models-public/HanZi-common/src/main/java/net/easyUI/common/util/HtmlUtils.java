package net.easyUI.common.util;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.easyUI.common.util.html.parse.HTMLParser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author fish
 * @version $Id: HtmlUtils.java,v 1.2 2009/05/22 06:20:07 fish Exp $
 */
public final class HtmlUtils {

	private static final Log logger = LogFactory.getLog(HtmlUtils.class);

	private static final Pattern scriptTag = Pattern
			.compile("(?i)(?s)(<script.+?</script>)|(on.+?=)");

	private static final String emptyString = "";

	/**
	 * 去掉禁止输入的html标签,比如说script等
	 * 
	 * @param s
	 * @return
	 */
	public static final String escapeForbiddenHtml(String s) {
		return escapeScriptTag(s);
	}

	/**
	 * 去掉禁止输入的script标签,以及 onclick这样的函数
	 * 
	 * @param s
	 * @return
	 */
	public static final String escapeScriptTag(String s) {
		if (s == null) {
			return null;
		}
		Matcher m = scriptTag.matcher(s);
		return m.replaceAll(emptyString);
	}

	/**
	 * 得到去除html标签后的文字
	 * 
	 * @param s
	 * @return
	 */
	public static final String parseHtml(String s) {
		if (s == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			Reader r = new StringReader(s);
			HTMLParser parser = new HTMLParser(r);
			LineNumberReader reader = new LineNumberReader(parser.getReader());
			for (String l = reader.readLine(); l != null; l = reader.readLine()) {
				sb.append(l);
			}
			r.close();
		} catch (IOException e) {
			logger.error("error than parse html string", e);
		}
		return sb.toString();
	}

	/**
	 * 得到去除html标签后的文字
	 * 
	 * @param s
	 * @param maxLength
	 *            最大长度,
	 * @return s.length <= maxLength
	 */
	public static final String parseHtml(String s, int maxLength) {
		if (maxLength <= 0) {
			logger.info("maxLength小于0,将得到去除html标签后的所有文字.");
			return parseHtml(s);
		}
		if (s == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		try {
			Reader r = new StringReader(s);
			HTMLParser parser = new HTMLParser(r);
			LineNumberReader reader = new LineNumberReader(parser.getReader());
			for (String l = reader.readLine(); l != null; l = reader.readLine()) {
				sb.append(l);
				if (sb.length() >= maxLength) {
					break;
				}
			}
			r.close();
		} catch (IOException e) {
			logger.error("error than parse html string", e);
		}
		if (sb.length() >= maxLength) {
			return sb.substring(0, maxLength);
		}
		return sb.toString();
	}

	/**
	 * 得到去除html标签后的文字
	 * 
	 * @param s
	 * @param maxLength
	 *            最大长度,
	 * @return s.length <= maxLength
	 */
	public static final String parseHtml(String s, String maxLength) {
		Integer maxLengthInt = -1;
		try {
			maxLengthInt = Integer.parseInt(maxLength);
		} catch (Exception e) {
			logger.error("", e);
		}
		return parseHtml(s, maxLengthInt);
	}
}
