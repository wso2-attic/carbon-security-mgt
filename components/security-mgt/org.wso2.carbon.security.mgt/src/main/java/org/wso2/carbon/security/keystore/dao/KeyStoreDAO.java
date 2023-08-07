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
import org.wso2.carbon.security.keystore.model.KeyStoreModel;

import java.util.List;
import java.util.Optional;

public abstract class KeyStoreDAO {

    // TODO: check whether converting this to a protected variable is better.
    private final int tenantId;

    public KeyStoreDAO(int tenantId) {
        this.tenantId = tenantId;
    }

    public abstract void addKeyStore(KeyStoreModel keyStoreModel) throws
            SecurityConfigException;

    // TODO: think whether we need a method to see existence of a key store.

    public abstract List<KeyStoreModel> getKeyStores() throws SecurityConfigException;

    public abstract Optional<KeyStoreModel> getKeyStore(String fileName) throws SecurityConfigException;

    public abstract void deleteKeyStore(String fileName) throws SecurityConfigException;

    public abstract void updateKeyStore(KeyStoreModel keyStoreModel) throws SecurityConfigException;

    public abstract void addPubCertIdToKeyStore(String fileName, String pubCertId) throws SecurityConfigException;

    public abstract Optional<String> getPubCertIdFromKeyStore(String fileName) throws SecurityConfigException;

    public int getTenantId() {

        return tenantId;
    }
}
