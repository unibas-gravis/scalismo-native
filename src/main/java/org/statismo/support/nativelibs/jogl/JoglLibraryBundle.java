package org.statismo.support.nativelibs.jogl;

import com.jogamp.common.jvm.JNILibLoaderBase;
import com.jogamp.common.os.Platform;
import com.jogamp.common.util.cache.TempJarCache;
import jogamp.common.Debug;
import org.statismo.support.nativelibs.impl.NativeLibraryBundle;
import org.statismo.support.nativelibs.impl.NativeLibraryException;
import org.statismo.support.nativelibs.impl.NativeLibraryInfo;
import vtk.vtkNativeLibrary;
import vtk.vtkPanel;

import javax.media.opengl.GLProfile;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;


public class JoglLibraryBundle extends NativeLibraryBundle {

	public String getName() {
		return "JOGL";
	}

	public String getVersion() {
		return "2.0.2";
	}

	@Override
	protected void getSupportedPlatformsInto(List<String> list) {
		list.add(PLATFORM_LINUX64);
		list.add(PLATFORM_LINUX32);
		list.add(PLATFORM_WIN64);
		list.add(PLATFORM_WIN32);
		list.add(PLATFORM_MAC64);
		return;
	}

	@Override
	protected void getLibraryNamesInto(List<String> list, String platform) {

		/*
		 * NOTE: the order IS important. Later libs may depend on earlier ones
		 * being loaded, and may fail if they haven't been loaded.
		 * 
		 * The easiest "algorithm" for getting the order right is manual, using
		 * trial and error: first, add everything in random order. Then try to
		 * initialize the system. Whenever a dependency is not met, an exception
		 * will be thrown. Example:
		 * 
		 * UnsatisfiedLinkError: libvtkGeovis.so: libvtkproj4.so.5.10: cannot
		 * open shared object file: No such file or directory
		 * 
		 * ^- Which means that vtkproj4 must be moved before vtkGeovis in the
		 * list. Repeat until everything loads properly.
		 */
		
		boolean linux = platform.equals(PLATFORM_LINUX64) || platform.equals(PLATFORM_LINUX32);
		boolean windows = platform.equals(PLATFORM_WIN64) || platform.equals(PLATFORM_WIN32);
		boolean mac = platform.equals(PLATFORM_MAC64);

        list.add("gluegen-rt");
        list.add("nativewindow_awt");
        if (linux) {
            list.add("nativewindow_x11");
        } else if (mac) {
            list.add("nativewindow_macosx");
        } else if (windows) {
            list.add("nativewindow_win32");
        }
        list.add("jogl_desktop");
        list.add("jogl_mobile");
        list.add("newt");
	}

	@Override
	protected void onInitializeStart() throws NativeLibraryException {
		// // Loads mawt.so
		Toolkit.getDefaultToolkit();
		// // Loads jawt.so - this is explicitly required in JRE 7
		try {
			System.loadLibrary("jawt");
		} catch (UnsatisfiedLinkError ignored) {}

        // silence warning messages
        System.setProperty("jogamp.gluegen.UseTempJarCache", "false");
	}

    @Override
    protected void onLibraryLoaded(NativeLibraryInfo info) {
        JNILibLoaderBase.addLoaded(info.getBaseName());
    }

    @Override
    protected void onInitializeEnd() throws NativeLibraryException {
        GLProfile.initSingleton();
    }

    @Override
	public Runnable getVerifierRunnable() {
		return new Runnable() {
			@Override
			public void run() {
                GLProfile.initSingleton();
			}
		};
	}

	@Override
	public boolean isLoadByDefault() {
		return true;
	}


}
