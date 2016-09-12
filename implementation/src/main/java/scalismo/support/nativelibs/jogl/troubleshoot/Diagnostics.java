/*
 * Copyright 2016 University of Basel, Graphics and Vision Research Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scalismo.support.nativelibs.jogl.troubleshoot;

import scalismo.support.nativelibs.InitializationMode;
import scalismo.support.nativelibs.NativeLibraryBundles;

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
