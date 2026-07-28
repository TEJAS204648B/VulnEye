package com.vulneye.platform.infrastructure.parser;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.infrastructure.parser.dto.NmapHostResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapPortResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;

@Service
public class NmapXmlParserImpl implements NmapXmlParser {

    @Override
    public NmapScanResult parse(Scan scan) throws IOException {

        File reportFile = new File(scan.getResultPath());

        if (!reportFile.exists()) {
            throw new IOException("Nmap report not found: " + reportFile.getAbsolutePath());
        }

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
            factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
            factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

            factory.setXIncludeAware(false);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.parse(reportFile);
            document.getDocumentElement().normalize();

            NmapScanResult scanResult = new NmapScanResult();

            NodeList hostNodes = document.getElementsByTagName("host");

            for (int i = 0; i < hostNodes.getLength(); i++) {

                Element hostElement = (Element) hostNodes.item(i);

                NmapHostResult hostResult = new NmapHostResult();

                NodeList statusNodes = hostElement.getElementsByTagName("status");
                if (statusNodes.getLength() > 0) {
                    Element statusElement = (Element) statusNodes.item(0);
                    hostResult.setStatus(statusElement.getAttribute("state"));
                }

                NodeList addressNodes = hostElement.getElementsByTagName("address");
                if (addressNodes.getLength() > 0) {
                    Element addressElement = (Element) addressNodes.item(0);
                    hostResult.setAddress(addressElement.getAttribute("addr"));
                }

                NodeList hostnameNodes = hostElement.getElementsByTagName("hostname");
                if (hostnameNodes.getLength() > 0) {
                    Element hostnameElement = (Element) hostnameNodes.item(0);
                    hostResult.setHostname(hostnameElement.getAttribute("name"));
                }

                NodeList portNodes = hostElement.getElementsByTagName("port");

                for (int j = 0; j < portNodes.getLength(); j++) {

                    Element portElement = (Element) portNodes.item(j);

                    NmapPortResult portResult = new NmapPortResult();

                    portResult.setPort(portElement.getAttribute("portid"));
                    portResult.setProtocol(portElement.getAttribute("protocol"));

                    NodeList stateNodes = portElement.getElementsByTagName("state");
                    if (stateNodes.getLength() > 0) {
                        Element stateElement = (Element) stateNodes.item(0);
                        portResult.setState(stateElement.getAttribute("state"));
                    }

                    NodeList serviceNodes = portElement.getElementsByTagName("service");
                    if (serviceNodes.getLength() > 0) {
                        Element serviceElement = (Element) serviceNodes.item(0);
                        portResult.setService(serviceElement.getAttribute("name"));
                    }

                    hostResult.getPorts().add(portResult);
                }

                scanResult.getHosts().add(hostResult);
            }

            return scanResult;

        } catch (Exception ex) {
            throw new IOException("Failed to parse Nmap XML report.", ex);
        }
    }
}