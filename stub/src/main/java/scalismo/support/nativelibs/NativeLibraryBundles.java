package scalismo.support.nativelibs;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NativeLibraryBundles {

    /**
     * Since we're using semantic versioning, any implementation
     * with a different major version is considered incompatible, whereas
     * a higher minor version is downward-compatible. In other words:
     * A stub version 2.3 will accept an implementation with version 2.5,
     * but neither version 2.2 nor 1.x or 3.x would be accepted.
     */
    public static final int MAJOR_VERSION = 3;
    public static final int MINOR_VERSION = 0;

    private static final String IMPLEMENTATION_CLASS_NAME = NativeLibraryBundles.class.getPackage().getName() + ".NativeLibraryBundlesImplementation";
    private static final String IMPLEMENTATION_MAJOR_VERSION_FIELDNAME = "MAJOR_VERSION";
    private static final String IMPLEMENTATION_MINOR_VERSION_FIELDNAME = "MINOR_VERSION";

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
        if (instance == null) {
            findAndSetInstance();
        }
        // by now, we have either thrown an exeption, or instance is set.
        return delegateInitializeCall(instance, mode, bundleIds);
    }

    private static class Instance {
        private Object object;
        private Method method;
    }

    private static Instance instance = null;

    private static NativeLibraryException noInstanceFound() {
        StringBuffer s = new StringBuffer("\n\n");
        s.append("==============================================\n");
        s.append("scalismo-native-stub: no implementation found!\n");
        s.append("==============================================\n\n");
        s.append("The native libraries required for scalismo to work\n");
        s.append("were not found in the classpath.\n\n");
        s.append("Please provide a jar file that contains the implementation.\n");
        s.append("The implementation is packaged separately from the interface,\n");
        s.append("because this helps to avoid dependency conflicts, and allows\n");
        s.append("you to decide which Operating Systems should be supported.\n");
        s.append("There are multiple implementation versions, each named after\n");
        s.append("the Operating Systems they support:\n\n");
        s.append("scalismo-native-linux64 - Linux (64-bit)\n");
        s.append("scalismo-native-mac64   - MacOS X (Intel 64-bit)\n");
        s.append("scalismo-native-win64   - MS Windows (64-bit)\n");
        s.append("scalismo-native-win32   - MS Windows (32-bit)\n");
        s.append("scalismo-native-win     - MS Windows (both 32 and 64 bit)\n");
        s.append("scalismo-native-all     - All of the above\n\n");
        s.append("Naturally, the last package is the largest, so there is\n");
        s.append("a tradeoff between size and \"universality\". The decision is yours.\n");
        appendGeneralHelp(s);
        return new NativeLibraryException(s.toString());
    }

    private static void appendGeneralHelp(StringBuffer s) {
        String v = MAJOR_VERSION + "." + MINOR_VERSION + ".";
        s.append("\n\nIf you are using sbt to manage dependencies,\n");
        s.append("put a line similar to:\n\n");
        s.append("    libraryDependencies += \"ch.unibas.cs.gravis\" % \"scalismo-native-all\" % \"" + v + "+\"\n\n");
        s.append("in your build.sbt or Build.scala file.\n");
        s.append("For general help about managing sbt dependencies, please\n");
        s.append("see the sbt documentation.\n\n");
        s.append("If you are not using sbt, download and add this file to your classpath:\n\n");
        s.append("    http://statismo.cs.unibas.ch/repository/public/ch/unibas/cs/gravis/scalismo-native-all/" + v + "0/scalismo-native-all-" + v + "0.jar\n\n");
        s.append("Note that the actual location or file name might be slightly different, but you'll figure it out no doubt. ;-)\n");

        s.append("Reminder: this version of scalismo-native-stub will accept any implementation with a version that is\n");
        s.append("larger than or equal to " + MAJOR_VERSION + "." + MINOR_VERSION + ".0, and strictly smaller than " + (MAJOR_VERSION + 1) + ".0.0.\n");
        s.append("\nGood luck! :-)\n\n");
    }

    private static NativeLibraryException wrongApiVersion(int implMajor, int implMinor) {
        StringBuffer s = new StringBuffer("\n\n");
        s.append("==========================================================\n");
        s.append("scalismo-native-stub: incompatible implementation version!\n");
        s.append("==========================================================\n\n");
        s.append("An implementation of the statismo-native interface\n");
        s.append("was found, but it is deemed to be incompatible.\n\n");
        s.append("Stub (interface) version: " + MAJOR_VERSION + "." + MINOR_VERSION + ".x\n");
        s.append("Implementation   version: " + implMajor + "." + implMinor + ".y\n\n");

        if (implMajor > MAJOR_VERSION) {
            s.append("The implementation is too new!\n");
            s.append("Please downgrade the implementation to a version that is\n");
            s.append("larger than or equal to " + MAJOR_VERSION + "." + MINOR_VERSION + ".0, and strictly smaller\n");
            s.append("than " + (MAJOR_VERSION + 1) + ".0.0.\n");
        } else {
            s.append("The implementation is too old!\n");
            s.append("Please upgrade the implementation to a version that is\n");
            s.append("larger than or equal to " + MAJOR_VERSION + "." + MINOR_VERSION + ".0, and strictly smaller\n");
            s.append("than " + (MAJOR_VERSION + 1) + ".0.0.\n");
        }
        appendGeneralHelp(s);
        return new NativeLibraryException(s.toString());
    }

    private static void findAndSetInstance() throws NativeLibraryException {
        Class<?> clazz;
        Method initMethod;
        Object object;
        int majorVersion = -1;
        int minorVersion = -1;
        try {
            clazz = Class.forName(IMPLEMENTATION_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw noInstanceFound();
        }
        try {
            initMethod = clazz.getMethod("initialize", InitializationMode.class, String[].class);
            Field majorField = clazz.getField(IMPLEMENTATION_MAJOR_VERSION_FIELDNAME);
            Field minorField = clazz.getField(IMPLEMENTATION_MINOR_VERSION_FIELDNAME);
            object = clazz.newInstance();
            majorVersion = majorField.getInt(object);
            minorVersion = minorField.getInt(object);
        } catch (Throwable t) {
            throw new NativeLibraryException("Unexpected Throwable:", t);
        }

        if ((majorVersion != MAJOR_VERSION || minorVersion < MINOR_VERSION)) {
            throw wrongApiVersion(majorVersion, minorVersion);
        }

        instance = new Instance();
        instance.object = object;
        instance.method = initMethod;
    }

    private static int delegateInitializeCall(Instance delegate, InitializationMode mode, String[] bundleIds) throws NativeLibraryException {
        try {
            return (Integer) delegate.method.invoke(delegate.object, mode, bundleIds);
        } catch (InvocationTargetException inv) {
            throw NativeLibraryException.wrap(inv.getCause());
        } catch (Throwable t) {
            throw new NativeLibraryException("Unexpected runtime exception", t);
        }
    }
}
