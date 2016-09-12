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

import java.net.URL;

public abstract class NativeLibraryDirectory {
	protected abstract String mapToResourceName(String baseName);
	
	static NativeLibraryDirectory instantiate(
			NativeLibraryBundle bundle, String platform) throws NativeLibraryException {
		String className = getClassNameFor(bundle, platform);
		try {
			Class<?> clazz = Class.forName(className);
			return (NativeLibraryDirectory) clazz.newInstance();
		} catch (Throwable t) {
			throw new NativeLibraryException("Unable to instantiate "+className, t);
		}
	}

	private static String getClassNameFor(NativeLibraryBundle bundle, String platform) {
		return bundle.getClass().getPackage().getName() + "." + platform + ".NativeLibraryDirectory";
	}

	static boolean exists(NativeLibraryBundle bundle, String platform) {
		String className = getClassNameFor(bundle, platform);
		try {
			Class.forName(className);
			return true;
		} catch (Throwable t) {
			return false;
		}
	}

	URL getResource(String name) {
		return this.getClass().getResource(name);
	}

}
