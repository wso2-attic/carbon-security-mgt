/*
 * Copyright (c) 2023, WSO2 LLC. (https://www.wso2.com).
 *
 * WSO2 LLC. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.security.keystore;

/**
 * Used for creating checked exceptions that can be handled.
 */
public class KeyStoreException extends Exception {

    private static final long serialVersionUID = 725992116511551241L;
    private String errorCode = null;

    public KeyStoreException(String message) {

        super(message);
    }

    public KeyStoreException(String errorCode, String message) {

        super(message);
        this.errorCode = errorCode;
    }

    public KeyStoreException(String message, Throwable cause) {

        super(message, cause);
    }

    public KeyStoreException(String errorCode, String message, Throwable cause) {

        super(message, cause);
        this.errorCode = errorCode;
    }

    @Deprecated
    public static KeyStoreException error(String message) {

        return new KeyStoreException(message);
    }

    @Deprecated
    public static KeyStoreException error(String errorCode, String message) {

        return new KeyStoreException(errorCode, message);
    }

    @Deprecated
    public static KeyStoreException error(String message, Throwable cause) {

        return new KeyStoreException(message, cause);
    }

    @Deprecated
    public static KeyStoreException error(String errorCode, String message, Throwable cause) {

        return new KeyStoreException(errorCode, message, cause);
    }

    public static <T extends KeyStoreException> T error(Class<T> exceptionClass, String message) {

        T exception = null;
        try {
            exception = exceptionClass.getConstructor(String.class).newInstance(message);
        } catch (Exception e) {
            throw new KeyStoreRuntimeException("Invalid Exception Type, " + e.getMessage());
        }
        return exception;
    }

    public static <T extends KeyStoreException> T error(Class<T> exceptionClass, String errorCode, String message) {

        T exception = null;
        try {
            exception = exceptionClass.getConstructor(String.class, String.class).newInstance(errorCode, message);
        } catch (Exception e) {
            throw new KeyStoreRuntimeException("Invalid Exception Type, " + e.getMessage(), e);
        }
        return exception;
    }

    public static <T extends KeyStoreException> T error(Class<T> exceptionClass, String message, Throwable cause) {

        T exception = null;
        try {
            exception = exceptionClass.getConstructor(String.class, Throwable.class).newInstance(message, cause);
        } catch (Exception e) {
            throw new KeyStoreRuntimeException("Invalid Exception Type, " + e.getMessage(), e);
        }
        return exception;
    }

    public static <T extends KeyStoreException> T error(Class<T> exceptionClass, String errorCode, String message,
                                                        Throwable cause) {

        T exception = null;
        try {
            exception = exceptionClass.getConstructor(String.class, String.class, Throwable.class).
                    newInstance(errorCode, message, cause);
        } catch (Exception e) {
            throw new KeyStoreRuntimeException("Invalid Exception Type, " + e.getMessage(), e);
        }
        return exception;
    }

    public String getErrorCode() {

        return errorCode;
    }

    public void setErrorCode(String errorCode) {

        this.errorCode = errorCode;
    }
}
