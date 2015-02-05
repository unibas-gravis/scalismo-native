package scalismo.support.nativelibs.vtk6.mac_x86_64;



public class NativeLibraryDirectory extends scalismo.support.nativelibs.impl.NativeLibraryDirectory {

	@Override
	protected String mapToResourceName(String baseName) {
		if (baseName.endsWith("-6.1")) {
			baseName = baseName+".1";
		}
		String name = System.mapLibraryName(baseName);
		return name.replace(".jnilib", ".dylib");
	}
}
