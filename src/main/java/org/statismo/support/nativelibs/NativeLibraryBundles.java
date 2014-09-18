package org.statismo.support.nativelibs;

import org.statismo.support.nativelibs.impl.CleanupFilesShutdownHook;
import org.statismo.support.nativelibs.impl.NativeLibraryBundle;
import org.statismo.support.nativelibs.impl.NativeLibraryException;
import org.statismo.support.nativelibs.impl.Util;
import org.statismo.support.nativelibs.jhdf5.JhdfLibraryBundle;
import org.statismo.support.nativelibs.jogl.JoglLibraryBundle;
import org.statismo.support.nativelibs.vtk6.Vtk6LibraryBundle;

import java.io.File;
import java.util.*;


public class NativeLibraryBundles {

    /**
     * Controls behavior on initialization. Note that fatal errors (i.e., when
     * something goes wrong while initializing an (explicitly or implicitly)
     * requested bundle will always throw an exception.
     */
    public static enum InitializationMode {
        /**
         * Silently ignore bundles which are not available on the target
         * platform.
         */
        SILENT,
        /**
         * Print a warning to System.err if a bundle is not available on the
         * target platform.
         */
        WARNONFAIL,
        /**
         * Throw an exception if a bundle is not available on the target
         * platform.
         */
        ABORTONFAIL,
        /**
         * Like WARNONFAIL, but in addition print the loaded bundles to
         * System.out.
         */
        VERBOSE,
    }

    private static final String _UNKNOWN_PLATFORM = "UNKNOWN";
    private static final Map<String, NativeLibraryBundle> _BUNDLES = setupBundles();

    private static File baseDirectory = null;

    private static Map<String, NativeLibraryBundle> setupBundles() {
        Map<String, NativeLibraryBundle> map = new LinkedHashMap<String, NativeLibraryBundle>();

        addBundle(map, new JhdfLibraryBundle());
        addBundle(map, new JoglLibraryBundle());
        addBundle(map, new Vtk6LibraryBundle());

        return map;
    }

    private static void addBundle(Map<String, NativeLibraryBundle> map,
                                  NativeLibraryBundle bundle) {
        map.put(bundle.getId(), bundle);
    }

    public static Collection<String> getAvailableBundleIds() {
        return Collections.unmodifiableCollection(_BUNDLES.keySet());
    }

    /**
     * Initialize native library bundles.
     *
     * @param mode      initialization mode
     * @param bundleIds the ids of the bundles that should be initialized. Note that
     *                  if no ids are specified at all, then ALL available bundles which are marked to automatically load
     *                  will be loaded.
     * @return the number of library bundles that have been freshly initialized.
     * @throws NativeLibraryException if anything goes wrong.
     */
    public static synchronized int initialize(InitializationMode mode,
                                              String... bundleIds) throws NativeLibraryException {

        Collection<String> bundles = Arrays.asList(bundleIds);
        boolean autoMode = false;
        if (bundles.isEmpty()) {
            bundles = getAvailableBundleIds();
            autoMode = true;
        }

        String platform = NativeLibraryBundle.getPlatform();

		/* On Linux, we sometimes have the dreaded "[xcb] Most likely this is a multi-threaded client and XInitThreads has not been called" crash,
		 * which occurs during shutdown. It's essentially harmless, but will clutter the /tmp directory over time. So if on Linux, we try to clean
		 * up immediately.
		 */
        boolean linux = platform.equals(NativeLibraryBundle.PLATFORM_LINUX64) || platform.equals(NativeLibraryBundle.PLATFORM_LINUX32);


        int loaded = 0;

        for (String id : bundles) {
            NativeLibraryBundle bundle = _BUNDLES.get(id);
            if (bundle == null) {
                throw new NativeLibraryException("Unknown bundle ID: " + id);
            }
            if (autoMode && !bundle.isLoadByDefault()) {
                continue;
            }
            if (!bundle.isPlatformSupported(platform)) {
                String msg = "Bundle " + bundle + " does not support platform "
                        + platform;
                switch (mode) {
                    case WARNONFAIL:
                    case VERBOSE:
                        System.err.println("WARNING: " + msg);
                    case SILENT:
                        continue;
                    case ABORTONFAIL:
                        throw new NativeLibraryException(msg);
                }
            }
            try {
                boolean verbose = mode == InitializationMode.VERBOSE;
                setupBaseDirectoryIfNeeded();
                if (verbose) {
                    System.out.println(bundle + ": initializing");
                }
                if (bundle.initialize(baseDirectory, platform)) {
                    ++loaded;
                    if (verbose) {
                        System.out.println(bundle + ": initialized.");
                    }
                    Runnable verify = bundle.getVerifierRunnable();
                    if (verify != null && (verbose || linux)) {
                        try {
                            verify.run();
                            if (verbose) {
                                System.out.println(bundle + ": verified, seems to work.");
                            }
                        } catch (Throwable t) {
                            System.err.println(bundle + ": failed verification, it probably does not work.");
                        }
                    }
                } else if (verbose) {
                    System.out.println(bundle
                            + " skipped, was already initialized");
                }
            } catch (NativeLibraryException ex) {
                throw ex;
            }
        }

        if (linux) {
            CleanupFilesShutdownHook.getInstance().run();
        }
        return loaded;

    }

    private static synchronized void setupBaseDirectoryIfNeeded()
            throws NativeLibraryException {
        if (baseDirectory == null) {
            baseDirectory = Util.createTemporaryDirectory("org_statismo_nativelibs",
                    null);
            CleanupFilesShutdownHook.getInstance().deleteOnExit(baseDirectory);
        }
    }

    public static void main(String[] args) {
        String platform = NativeLibraryBundle.getPlatform();
        System.out.println("Current platform: " + platform);
        if (_UNKNOWN_PLATFORM.equals(platform)) {
            exitWithError("Cannot determine the platform you are running on.");
        }

        printUsage();

        try {
            int ok = initialize(InitializationMode.VERBOSE, args);
            String plural = ok == 1 ? "bundle" : "bundles";
            System.out.println("Initialization OK, " + ok + " " + plural
                    + " initialized.");
        } catch (Throwable t) {
            System.err.println("Initialization failed with " + t.getClass().getSimpleName() + ", stacktrace follows.");
            t.printStackTrace(System.err);
            System.err.println("stacktrace above.");
            System.exit(1);
        }
    }

    private static void printUsage() {
        System.out
                .println("==========================================================================");
        System.out.println("The following library bundles are available (bundles marked with an");
        System.out.println("asterisk (*) are loaded by default unless specific bundles are requested):");
        // System.out.println("[ ID (Name Version): platforms ]");
        // System.out.println("-------------------------------------");
        for (Map.Entry<String, NativeLibraryBundle> entry : _BUNDLES.entrySet()) {
            StringBuilder sb = new StringBuilder(entry.getKey());
            NativeLibraryBundle bundle = entry.getValue();
            if (bundle.isLoadByDefault()) {
                sb.append("*");
            }
            sb.append(" (");
            sb.append(bundle.getName());
            sb.append(" ");
            sb.append(bundle.getVersion());
            sb.append("):");
            for (String platform : bundle.getSupportedPlatforms()) {
                sb.append(" ");
                sb.append(platform);
            }
            System.out.println(sb);
        }
        // System.out.println();
        // System.out.println("You can selectively initialize bundles by calling the "+ExternalLibrariesJar.class.getCanonicalName()+"#initialize() function with the bundles to initialize.");
        // System.out.println("Example: "+
        // ExternalLibrariesJar.class.getSimpleName()+".initialize(\"b1\",\"b2\");");
        // System.out.println("If no arguments are given, ALL available bundles will be initialized.");
        System.out
                .println("==========================================================================");
        System.out.println();
    }

    private static void exitWithError(String... msgs) {
        for (String msg : msgs) {
            System.err.println(msg);
        }
        System.exit(1);
    }
}
