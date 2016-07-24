package org.xiaotuitui.testframework;

public class TestException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public TestException() {
		super();
	}

	public TestException(String message, Throwable cause) {
		super(message, cause);
	}

	public TestException(String message) {
		super(message);
	}

	public TestException(Throwable cause) {
		super(cause);
	}

}