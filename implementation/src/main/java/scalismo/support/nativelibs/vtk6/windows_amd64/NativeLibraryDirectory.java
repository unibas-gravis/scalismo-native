package scalismo.support.nativelibs.vtk6.windows_amd64;



public class NativeLibraryDirectory extends scalismo.support.nativelibs.impl.NativeLibraryDirectory {

	@Override
	protected String mapToResourceName(String baseName) {
		return System.mapLibraryName(baseName);
	}

}
