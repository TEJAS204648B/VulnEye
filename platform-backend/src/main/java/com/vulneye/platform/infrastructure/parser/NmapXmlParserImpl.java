package com.vulneye.platform.infrastructure.parser;

import com.vulneye.platform.entity.Scan;
import com.vulneye.platform.infrastructure.parser.dto.NmapHostResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapPortResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapScanResult;
import com.vulneye.platform.infrastructure.parser.dto.NmapScriptResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(NmapXmlParserImpl.class);

    @Override
    public NmapScanResult parse(Scan scan) throws IOException {

        File reportFile = new File(scan.getResultPath());

        if (!reportFile.exists()) {
            throw new IOException("Nmap report not found: " + reportFile.getAbsolutePath());
        }

        try {
            DocumentBuilder builder = createDocumentBuilder();
            Document document = builder.parse(reportFile);
            document.getDocumentElement().normalize();

            return parseScanResult(document);

        } catch (Exception ex) {
            logger.error(
                    "Failed to parse Nmap XML report: {}",
                    reportFile.getAbsolutePath(),
                    ex);

            throw new IOException("Failed to parse Nmap XML report.", ex);
        }
    }

    private DocumentBuilder createDocumentBuilder() throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
        factory.setFeature(
                "http://xml.org/sax/features/external-general-entities",
                false);
        factory.setFeature(
                "http://xml.org/sax/features/external-parameter-entities",
                false);

        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        factory.setXIncludeAware(false);
        factory.setExpandEntityReferences(false);

        return factory.newDocumentBuilder();
    }

    private NmapScanResult parseScanResult(Document document) {

        NmapScanResult scanResult = new NmapScanResult();
        NodeList hostNodes = document.getElementsByTagName("host");

        for (int i = 0; i < hostNodes.getLength(); i++) {
            Element hostElement = (Element) hostNodes.item(i);
            scanResult.getHosts().add(parseHost(hostElement));
        }

        return scanResult;
    }

    private NmapHostResult parseHost(Element hostElement) {

        NmapHostResult hostResult = new NmapHostResult();

        setHostStatus(hostElement, hostResult);
        setHostAddress(hostElement, hostResult);
        setHostHostname(hostElement, hostResult);

        NodeList portNodes = hostElement.getElementsByTagName("port");

        for (int i = 0; i < portNodes.getLength(); i++) {
            Element portElement = (Element) portNodes.item(i);
            hostResult.getPorts().add(parsePort(portElement));
        }

        return hostResult;
    }

    private void setHostStatus(
            Element hostElement,
            NmapHostResult hostResult) {

        NodeList statusNodes = hostElement.getElementsByTagName("status");

        if (statusNodes.getLength() > 0) {
            Element statusElement = (Element) statusNodes.item(0);
            hostResult.setStatus(
                    normalize(statusElement.getAttribute("state")));
        }
    }

    private void setHostAddress(
            Element hostElement,
            NmapHostResult hostResult) {

        NodeList addressNodes = hostElement.getElementsByTagName("address");

        if (addressNodes.getLength() > 0) {
            Element addressElement = (Element) addressNodes.item(0);
            hostResult.setAddress(
                    normalize(addressElement.getAttribute("addr")));
        }
    }

    private void setHostHostname(
            Element hostElement,
            NmapHostResult hostResult) {

        NodeList hostnameNodes = hostElement.getElementsByTagName("hostname");

        if (hostnameNodes.getLength() > 0) {
            Element hostnameElement = (Element) hostnameNodes.item(0);
            hostResult.setHostname(
                    normalize(hostnameElement.getAttribute("name")));
        }
    }

    private NmapPortResult parsePort(Element portElement) {

        NmapPortResult portResult = new NmapPortResult();

        portResult.setPort(
                normalize(portElement.getAttribute("portid")));
        portResult.setProtocol(
                normalize(portElement.getAttribute("protocol")));

        setPortState(portElement, portResult);
        setPortService(portElement, portResult);
        setPortScripts(portElement, portResult);

        return portResult;
    }

    private void setPortState(
            Element portElement,
            NmapPortResult portResult) {

        NodeList stateNodes = portElement.getElementsByTagName("state");

        if (stateNodes.getLength() > 0) {
            Element stateElement = (Element) stateNodes.item(0);
            portResult.setState(
                    normalize(stateElement.getAttribute("state")));
        }
    }

    private void setPortService(
            Element portElement,
            NmapPortResult portResult) {

        NodeList serviceNodes = portElement.getElementsByTagName("service");

        if (serviceNodes.getLength() == 0) {
            return;
        }

        Element serviceElement = (Element) serviceNodes.item(0);

        portResult.setService(
                normalize(serviceElement.getAttribute("name")));
        portResult.setProduct(
                normalize(serviceElement.getAttribute("product")));
        portResult.setVersion(
                normalize(serviceElement.getAttribute("version")));
        portResult.setExtraInfo(
                normalize(serviceElement.getAttribute("extrainfo")));
    }

    private void setPortScripts(
            Element portElement,
            NmapPortResult portResult) {

        NodeList scriptNodes = portElement.getElementsByTagName("script");

        for (int i = 0; i < scriptNodes.getLength(); i++) {
            Element scriptElement = (Element) scriptNodes.item(i);
            NmapScriptResult scriptResult = parseScript(scriptElement);

            portResult.getScripts().add(scriptResult);
        }

        logScripts(portResult);
    }

    private NmapScriptResult parseScript(Element scriptElement) {

        NmapScriptResult scriptResult = new NmapScriptResult();

        scriptResult.setId(
                normalize(scriptElement.getAttribute("id")));
        scriptResult.setOutput(
                normalize(scriptElement.getAttribute("output")));

        return scriptResult;
    }

    private void logScripts(NmapPortResult portResult) {

        for (NmapScriptResult script : portResult.getScripts()) {
            logger.debug(
                    "[NSE] Port {} Script={}",
                    portResult.getPort(),
                    script.getId());
        }
    }

    /**
     * Returns null if the value is null or blank.
     */
    private String normalize(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return value;
    }
}