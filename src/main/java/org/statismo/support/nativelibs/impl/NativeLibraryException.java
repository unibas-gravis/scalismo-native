package org.statismo.support.nativelibs.impl;

public class NativeLibraryException extends Exception {
	private static final long serialVersionUID = 1L;

	public NativeLibraryException(String msg) {
		super(msg);
	}

	public NativeLibraryException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public static class UnsupportedPlatformException extends NativeLibraryException {
		private static final long serialVersionUID = 1L;
		
		public UnsupportedPlatformException(String msg) {
			super(msg);
		}

	}
}
