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

package scalismo.support.nativelibs.impl;

import java.io.File;
import java.util.Stack;

public class CleanupFilesShutdownHook extends Thread {

    private static CleanupFilesShutdownHook _INSTANCE = null;

    private final Stack<File> cleanupStack = new Stack<File>();

    public static synchronized CleanupFilesShutdownHook getInstance() {
        if (_INSTANCE == null) {
            _INSTANCE = new CleanupFilesShutdownHook();
        }
        return _INSTANCE;
    }

    private CleanupFilesShutdownHook() {
        Runtime.getRuntime().addShutdownHook(this);
    }

    @Override
    public void run() {
        while (!cleanupStack.isEmpty()) {
            File file = cleanupStack.pop();
            delete(file);
        }
    }

    private void delete(File file) {
        if (file.exists()) {
            if (!file.isDirectory()) {
                file.delete();
            } else {
                deleteRecursively(file);
            }
        }
    }

    private void deleteRecursively(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                delete(file);
            }
            dir.delete();
        }
    }

    public void deleteOnExit(File file) {
        cleanupStack.push(file);
    }

}
