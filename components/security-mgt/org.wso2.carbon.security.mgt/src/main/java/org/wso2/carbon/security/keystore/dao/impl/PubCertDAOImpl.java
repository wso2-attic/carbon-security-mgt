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

package org.wso2.carbon.security.keystore.dao.impl;

import org.wso2.carbon.database.utils.jdbc.NamedPreparedStatement;
import org.wso2.carbon.security.SecurityConfigException;
import org.wso2.carbon.security.keystore.dao.PubCertDAO;
import org.wso2.carbon.security.keystore.dao.constants.PubCertDAOConstants;
import org.wso2.carbon.security.keystore.dao.constants.PubCertDAOConstants.PubCertTableColumns;
import org.wso2.carbon.security.keystore.model.PubCertModel;
import org.wso2.carbon.security.util.KeyStoreDatabaseUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.wso2.carbon.security.keystore.dao.DAOUtils.getTenantUUID;

public class PubCertDAOImpl extends PubCertDAO {

    private final String tenantUUID;

    public PubCertDAOImpl(int tenantId) throws SecurityConfigException {
        super(tenantId);
        this.tenantUUID = getTenantUUID(tenantId);
    }

    @Override
    public String addPubCert(PubCertModel pubCertModel) throws SecurityConfigException {

        try (Connection connection = KeyStoreDatabaseUtil.getDBConnection(true)) {
            try {
                String uuid = processAddPubCert(connection, pubCertModel, tenantUUID);
                KeyStoreDatabaseUtil.commitTransaction(connection);
                return uuid;
            } catch (SQLException e) {
                KeyStoreDatabaseUtil.rollbackTransaction(connection);
                // TODO: Check whether this exception type is okay. Also see if we need to use a server exception type. i.e something like SecurityConfigServerException
                throw new SecurityConfigException("Error while adding public certificate.", e);
            }
        } catch (SQLException e) {
            // TODO: Check whether this exception type is okay. Also see if we need to use a server exception type. i.e something like SecurityConfigServerException
            throw new SecurityConfigException("Error while adding public certificate.", e);
        }
    }

    @Override
    public Optional<PubCertModel> getPubCert(String uuid) throws SecurityConfigException {

        PubCertModel pubCertModel = null;

        try (Connection connection = KeyStoreDatabaseUtil.getDBConnection(false)) {
            try (NamedPreparedStatement statement = new NamedPreparedStatement(connection,
                    PubCertDAOConstants.SQLQueries.GET_PUB_CERT)) {
                statement.setString(PubCertTableColumns.ID, uuid);
                statement.setString(PubCertTableColumns.TENANT_UUID, tenantUUID);
                // T
                statement.setMaxRows(1);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        pubCertModel = new PubCertModel();
                        pubCertModel.setFileNameAppender(resultSet.getString(PubCertTableColumns.FILE_NAME_APPENDER));
                        pubCertModel.setContent(resultSet.getBytes(PubCertTableColumns.CONTENT));
                    }
                }
            } catch (SQLException e) {
                throw new SecurityConfigException("Error while retrieving notification template types.", e);
            }
        } catch (SQLException e) {
            throw new SecurityConfigException("Error while retrieving notification template types.", e);
        }
        return Optional.ofNullable(pubCertModel);
    }

    private String processAddPubCert(Connection connection, PubCertModel pubCertModel, String tenantUUID)
            throws SQLException {

        String id = UUID.randomUUID().toString();

        try (NamedPreparedStatement statement = new NamedPreparedStatement(connection,
                PubCertDAOConstants.SQLQueries.ADD_PUB_CERT)) {
            statement.setString(PubCertTableColumns.ID, id);
            statement.setString(PubCertTableColumns.FILE_NAME_APPENDER, pubCertModel.getFileNameAppender());
            statement.setString("TENANT_UUID", tenantUUID);
            statement.setBytes(4, pubCertModel.getContent());
            statement.executeUpdate();
        }
        return id;
    }
}
