package au.edu.unsw.cse.soc.federatedcloud.connectors;
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

import au.edu.unsw.cse.soc.federatedcloud.DataModelUtil;
import au.edu.unsw.cse.soc.federatedcloud.DeploymentEngine;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.Behavior;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.CloudResourceDescription;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.CloudResourcesComposition;
import au.edu.unsw.cse.soc.federatedcloud.datamodel.DeploymentScriptReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;

/**
 * User: denis
 * Connector for CloudBase deployer
 */
public class CloudBaseDeploymentConnector implements CloudResourceDeploymentConnector {
    private static final Logger logger = LoggerFactory.getLogger(CloudBaseDeploymentConnector.class);

    public void deploy(CloudResourceDescription description) throws IOException {
        DeploymentScriptReference reference = description.getDeploymentScriptReference("CloudResourceBase");
        File deploymentWorkflowScriptFile = new File(reference.getReference());
        CloudResourcesComposition compositionWorkflow = DataModelUtil.buildCompositionWorkflowFromFile(deploymentWorkflowScriptFile);

        Behavior behavior = compositionWorkflow.getControlFlow().getDeploymentBehavior();
        HashSet<Integer> componentResourceIDs = compositionWorkflow.getControlFlow().getComponentResourceIDs();

        logger.warn("Deployment behavior is still not integrated to the deployment logic.");
        for(Integer componentID : componentResourceIDs) {
            DeploymentEngine engine = new DeploymentEngine();
            try {
                engine.deployCloudResourceDescription(componentID);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
