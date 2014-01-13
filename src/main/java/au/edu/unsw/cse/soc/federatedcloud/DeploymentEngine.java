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


import au.edu.unsw.cse.soc.federatedcloud.connectors.AWSEC2Connector;
import au.edu.unsw.cse.soc.federatedcloud.connectors.CloudResourceDeploymentConnector;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * User: denis
 * Interpret a workflow xml and execute actions
 */
public class DeploymentEngine {
    private static final Logger logger = LoggerFactory.getLogger(DeploymentEngine.class);

    public static void main(String[] args) throws Exception {
        File file = new File("sample-descriptions/SENG1031.json");
        //deployWorkflow(file);
        deployCloudResourceDescription(file);

    }

    /**
     * Deploy a cloud resources configuration for a given JSON file which specifies a cloud resource description
     * @param file JSON file which specifies a cloud resource description
     * @throws Exception
     */
    private static void deployCloudResourceDescription(File file) throws Exception {
        CloudResourceDescription description = buildCouldResourceDescriptionFromJSON(file);

        deployCloudResourceDescription(description);
    }

    /**
     * Deploy a cloud resources configuration for a given {@code CloudResourceDescription}
     * @param description object of {@code CloudResourceDescription}
     * @throws Exception
     */
    private static void deployCloudResourceDescription(CloudResourceDescription description) throws Exception {
        Behavior behavior = description.getControlFlow().getDeploymentBehavior();
        HashSet<Integer> componentResourceIDs = description.getControlFlow().getComponentResourceIDs();

        logger.warn("Deployment behavior is still not integrated to the deployment logic.");
        for(Integer componentID : componentResourceIDs) {
            deployCloudResourceDescription(componentID);
        }
    }

    /**
     * Deploy a cloud resource for a given id
     * @param componentID
     */
    private static void deployCloudResourceDescription(int componentID) throws Exception {
        CloudResourceDescription description = buildCouldResourceDescriptionFromJSON(componentID);

        CloudResourceDeploymentConnector connector = new AWSEC2Connector();
        connector.deploy(description);

    }

    /**
     * Build a {@code CloudResourceDescription} from the JSON file
     * @param file the JSON model of the cloud resource description
     * @return an object of {@code CloudResourceDescription}
     * @throws Exception
     */
    private static CloudResourceDescription buildCouldResourceDescriptionFromJSON(File file) throws Exception {
        return processFile(file);
    }

    /**
     * Build a {@code CloudResourceDescription} for a given ID of a cloud resource description
     * @param cloudResourceID
     * @return an object of {@code CloudResourceDescription}
     * @throws Exception
     */
    private static CloudResourceDescription buildCouldResourceDescriptionFromJSON(int cloudResourceID) throws Exception {
        return searchFolder("./sample-descriptions", Pattern.compile("(?i).*\\.json$"), cloudResourceID);
    }

    private static CloudResourceDescription readJSON(String json) throws Exception {
        Gson gson = new Gson();
        return gson.fromJson(json, CloudResourceDescription.class);
    }

    /**
     * Search for all the files in a particular path and process them one by one
     * @param searchPath file location to be searched
     * @param filePattern search criteria
     */
    private static CloudResourceDescription searchFolder(String searchPath, Pattern filePattern, int cloudResourceDescriptionID) throws Exception {
        File dir = new File(searchPath);
        for(File item : dir.listFiles()){
            if(item.isDirectory()){
                //recursively search subdirectories
                return searchFolder(item.getAbsolutePath(), filePattern, cloudResourceDescriptionID);
            } else if(item.isFile() && filePattern.matcher(item.getName()).matches()){
                CloudResourceDescription description = processFile(item);
                boolean isEqual = isCorrectCloudResourceDescription(description, cloudResourceDescriptionID);
                if (isEqual) {
                    return description;
                }
            } else {
                String errorMsg = "Expected cloud resource description with \"id\": " + cloudResourceDescriptionID +
                        " was not found.";
                RuntimeException ex = new RuntimeException(errorMsg);
                logger.error(errorMsg, ex);
                throw ex;
            }
        }
        throw new RuntimeException("No files found on the specified directory:" + searchPath);
    }

    /**
     * The business logic to compare cloud resource description for a given id
     * @param description cloud resource description object
     * @param cloudResourceDescriptionID id to be search for the input cloud resource description object
     * @return whether the ID of the input cloud resource description object is equal to the input {@code cloudResourceDescriptionID}
     */
    private static boolean isCorrectCloudResourceDescription(CloudResourceDescription description, int cloudResourceDescriptionID) {
        int id = getIDOfCloudResourceDescription(description);
        return id == cloudResourceDescriptionID;
    }

    /**
     * Extract the ID of a cloud resource description
     * @param description an object of {@code CloudResourceDescription}
     * @return the ID of the given cloud resource description
     */
    private static int getIDOfCloudResourceDescription(CloudResourceDescription description) {
        String id = description.attributes.get("id");
        if (id == null || "".equals(id)) {
            String errorMsg = "This cloud description does not have the \"id\" attribute. \n" + description;
            RuntimeException ex =  new RuntimeException(errorMsg);
            logger.error(errorMsg, ex);
            throw ex;
        } else {
            return Integer.parseInt(id);
        }

    }

    /**
     * Process an file and returns the CloudResourceDescription
     * @param aFile input file to be processed (must be a JSON file)
     */
    private static CloudResourceDescription processFile(File aFile) throws Exception {
        String fileContent = FileUtils.readFileToString(aFile);
        return readJSON(fileContent);
    }
}
