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

package org.wso2.carbon.security.keystore.persistance;

import org.wso2.carbon.security.keystore.KeyStoreRuntimeException;

/**
 * Custom exception used to handle DB connection unavailability issues
 */
public class KeyStoreDBConnectionException extends KeyStoreRuntimeException {

    public KeyStoreDBConnectionException(String message) {

        super(message);
    }

    public KeyStoreDBConnectionException(String errorCode, Throwable cause) {

        super(errorCode, cause);
    }

    public static KeyStoreDBConnectionException error(String message) {

        return new KeyStoreDBConnectionException(message);
    }

    public static KeyStoreDBConnectionException error(String errorDescription, Throwable cause) {

        return new KeyStoreDBConnectionException(errorDescription, cause);
    }
}
