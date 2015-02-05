/* Implementation note: this class is defined both in the stub and implementation projects,
 * which means that care must be taken to synchronize both in case of changes.
 */
package scalismo.support.nativelibs;

/**
 * Controls behavior on initialization. Stick to TERMINATE_ON_FAIL unless you have good
 * reasons to do otherwise.
 */
public enum InitializationMode {
    /**
     * If a bundle is not available on the target platform, print a stack trace
     * and terminate the program. This is the most strict, and recommended, argument.
     */
    TERMINATE_ON_FAIL,
    /**
     * Like TERMINATE_ON_FAIL, but in addition print the loaded bundles to
     * System.out. NOT RECOMMENDED for production.
     */
    TERMINATE_VERBOSE,
    /**
     * Throw an exception if a bundle is not available on the target
     * platform, or fails to initialize. This is less strict than TERMINATE_ON_FAIL,
     * and is recommended if you implement your own error handling.
     */
    THROW_EXCEPTION_ON_FAIL,
    /**
     * Print a warning to System.err if a bundle is not available on the
     * target platform, or fails to initialize properly. NOT RECOMMENDED.
     */
    WARN_ON_FAIL,
    /**
     * Like WARN_ON_FAIL, but in addition print the loaded bundles to
     * System.out. NOT RECOMMENDED for production.
     */
    WARN_VERBOSE,
    /**
     * Silently ignore bundles which are not available on the target
     * platform, or which fail to load properly. NOT RECOMMENDED, EVER.
     */
    SILENT,
}

