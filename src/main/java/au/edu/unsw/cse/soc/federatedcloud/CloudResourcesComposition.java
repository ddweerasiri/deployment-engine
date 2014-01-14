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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;

/**
 * User: denis
 * Represent the composition of a set of cloud resources (aka Composite resource)
 */
public class CloudResourcesComposition {
    private static final Logger logger = LoggerFactory.getLogger(CloudResourcesComposition.class);

    public ControlFlow getControlFlow() {
        return controlFlow;
    }

    ControlFlow controlFlow;
}

class ControlFlow {
    public Behavior getDeploymentBehavior() {
        return deploymentBehavior;
    }

    public HashSet<Integer> getComponentResourceIDs() {
        return componentResourceIDs;
    }

    Behavior deploymentBehavior;
    HashSet<Integer> componentResourceIDs;
}

enum Behavior {SEQUENTIAL, PARALLEL};
