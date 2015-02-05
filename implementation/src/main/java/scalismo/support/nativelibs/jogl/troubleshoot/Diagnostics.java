package scalismo.support.nativelibs.jogl.troubleshoot;

import scalismo.support.nativelibs.InitializationMode;
import scalismo.support.nativelibs.NativeLibraryBundles;
import scalismo.support.nativelibs.NativeLibraryBundlesImplementation;

import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;

public class Diagnostics {

    public static void main(String[] args) throws Exception {
        System.setProperty("jogl.verbose", "true");
        NativeLibraryBundles.initialize(InitializationMode.THROW_EXCEPTION_ON_FAIL);

        try {
            GLProfile profile = GLProfile.getDefault();
            System.out.println("Default OpenGL profile: " + profile.toString());
        } catch (GLException ex) {
            System.out.println("FAILED TO LOAD DEFAULT OpenGL PROFILE: " + ex.getMessage());
        }

        System.out.println();
        System.out.println("Trying to load all GL profiles...");
        for (String profileName : GLProfile.GL_PROFILE_LIST_ALL) {
            try {
                System.out.print("getting " + profileName + " - ");
                GLProfile profile = GLProfile.get(profileName);
                System.out.println("OK, got: " + profile.toString() + ", isGL2=" + profile.isGL2());
            } catch (GLException ex) {
                System.out.println("FAILED");
            }
        }
    }
}
