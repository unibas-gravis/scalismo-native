package org.statismo.support.nativelibs.vtk6.mac_x86_64;



public class NativeLibraryDirectory extends org.statismo.support.nativelibs.impl.NativeLibraryDirectory {

	@Override
	protected String mapToResourceName(String baseName) {
		if (baseName.endsWith("-6.0")) {
			baseName = baseName+".1";
		}
		String name = System.mapLibraryName(baseName);
		return name.replace(".jnilib", ".dylib");
	}
}
