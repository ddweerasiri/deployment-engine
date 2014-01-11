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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * User: denis
 * Interpret a workflow xml and execute actions
 */
public class DeploymentEngine {
    private static final Logger logger = LoggerFactory.getLogger(DeploymentEngine.class);

    public static void main(String[] args) throws Exception {
        buildWorkflow();
    }

    private static void buildWorkflow() throws Exception {
        File file = new File("src/main/resources/workflow.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);

        Element controlflow = (Element) document.getElementsByTagName("controlflow").item(0);
        Element sequential = (Element) controlflow.getElementsByTagName("sequential").item(0);
        NodeList cloudResources = sequential.getElementsByTagName("resource");

        for(int i = 0; i < cloudResources.getLength(); i++) {
            Element cloudResource = (Element) cloudResources.item(i);
            String cloudResourceID = cloudResource.getAttribute("id");
            CloudResourceDescription description = buildCouldResourceDescriptionFromJSON(cloudResourceID);

            CloudResourceDeploymentConnector connector = new AWSEC2Connector();
            connector.deploy(description);
        }
    }

    private static CloudResourceDescription buildCouldResourceDescriptionFromJSON(String cloudResourceID) throws Exception {
        logger.warn("Not implemented yet");
        readJSON();
        return null;
    }

    private static void readJSON() throws Exception {
        //String json = readUrl("http://www.javascriptkit.com/" + "dhtmltutors/javascriptkit.json");
        String json = "{\n" +
                "  \"attributes\": {\n" +
                "    \"id\": 2,\n" +
                "    \"name\": \"SENG1031 Course configuration\"\n" +
                "  },\n" +
                "  \"componentResourceIDs\": [1,3,4,5,6],\n" +
                "  \"deploymentScriptReferences\": []\n" +
                "}\n";

        Gson gson = new Gson();
        CloudResourceDescription description = gson.fromJson(json, CloudResourceDescription.class);

        logger.info("    " + description);
    }

    /**
     * Download the given URL (as text)
     * @param urlString
     * @return
     * @throws Exception
     */
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }
}
