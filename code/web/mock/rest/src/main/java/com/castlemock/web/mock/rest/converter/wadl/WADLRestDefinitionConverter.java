/*
 * Copyright 2017 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.castlemock.web.mock.rest.converter.wadl;

import com.castlemock.core.basis.model.http.domain.HttpMethod;
import com.castlemock.core.mock.rest.model.project.domain.*;
import com.castlemock.web.basis.manager.FileManager;
import com.castlemock.web.mock.rest.converter.AbstractRestDefinitionConverter;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The {@link WADLRestDefinitionConverter} class provides functionality related to WADL.
 * @author Karl Dahlgren
 * @since 1.10
 */
public class WADLRestDefinitionConverter extends AbstractRestDefinitionConverter {

    private final FileManager fileManager;
    private static final Logger LOGGER = Logger.getLogger(WADLRestDefinitionConverter.class);


    public WADLRestDefinitionConverter(final FileManager fileManager){
        this.fileManager = fileManager;
    }

    /**
     * The method is responsible for parsing a {@link File} and converting into a list of {@link RestApplication}.
     * @param file The {@link File} be parsed and converted into a list of {@link RestApplication}.
     * @param generateResponse Will generate a default response if true. No response will be generated if false.
     * @return A list of {@link RestApplication} based on the provided file.
     */
    @Override
    public List<RestApplication> convert(final File file, final boolean generateResponse){
        List<RestApplication> applications = new LinkedList<RestApplication>();
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);
            document.getDocumentElement().normalize();

            List<Element> applicationElements = getApplications(document);

            for(Element applicationElement : applicationElements){
                final String applicationName = file.getName().replace(".wadl", "");
                final String baseUri = resourceBase(applicationElement);
                final RestApplication restApplication = new RestApplication();
                restApplication.setName(applicationName);
                applications.add(restApplication);

                final List<Element> resourceElements = getResources(applicationElement);
                for(Element resourceElement : resourceElements){
                    final String resourceName = resourceElement.getAttribute("path");
                    final RestResource restResource = new RestResource();
                    restResource.setName(resourceName);
                    restResource.setUri(baseUri + resourceName);
                    restApplication.getResources().add(restResource);

                    final List<Element> methodElements = getMethods(resourceElement);
                    for(Element methodElement : methodElements){
                        final String methodName = methodElement.getAttribute("id");
                        final String methodType = methodElement.getAttribute("name");

                        final RestMethod restMethod = new RestMethod();
                        restMethod.setName(methodName);
                        restMethod.setHttpMethod(HttpMethod.valueOf(methodType));
                        restMethod.setStatus(RestMethodStatus.MOCKED);
                        restMethod.setResponseStrategy(RestResponseStrategy.RANDOM);
                        restMethod.setMockResponses(new ArrayList<RestMockResponse>());


                        if(generateResponse){
                            RestMockResponse restMockResponse = generateResponse();
                            restMethod.getMockResponses().add(restMockResponse);
                        }

                        restResource.getMethods().add(restMethod);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable parse WADL file", e);
        }

        return applications;
    }

    /**
     * The convert method provides the functionality to convert the provided {@link File} into
     * a list of {@link RestApplication}.
     *
     * @param location         The location of the definition file
     * @param generateResponse Will generate a default response if true. No response will be generated if false.
     * @return A list of {@link RestApplication} based on the provided file.
     */
    @Override
    public List<RestApplication> convert(final String location,
                                         boolean generateResponse) {

        final List<RestApplication> restApplications = new ArrayList<>();
        List<File> files = null;
        try {
            files = fileManager.uploadFiles(location);

            for(File file : files){
                List<RestApplication> convertedRestApplications = convert(file, generateResponse);
                restApplications.addAll(convertedRestApplications);
            }
        } catch (IOException e) {
            LOGGER.error("Unable to download file file: " + location, e);
        } finally {
            if(files != null){
                for(File uploadedFile : files){
                    boolean deletionResult = fileManager.deleteFile(uploadedFile);
                    if(deletionResult){
                        LOGGER.debug("Deleted the following WADL file: " + uploadedFile.getName());
                    } else {
                        LOGGER.warn("Unable to delete the following WADL file: " + uploadedFile.getName());
                    }

                }
            }
        }
        return restApplications;
    }

    /**
     * The method extracts all the application elements from the provided document
     * @param document The document which contains all the application that will be extracted
     * @return A list of application elements
     */
    private List<Element> getApplications(Document document){
        List<Element> applicationElements = new LinkedList<Element>();
        final NodeList applicationNodeList = document.getElementsByTagName("application");

        for (int applicationIndex = 0; applicationIndex < applicationNodeList.getLength(); applicationIndex++) {
            Node applicationNode = applicationNodeList.item(applicationIndex);
            if (applicationNode.getNodeType() == Node.ELEMENT_NODE) {
                Element applicationElement = (Element) applicationNode;
                applicationElements.add(applicationElement);
            }
        }
        return applicationElements;
    }

    /**
     * The method extracts all the resource elements from the provided application element
     * @param applicationElement The application element which contains all the resources that will be extracted
     * @return A list of resource elements
     */
    private List<Element> getResources(Element applicationElement){
        List<Element> resourceElements = new LinkedList<Element>();
        NodeList resourceNodeList = applicationElement.getElementsByTagName("resource");

        for (int resourceIndex = 0; resourceIndex < resourceNodeList.getLength(); resourceIndex++) {
            Node resourceNode = resourceNodeList.item(resourceIndex);
            if (resourceNode.getNodeType() == Node.ELEMENT_NODE) {
                Element resourceElement = (Element) resourceNode;
                resourceElements.add(resourceElement);
            }
        }
        return resourceElements;
    }

    /**
     * The method extracts all the method elements from the provided resource element
     * @param resourceElement The resource element which contains all the methods that will be extracted
     * @return A list of method elements
     */
    private List<Element> getMethods(Element resourceElement){
        List<Element> methodElements = new LinkedList<Element>();
        NodeList methodNodeList = resourceElement.getElementsByTagName("method");

        for (int methodIndex = 0; methodIndex < methodNodeList.getLength(); methodIndex++) {
            Node methodNode = methodNodeList.item(methodIndex);
            if (methodNode.getNodeType() == Node.ELEMENT_NODE) {
                Element methodElement = (Element) methodNode;
                methodElements.add(methodElement);
            }
        }
        return methodElements;
    }

    /**
     * The method provides the functionality to extract the resource base from a provided application element
     * @param applicationElement The application element that contains the resource base
     * @return The resource base from the application element
     * @throws MalformedURLException
     */
    private String resourceBase(Element applicationElement) throws MalformedURLException {
        final NodeList resourcesNodeList = applicationElement.getElementsByTagName("resources");
        for (int resourcesIndex = 0; resourcesIndex < resourcesNodeList.getLength(); resourcesIndex++) {
            Node resourcesNode = resourcesNodeList.item(resourcesIndex);
            if (resourcesNode.getNodeType() == Node.ELEMENT_NODE) {
                Element resourcesElement = (Element) resourcesNode;
                String resourceBase = resourcesElement.getAttribute("base");
                URL url = new URL(resourceBase);
                return url.getPath();
            }
        }
        return null;
    }

}
