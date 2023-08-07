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

package org.wso2.carbon.security.keystore.dao;

import org.wso2.carbon.identity.core.util.IdentityTenantUtil;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.user.api.Tenant;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

public class DAOUtils {

    public static String getTenantUUID(int tenantId) throws SecurityConfigException {

        // Super tenant does not have a tenant UUID. Therefore, set a hard coded value.
        if (tenantId == MultitenantConstants.SUPER_TENANT_ID) {
            // Set a hard length of 32 characters for super tenant ID.
            // This is to avoid the database column length constraint violation.
            // TODO: shouldn't the length be 36 ?
            return String.format("%1$-32d", tenantId);
        }

        // TODO: getTenant also seems to throw a runtime exception if tenant does not exist. Figure out a way to use that, or catch that and convert to SecurityConfigException
        if (tenantId != MultitenantConstants.INVALID_TENANT_ID) {
            Tenant tenant = IdentityTenantUtil.getTenant(tenantId);
            return tenant.getTenantUniqueID();
        }

        throw new SecurityConfigException("Invalid tenant id: " + tenantId);
    }
}
