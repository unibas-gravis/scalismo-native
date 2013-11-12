package org.statismo.support.nativelibs.impl;

import java.net.URL;

public abstract class NativeLibraryDirectory {
	protected abstract String mapToResourceName(String baseName);
	
	static NativeLibraryDirectory instantiate(
			NativeLibraryBundle bundle, String platform) throws NativeLibraryException {
		String className = bundle.getClass().getPackage().getName() + "." + platform + ".NativeLibraryDirectory";
		try {
			Class<?> clazz = Class.forName(className);
			return (NativeLibraryDirectory) clazz.newInstance();
		} catch (Throwable t) {
			throw new NativeLibraryException("Unable to instantiate "+className, t);
		}
	}


	URL getResource(String name) {
		return this.getClass().getResource(name);
	}
}
