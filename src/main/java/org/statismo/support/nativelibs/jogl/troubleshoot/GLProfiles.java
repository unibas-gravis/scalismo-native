package org.statismo.support.nativelibs.jogl.troubleshoot;

import org.statismo.support.nativelibs.NativeLibraryBundles;
import org.statismo.support.nativelibs.impl.NativeLibraryException;

import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import java.util.*;

public class GLProfiles {
    static {
        try {
            NativeLibraryBundles.initialize(NativeLibraryBundles.InitializationMode.WARN_VERBOSE);
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
