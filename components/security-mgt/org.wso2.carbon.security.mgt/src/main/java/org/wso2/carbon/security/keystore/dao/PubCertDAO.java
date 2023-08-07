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

import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.keystore.model.PubCertModel;

import java.util.Optional;

public abstract class PubCertDAO {

    private final int tenantId;

    public PubCertDAO(int tenantId) {
        this.tenantId = tenantId;
    }

    public abstract String addPubCert(PubCertModel pubCertModel) throws SecurityConfigException;

    public abstract Optional<PubCertModel> getPubCert(String uuid) throws SecurityConfigException;

    public int getTenantId() {

        return tenantId;
    }
}

