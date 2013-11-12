package org.statismo.support.nativelibs.jhdf5;

import java.util.List;

import org.statismo.support.nativelibs.impl.NativeLibraryBundle;
import org.statismo.support.nativelibs.impl.NativeLibraryException;
import org.statismo.support.nativelibs.impl.NativeLibraryInfo;

import ncsa.hdf.object.FileFormat;


public class JhdfLibraryBundle extends NativeLibraryBundle {

	public String getName() {
		return "JHDF";
	}

	public String getVersion() {
		return "5";
	}

	@Override
	protected void getSupportedPlatformsInto(List<String> list) {
		list.add(PLATFORM_LINUX64);
		list.add(PLATFORM_LINUX32);
		list.add(PLATFORM_WIN64);
		list.add(PLATFORM_WIN32);
		return;
	}

	@Override
	protected void getLibraryNamesInto(List<String> list, String platform) {

		list.add("jhdf5");

	}

	@Override
	public Runnable getVerifierRunnable() {
		return new Runnable() {
			
			@Override
			public void run() {
				FileFormat f = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
				if (f == null) {
					throw new IllegalStateException("didn't work, eh?");
				}
			}
		};
	}

	@Override
	protected boolean onLibraryExtracted(NativeLibraryInfo info)
			throws NativeLibraryException {
		if ("jhdf5".equals(info.getBaseName())) {
			System.setProperty("ncsa.hdf.hdf5lib.H5.hdf5lib", info.getTargetFile().getAbsolutePath());
			return false;
		}
		return super.onLibraryExtracted(info);
	}

	
	
}
