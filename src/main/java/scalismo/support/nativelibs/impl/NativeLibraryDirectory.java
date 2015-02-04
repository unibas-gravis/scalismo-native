package scalismo.support.nativelibs.impl;

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
