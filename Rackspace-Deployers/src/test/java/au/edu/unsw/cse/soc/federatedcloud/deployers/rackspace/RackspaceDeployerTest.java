package au.edu.unsw.cse.soc.federatedcloud.deployers.rackspace;
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
import au.edu.unsw.cse.soc.federatedcloud.datamodel.CloudResourceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: denis
 * Test case for RackspaceDeployer
 */
public class RackspaceDeployerTest {
    private static final Logger log = LoggerFactory.getLogger(RackspaceDeployerTest.class);

    @org.junit.Test
    public void testDeploy() throws Exception {
        CloudResourceDescription cloudResourceDescription = DataModelUtil.buildCouldResourceDescriptionFromJSON(8);
        RackspaceDeployer deployer = new RackspaceDeployer();
        deployer.deploy(cloudResourceDescription);
    }
}
