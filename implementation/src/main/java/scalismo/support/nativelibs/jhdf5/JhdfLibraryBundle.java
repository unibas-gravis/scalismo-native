package scalismo.support.nativelibs.jhdf5;

import ncsa.hdf.object.FileFormat;
import scalismo.support.nativelibs.NativeLibraryException;
import scalismo.support.nativelibs.impl.NativeLibraryBundle;
import scalismo.support.nativelibs.impl.NativeLibraryInfo;
import scalismo.support.nativelibs.impl.Platform;

import java.util.List;


public class JhdfLibraryBundle extends NativeLibraryBundle {

    public String getName() {
        return "JHDF";
    }

    public String getVersion() {
        return "5";
    }

    @Override
    protected void getSupportedPlatformsInto(List<String> list) {
        list.add(Platform.PLATFORM_LINUX64);
        list.add(Platform.PLATFORM_WIN64);
        list.add(Platform.PLATFORM_WIN32);
        list.add(Platform.PLATFORM_MAC64);
    }

    @Override
    protected void getLibraryNamesInto(List<String> list) {

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
