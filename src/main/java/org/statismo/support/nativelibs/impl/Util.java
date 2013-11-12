package org.statismo.support.nativelibs.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class Util {
	private Util() {}
	
	public static File createTemporaryDirectory(String prefix, File parent) throws NativeLibraryException {

		Throwable error = null;
		int tries = 5;

		for (int i = 0; i < tries; ++i) {
			try {
				File dir = File.createTempFile(prefix + "-", null, parent);
				if (!dir.exists() || dir.delete()) {
					if (dir.mkdir()) {
						return dir;
					}
				}
			} catch (Throwable t) {
				error = t;
			}
		}
		throw new NativeLibraryException(
				"Unable to create temporary directory, giving up after "
						+ tries + " tries", error);
	}


	public static void copyUrlToFile(URL url, File file) throws IOException {
		BufferedInputStream is = new BufferedInputStream(url.openStream());
		BufferedOutputStream os = new BufferedOutputStream(
				new FileOutputStream(file));

		byte[] buffer = new byte[4096 * 512];
		for (int read = is.read(buffer); read >= 0; read = is.read(buffer)) {
			os.write(buffer, 0, read);
		}
		is.close();
		os.close();
	}


}
