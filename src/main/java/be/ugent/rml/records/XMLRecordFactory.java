package be.ugent.rml.records;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XMLRecordFactory extends IteratorFormat<Document> implements ReferenceFormulationRecordFactory {

    protected String getContentType() {
        return "application/xml";
    }

    @Override
    List<Record> getRecordsFromDocument(Document document, String iterator) throws IOException {
        List<Record> records = new ArrayList<>();

        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            NodeList result = (NodeList) xPath.compile(iterator).evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < result.getLength(); i ++) {
                records.add(new XMLRecord(result.item(i)));
            }
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }

        return records;
    }

    @Override
    Document getDocumentFromStream(InputStream stream) throws IOException {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            return builder.parse(stream);
        } catch (SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
