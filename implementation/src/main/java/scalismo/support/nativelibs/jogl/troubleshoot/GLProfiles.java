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
import scalismo.support.nativelibs.NativeLibraryException;

import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import java.util.HashMap;
import java.util.Map;

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
