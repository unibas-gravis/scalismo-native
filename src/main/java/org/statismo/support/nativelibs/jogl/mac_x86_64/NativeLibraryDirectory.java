package org.statismo.support.nativelibs.jogl.mac_x86_64;



public class NativeLibraryDirectory extends org.statismo.support.nativelibs.impl.NativeLibraryDirectory {

	@Override
	protected String mapToResourceName(String baseName) {
		String name = System.mapLibraryName(baseName);
		return name.replace(".jnilib", ".dylib");
	}
}
