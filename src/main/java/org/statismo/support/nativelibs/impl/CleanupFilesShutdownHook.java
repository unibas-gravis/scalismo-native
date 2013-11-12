package org.statismo.support.nativelibs.impl;

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
		for (File file: files) {
			delete(file);
		}
		dir.delete();
	}

	public void deleteOnExit(File file) {
		cleanupStack.push(file);
	}
	
}
