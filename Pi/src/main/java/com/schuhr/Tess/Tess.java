package com.schuhr.Tess;

import org.bytedeco.javacpp.*;

import static org.bytedeco.javacpp.lept.*;
import static org.bytedeco.javacpp.tesseract.*;
import static org.junit.Assert.assertTrue;

public class Tess {

	private String filePath;
	public Tess(String fPath) {
		filePath = fPath;
	}

	public String Run() {
		BytePointer outText;

		TessBaseAPI api = new TessBaseAPI();

		if (api.Init(".", "ENG") != 0) {
			System.err.println("Could not initialize tesseract.");
			System.exit(1);
		}

		PIX image = pixRead(filePath);
		api.SetImage(image);

		outText = api.GetUTF8Text();
		String string = outText.getString();
		assertTrue(!string.isEmpty());
		System.out.println("OCR Output:\n" + string);

		api.End();
		outText.deallocate();
		pixDestroy(image);
		return string;
	}

}
