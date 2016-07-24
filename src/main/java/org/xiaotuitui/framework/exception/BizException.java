package org.xiaotuitui.framework.exception;

public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public BizException() {
        super();
    }

    public BizException(String message) {
        super(message);
    }

    public BizException(Throwable cause) {
    	super(cause);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public static void throwWhenFalse(boolean maybeFalse, String msgToUsr, Throwable cause) throws BizException {
        if (!maybeFalse) {
            throw new BizException(msgToUsr, cause);
        }
    }

    public static void throwWhenFalse(boolean maybeFalse, String msgToUsr) throws BizException {
        if (!maybeFalse) {
            throw new BizException(msgToUsr);
        }
    }

    public static void throwWhenNull(Object objMayBeNull, String msgToUsr, Throwable cause) throws BizException {
        if (objMayBeNull == null) {
            throw new BizException(msgToUsr, cause);
        }
    }
    
    public static void throwWhenNull(Object objMayBeNull, String msgToUsr) throws BizException {
        if (objMayBeNull == null) {
            throw new BizException(msgToUsr);
        }
    }

}