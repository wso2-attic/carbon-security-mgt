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

package org.wso2.carbon.security.util;

import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.identity.core.persistence.JDBCPersistenceManager;

import java.sql.Connection;

/**
 * Utility class for database operations.
 */
public class KeyStoreDatabaseUtil {

    private KeyStoreDatabaseUtil() {
    }

    /**
     * Get a database connection instance from the Identity Persistence Manager
     *
     * @param shouldApplyTransaction Whether to apply transaction or not.
     *
     * @return Database Connection
     * @throws IdentityRuntimeException Error when getting a database connection to Identity database
     */
    public static Connection getDBConnection(boolean shouldApplyTransaction) throws IdentityRuntimeException {

        return JDBCPersistenceManager.getInstance().getDBConnection(shouldApplyTransaction);
    }

    /**
     * Get a database connection instance from the Identity Persistence Manager.
     */
    public static void rollbackTransaction(Connection dbConnection) {

        JDBCPersistenceManager.getInstance().rollbackTransaction(dbConnection);
    }

    /**
     * Get a database connection instance from the Identity Persistence Manager.
     */
    public static void commitTransaction(Connection dbConnection) {

        JDBCPersistenceManager.getInstance().commitTransaction(dbConnection);
    }
}
