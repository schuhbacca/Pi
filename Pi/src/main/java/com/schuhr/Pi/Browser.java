package com.schuhr.Pi;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Browser {

	public Browser() {

	}

	public boolean IsBrowserSupported() {
		if (Desktop.isDesktopSupported()) {
			return true;
		}
		return false;
	}

	public void SearchWebOnWord(String word) {
		Document doc;
		Elements links = null;
		try {
			doc = Jsoup.connect("http://google.com/search?q=" + String.format("%s&start=0", word)).userAgent("Mozilla")
					.get();
			links = doc.select("a[href]");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String link = null;
		boolean found = false;
		for (Element l : links) {
			String testLink = ((String) l.attr("abs:href"));
			if (testLink.contains(String.format("http://www.google.com/search?q=%s&ie=UTF-8&prmd=", word)) == true
					&& testLink.contains("&start=10") && !found) {
				link = testLink.replace("&start=10", "&start=0");
				System.out.println("Found it!");
				found = true;
			}
		}

		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void SearchWebOnWordWithRecipe(String word) {
		String recipe = "recipe+with+";
		Document doc;
		Elements links = null;
		try {
			doc = Jsoup.connect("http://google.com/search?q=" + String.format("%s%s&start=0", recipe, word))
					.userAgent("Mozilla").get();
			links = doc.select("a[href]");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String link = null;
		boolean found = false;
		for (Element l : links) {
			String testLink = ((String) l.attr("abs:href"));
			if (testLink.contains(
					String.format("http://www.google.com/search?q=%s%s&ie=UTF-8&prmd=", recipe, word)) == true
					&& testLink.contains("&start=10") && !found) {
				link = testLink.replace("&start=10", "&start=0");
				System.out.println("Found it!");
				found = true;
			}
		}

		try {
			Desktop.getDesktop().browse(new URI(link));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
