package at.fundev.oobe.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

public class EncodingDetector {
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	public static EncodingDetector of(File file) {
		EncodingDetector det = new EncodingDetector(file);
		det.detectEncoding();
		
		return det;
	}
	
	private String encoding;
	
	private File file;

	private IOException ioException;
	
	private EncodingDetector(File file) {
		this.file = file;
		this.encoding = null;
	}
	
	private void detectEncoding() {
		CharsetDetector cd = new CharsetDetector();
		BufferedInputStream imp = null;
		
		try {
			imp = new BufferedInputStream(new FileInputStream(file));
			cd.setText(imp);
			CharsetMatch match = cd.detect();

			if(match != null) {
				encoding = match.getName();
			} else {
				encoding = DEFAULT_ENCODING;
			}
		} catch(IOException e) {
			ioException = e;
			encoding = null;
		}
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public IOException getIoException() {
		return ioException;
	}

}
