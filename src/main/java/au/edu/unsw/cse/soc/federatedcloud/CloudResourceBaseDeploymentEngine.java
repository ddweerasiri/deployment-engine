package au.edu.unsw.cse.soc.federatedcloud;
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


import au.edu.unsw.cse.soc.federatedcloud.connectors.CloudResourceDeploymentConnector;
import au.edu.unsw.cse.soc.federatedcloud.connectors.ConnectorFactory;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.CloudResourceDescription;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.Constants;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.DeploymentScriptReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * User: denis
 * Interpret a workflow xml and execute actions
 */
public class CloudResourceBaseDeploymentEngine {
    private static final Logger logger = LoggerFactory.getLogger(CloudResourceBaseDeploymentEngine.class);

    public static void main(String[] args) throws Exception {
        File file = new File("sample-descriptions/SENG1031.json");   // cloud resource to be deployed as the input

        CloudResourceBaseDeploymentEngine engine = new CloudResourceBaseDeploymentEngine();
        engine.deployCloudResourceDescription(file);

    }

    /**
     * Deploy a cloud resources configuration for a given JSON file which specifies a cloud resource description
     * @param file JSON file which specifies a cloud resource description
     * @throws Exception
     */
    public void deployCloudResourceDescription(File file) throws Exception {
        CloudResourceDescription description = DataModelUtil.buildCouldResourceDescriptionFromJSON(file);

        deployCloudResourceDescription(description);
    }

    /**
     * Deploy a cloud resources configuration for a given {@code CloudResourceDescription}
     * @param description object of {@code CloudResourceDescription}
     * @throws Exception
     */
    public void deployCloudResourceDescription(CloudResourceDescription description) throws Exception {
        DeploymentScriptReference deploymentReference;
        try {
            //check whether the cloud resource has the provider named {@code Constants.CLOUD_RESOURCE_BASE_PROVIDER_NAME}, which is used to deploy the composite resources
            deploymentReference = description.getDeploymentScriptReference(Constants.CLOUD_RESOURCE_BASE_PROVIDER_NAME);
        } catch (RuntimeException rex) {
            //if the provider named {@code Constants.CLOUD_RESOURCE_BASE_PROVIDER_NAME} does not exist, one of other deployment script references is used
            deploymentReference = description.getDeploymentScriptReference();
        }

        CloudResourceDeploymentConnector connector = ConnectorFactory.build(deploymentReference.getProvider());
        connector.deploy(description);
    }

    /**
     * Deploy a cloud resource for a given id
     * @param componentID ID of a cloud resource description
     */
    public void deployCloudResourceDescription(int componentID) throws Exception {
        CloudResourceDescription description = DataModelUtil.buildCouldResourceDescriptionFromJSON(componentID);

        deployCloudResourceDescription(description);
    }
}
