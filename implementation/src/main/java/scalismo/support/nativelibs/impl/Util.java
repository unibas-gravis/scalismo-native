/*
 * Copyright 2016 University of Basel, Graphics and Vision Research Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scalismo.support.nativelibs.impl;

import scalismo.support.nativelibs.NativeLibraryException;

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
