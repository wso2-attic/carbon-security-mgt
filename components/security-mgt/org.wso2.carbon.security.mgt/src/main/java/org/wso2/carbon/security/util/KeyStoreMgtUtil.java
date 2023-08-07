/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.security.util;

import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.base.IdentityRuntimeException;
import org.wso2.carbon.user.api.Tenant;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.user.core.service.RealmService;
import org.wso2.carbon.utils.ServerConstants;
import org.wso2.carbon.utils.WSO2Constants;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Hashtable;
import java.util.Map;

public class KeyStoreMgtUtil {

    private static final Log log = LogFactory.getLog(KeyStoreMgtUtil.class);
    private static RealmService realmService;

    // System Property constant for trust managers.
    public static final String PROP_TRUST_STORE_UPDATE_REQUIRED =
            "org.wso2.carbon.identity.core.util.TRUST_STORE_UPDATE_REQUIRED";
    private KeyStoreMgtUtil(){}

    /**
     * Get the realm service.
     *
     * @return realm service
     */
    public static RealmService getRealmService() {

        return realmService;
    }

    /**
     * Set the realm service.
     *
     * @param realmService realm service
     */
    public static void setRealmService(RealmService realmService) {

        KeyStoreMgtUtil.realmService = realmService;
    }

    /**
     * Dumping the generated pub. cert to a file
     *
     * @param configurationContext
     * @param cert                 content of the certificate
     * @param fileName             file name
     * @return file system location of the pub. cert
     */
    public static String dumpCert(ConfigurationContext configurationContext, byte[] cert,
                                  String fileName) {
        if (!verifyCertExistence(fileName, configurationContext)) {
            String workDir = (String) configurationContext.getProperty(ServerConstants.WORK_DIR);
            File pubCert = new File(workDir + File.separator + "pub_certs");

            if (fileName == null) {
                fileName = String.valueOf(System.currentTimeMillis() + new SecureRandom().nextDouble()) + ".cert";
            }
            if (!pubCert.exists()) {
                pubCert.mkdirs();
            }

            String filePath = workDir + File.separator + "pub_certs" + File.separator + fileName;
            OutputStream outStream = null;
            try {
                outStream = new FileOutputStream(filePath);
                outStream.write(cert);
            } catch (Exception e) {
                String msg = "Error when writing the public certificate to a file";
                log.error(msg);
                throw new SecurityException("msg", e);
            } finally {
                KeyStoreIOStreamUtils.flushOutputStream(outStream);
                KeyStoreIOStreamUtils.closeOutputStream(outStream);
            }

            Map fileResourcesMap = (Map) configurationContext.getProperty(WSO2Constants.FILE_RESOURCE_MAP);
            if (fileResourcesMap == null) {
                fileResourcesMap = new Hashtable();
                configurationContext.setProperty(WSO2Constants.FILE_RESOURCE_MAP, fileResourcesMap);
            }

            fileResourcesMap.put(fileName, filePath);
        }
        return WSO2Constants.ContextPaths.DOWNLOAD_PATH + "?id=" + fileName;
    }

    /**
     * Check whether the certificate is available in the file system
     *
     * @param fileName             file name
     * @param configurationContext configuration context of the current message
     */
    private static boolean verifyCertExistence(String fileName, ConfigurationContext configurationContext) {
        String workDir = (String) configurationContext.getProperty(ServerConstants.WORK_DIR);
        String filePath = workDir + File.separator + "pub_certs" + File.separator + fileName;
        File pubCert = new File(workDir + File.separator + "pub_certs" + File.separator + fileName);

        //if cert is still available then exit
        if (pubCert.exists()) {
            Map fileResourcesMap = (Map) configurationContext.getProperty(WSO2Constants.FILE_RESOURCE_MAP);
            if (fileResourcesMap == null) {
                fileResourcesMap = new Hashtable();
                configurationContext.setProperty(WSO2Constants.FILE_RESOURCE_MAP, fileResourcesMap);
            }
            if (fileResourcesMap.get(fileName) == null) {
                fileResourcesMap.put(fileName, filePath);
            }
            return true;
        }
        return false;
    }

    /**
     * Get the tenant id of the given tenant domain.
     *
     * @param tenantDomain tenant domain
     * @return tenant id
     * @throws IdentityRuntimeException if an error occurs while retrieving the tenant id
     */
    public static int getTenantId(String tenantDomain) throws IdentityRuntimeException {

        int tenantId = MultitenantConstants.INVALID_TENANT_ID;
        try {
            if (realmService != null) {
                tenantId = realmService.getTenantManager().getTenantId(tenantDomain);
            }
        } catch (UserStoreException e) {
            // Ideally user.core should be throwing an unchecked exception, in which case no need to wrap at this
            // level once more without adding any valuable contextual information. Because we don't have exception
            // enrichment properly implemented, we are appending the error message from the UserStoreException to the
            // new message
            throw IdentityRuntimeException.error("Error occurred while retrieving tenantId for tenantDomain: " +
                    tenantDomain + e.getMessage(), e);
        }
        if(tenantId == MultitenantConstants.INVALID_TENANT_ID){
            throw IdentityRuntimeException.error("Invalid tenant domain " + tenantDomain);
        } else {
            return tenantId;
        }
    }

    /**
     * Retrieve Tenant object for a given tenant ID.
     *
     * @param tenantId  Tenant ID.
     * @return          Tenant object.
     * @throws IdentityRuntimeException Error when retrieve tenant information.
     */
    public static Tenant getTenant(int tenantId) throws IdentityRuntimeException {

        Tenant tenant = null;
        try {
            if (realmService != null) {
                tenant = realmService.getTenantManager().getTenant(tenantId);
            }
        } catch (UserStoreException e) {
            throw IdentityRuntimeException.error("Error occurred while retrieving tenant for tenantId: " +
                    tenantId + e.getMessage(), e);
        }
        if (tenant == null){
            throw IdentityRuntimeException.error("Invalid tenantId: " + tenantId);
        } else {
            return tenant;
        }
    }
}
