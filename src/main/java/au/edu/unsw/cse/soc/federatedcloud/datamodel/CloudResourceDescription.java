package au.edu.unsw.cse.soc.federatedcloud.datamodel;
/*
 * Copyright (c) 2014, Denis Weerasiri All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * User: denis
 * Represent the Data model of a Cloud resource
 */
public class CloudResourceDescription {
    private static final Logger logger = LoggerFactory.getLogger(CloudResourceDescription.class);

    public HashMap<String, String> getAttributes() {
        return attributes;
    }

    HashMap<String, String> attributes;

    public HashSet<DeploymentScriptReference> getDeploymentScriptReferences() {
        return deploymentScriptReferences;
    }

    /**
     * Extract the deployment script reference of a specific provider from the cloud resource description
     *
     * @param provider
     * @return
     */
    public DeploymentScriptReference getDeploymentScriptReference(String provider) {
        for (Iterator<DeploymentScriptReference> it = deploymentScriptReferences.iterator(); it.hasNext(); ) {
            DeploymentScriptReference scriptReference = it.next();
            if (scriptReference.getProvider().equals(provider)) {
                return scriptReference;
            }
        }
        throw new RuntimeException("Deployment Reference by Provider \"" + provider + "\" was no found.");

    }

    /**
     * Extract the deployment script reference from the cloud resource description. (When there are multiple providers,
     * only the first provider is returned)
     *
     * @return
     */
    public DeploymentScriptReference getDeploymentScriptReference() {
        for (Iterator<DeploymentScriptReference> it = deploymentScriptReferences.iterator(); it.hasNext(); ) {
            DeploymentScriptReference scriptReference = it.next();
            return scriptReference;
        }
        throw new RuntimeException("No Deployment References found.");
    }

    HashSet<DeploymentScriptReference> deploymentScriptReferences;

    /**
     * Extract the ID of the cloud resource description
     *
     * @return the ID of the given cloud resource description
     */
    public int getIDOfCloudResourceDescription() {
        String id = attributes.get("id");
        if (id == null || "".equals(id)) {
            String errorMsg = "This cloud description does not have the \"id\" attribute. \n" + this;
            RuntimeException ex = new RuntimeException(errorMsg);
            logger.error(errorMsg, ex);
            throw ex;
        } else {
            return Integer.parseInt(id);
        }

    }

    /**
     * The business logic to compare cloud resource description for a given id
     *
     * @param cloudResourceDescriptionID id to be search for the input cloud resource description object
     * @return whether the ID of the input cloud resource description object is equal to the input {@code cloudResourceDescriptionID}
     */
    public boolean isCorrectCloudResourceDescription(int cloudResourceDescriptionID) {
        int id = getIDOfCloudResourceDescription();
        return id == cloudResourceDescriptionID;
    }
}

