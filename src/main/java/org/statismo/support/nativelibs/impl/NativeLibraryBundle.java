package org.statismo.support.nativelibs.impl;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class NativeLibraryBundle {
	
    /* caches initialization state. The field "exception" contains
     * the exception that occurred during initialization, or <tt>null</tt>
     * if the initialization was successful. If the initialization state
     * is not known at all (=when no initialization was attempted), then
     * the initializationResult <i>field</i> of the containing class
     * will be <tt>null</tt>
     */
    public static class InitializationResult {
        private final NativeLibraryException exception;
        public int refCount = 1;
        public boolean isSuccess() {
            return exception == null;
        }
        private InitializationResult() {
            this(null);
        }
        private InitializationResult(Throwable throwable) {
            exception = NativeLibraryException.wrap(throwable);
        }
        public NativeLibraryException getException() {
            return exception;
        }
    }
	
	private final List<String> _platforms;
	private final List<NativeLibraryInfo> _libraries;
	private InitializationResult _initializationResult = null;

	public abstract String getName();

	public abstract String getVersion();

	protected abstract void getLibraryNamesInto(List<String> list);

	protected abstract void getSupportedPlatformsInto(List<String> list);

	public boolean isLoadByDefault() {
		return true;
	}
	
	/**
	 * This callback method is called once for every Library bundle, before
	 * extracting the libraries.
	 * 
	 * @throws NativeLibraryException
	 *             if anything goes seriously wrong
	 */
	protected void onInitializeStart() throws NativeLibraryException {

	}

    /**
     * This callback method is called once for every library, right after it was
     * extracted to a file, but before it is (possibly) loaded as a library.
     * Whether an attempt to directly load it as a native library is determined
     * by the return value of this method (the default is <tt>true</tt> unless
     * overridden by subclasses).
     *
     * @param info
     *            a fully initialized {@link NativeLibraryInfo} instance containing all relevant information
     *            about the library which has just been extracted.
     * @return <tt>true</tt> if the library is to be loaded (using
     *         {@link Runtime#load(String)}), <tt>false</tt> if the library
     *         bundle will take care of the appropriate actions itself.
     * @throws NativeLibraryException
     */
    protected boolean onLibraryExtracted(NativeLibraryInfo info)
            throws NativeLibraryException {
        return true;
    }

    /**
     * This callback method is called once for every library, right after it was
     * loaded as a library.
     *
     * @param info
     *            a fully initialized {@link NativeLibraryInfo} instance containing all relevant information
     *            about the library which has just been extracted.
     */
    protected void onLibraryLoaded(NativeLibraryInfo info) {

    }

    /**
	 * This callback method is called once for every library bundle, after all
	 * libraries have been extracted.
	 * 
	 * @throws NativeLibraryException
	 *             if anything goes seriously wrong
	 */
	protected void onInitializeEnd() throws NativeLibraryException {

	}

	public Runnable getVerifierRunnable() {
		return null;
	}

	public final String getId() {
		String pkg = getClass().getPackage().getName();
		return pkg.substring(pkg.lastIndexOf('.') + 1);
	}

	public final List<String> getSupportedPlatforms() {
		return _platforms;
	}

	protected final List<NativeLibraryInfo> getLibraries() {
		return _libraries;
	}

	protected NativeLibraryBundle() {
		List<String> list = new ArrayList<String>();
		getSupportedPlatformsInto(list);
		Collections.sort(list);
		_platforms = Collections.unmodifiableList(list);

		list = new LinkedList<String>();
		getLibraryNamesInto(list);
		_libraries = Collections.unmodifiableList(instantiateInfoObjects(list));
	}

	private List<NativeLibraryInfo> instantiateInfoObjects(List<String> names) {
		List<NativeLibraryInfo> infos = new ArrayList<NativeLibraryInfo>(
				names.size());
		for (String name : names) {
			infos.add(new NativeLibraryInfo(name));
		}
		return infos;
	}

	public final boolean isPlatformSupported(String platform) {
		return Collections.binarySearch(_platforms, platform) >= 0;
	}

	public final String toString() {
		return getId() + " (" + getName() + " " + getVersion() + ")";
	}

	public final synchronized InitializationResult initialize(File baseDir) {
		if (_initializationResult != null) {
            _initializationResult.refCount += 1;
			return _initializationResult;
		}

		try {
			onInitializeStart();
            File target = Util.createTemporaryDirectory(getId(), baseDir);
            NativeLibraryDirectory source = NativeLibraryDirectory.instantiate(
                    this, Platform.getPlatform());

            determineUrls(source);
            createFiles(target);
            loadLibraries();
            onInitializeEnd();
            _initializationResult = new InitializationResult();
		} catch (Throwable error) {
			_initializationResult = new InitializationResult(error);
		}
        return _initializationResult;
	}

	private void determineUrls(NativeLibraryDirectory dir)
			throws NativeLibraryException {
		for (NativeLibraryInfo info : _libraries) {
			info.setNativeName(dir.mapToResourceName(info.getBaseName()));
			URL url = dir.getResource(info.getNativeName());
			if (url == null) {
				throw new NativeLibraryException("Unable to load resource "
						+ info.getNativeName() + " for platform " + Platform.getPlatform());
			}
			info.setSourceUrl(url);
		}
	}

	private void createFiles(File directory) throws NativeLibraryException {
		for (NativeLibraryInfo info : _libraries) {
			String name = info.getSourceUrl().getFile();
			File file = new File(directory, name.substring(
					name.lastIndexOf('/') + 1, name.length()));
			try {
				Util.copyUrlToFile(info.getSourceUrl(), file);
				info.setTargetFile(file);
			} catch (IOException io) {
				throw new NativeLibraryException("Unable to copy "
						+ info.getSourceUrl() + " to " + file, io);
			}
		}
	}

	private void loadLibraries() throws NativeLibraryException {
		for (NativeLibraryInfo info : _libraries) {
			try {
				if (onLibraryExtracted(info)) {
					String path = info.getTargetFile().getAbsolutePath();
					Runtime.getRuntime().load(path);
                    onLibraryLoaded(info);
				}
			} catch (Throwable t) {
				throw new NativeLibraryException(
						"Unable to load native library file "
								+ info.getTargetFile(), t);
			}
		}
	}

}
