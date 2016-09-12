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

package scalismo.support.nativelibs.jhdf5;

import ncsa.hdf.object.FileFormat;
import scalismo.support.nativelibs.NativeLibraryException;
import scalismo.support.nativelibs.impl.NativeLibraryBundle;
import scalismo.support.nativelibs.impl.NativeLibraryInfo;
import scalismo.support.nativelibs.impl.Platform;

import java.util.List;


public class JhdfLibraryBundle extends NativeLibraryBundle {

    public String getName() {
        return "JHDF";
    }

    public String getVersion() {
        return "5";
    }

    @Override
    protected void getSupportedPlatformsInto(List<String> list) {
        list.add(Platform.PLATFORM_LINUX64);
        list.add(Platform.PLATFORM_WIN64);
        list.add(Platform.PLATFORM_WIN32);
        list.add(Platform.PLATFORM_MAC64);
    }

    @Override
    protected void getLibraryNamesInto(List<String> list) {

        list.add("jhdf5");

    }

    @Override
    public Runnable getVerifierRunnable() {
        return new Runnable() {

            @Override
            public void run() {
                FileFormat f = FileFormat.getFileFormat(FileFormat.FILE_TYPE_HDF5);
                if (f == null) {
                    throw new IllegalStateException("didn't work, eh?");
                }
            }
        };
    }

    @Override
    protected boolean onLibraryExtracted(NativeLibraryInfo info)
            throws NativeLibraryException {
        if ("jhdf5".equals(info.getBaseName())) {
            System.setProperty("ncsa.hdf.hdf5lib.H5.hdf5lib", info.getTargetFile().getAbsolutePath());
            return false;
        }
        return super.onLibraryExtracted(info);
    }


}
