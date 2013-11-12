package org.statismo.support.nativelibs.jhdf5.windows_amd64;



public class NativeLibraryDirectory extends org.statismo.support.nativelibs.impl.NativeLibraryDirectory {

	@Override
	protected String mapToResourceName(String baseName) {
		return System.mapLibraryName(baseName);
	}

}
