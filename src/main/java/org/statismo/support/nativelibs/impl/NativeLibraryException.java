package org.statismo.support.nativelibs.impl;

public class NativeLibraryException extends Exception {
	private static final long serialVersionUID = 1L;

	public NativeLibraryException(String msg) {
		super(msg);
	}

    public NativeLibraryException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public NativeLibraryException(Throwable cause) {
        super(cause);
    }

    public static NativeLibraryException wrap (Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        else if (throwable instanceof NativeLibraryException) {
            return  (NativeLibraryException) throwable;
        } else {
            return new NativeLibraryException(throwable);
        }

    }
}
