package scalismo.support.nativelibs.jogl.troubleshoot;

import scalismo.support.nativelibs.InitializationMode;
import scalismo.support.nativelibs.NativeLibraryBundles;
import scalismo.support.nativelibs.NativeLibraryBundlesImplementation;
import scalismo.support.nativelibs.NativeLibraryException;

import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import java.util.*;

public class GLProfiles {
    static {
        try {
            NativeLibraryBundles.initialize(InitializationMode.WARN_VERBOSE);
        } catch (NativeLibraryException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, GLProfile> getAvailableProfiles(boolean debug) {
        Map<String, GLProfile> profiles = new HashMap<String, GLProfile>();
        for (String profileName : GLProfile.GL_PROFILE_LIST_ALL) {
            try {
                GLProfile profile = GLProfile.get(profileName);
                profiles.put(profileName, profile);

            } catch (GLException ex) {
                if (debug) {
                    System.out.println("Profile "+profileName+ " is not available");
                }
            }
        }
        return profiles;
    }
}
