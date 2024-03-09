
import generated.Tree;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.File;
import java.io.IOException;

public class XMLImporter {
    private Schema schema;
    private File inputXML;
    private beans.Node rootNode;

    public XMLImporter(File schemaFile, File inputXMLFile) throws SAXException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        this.schema = schemaFactory.newSchema(schemaFile);
        this.inputXML = inputXMLFile;
    }

    /**
     * First xmlFile is validated.
     * If valid: import data
     * @return true if imported, otherwise false
     */
    public boolean importXMLData() {

        try {
            Source xml = new StreamSource(this.inputXML);
            Validator validator = schema.newValidator();
            validator.validate(xml);
        } catch (SAXException e) {
            e.printStackTrace();
            System.out.println("XMLImporter: File structure is invalid!");
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("XMLImporter: IOException while validating.");
            return false;
        }

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Tree.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            this.rootNode = Transformer.getInstance().parseTree((Tree)unmarshaller.unmarshal(inputXML));
        } catch (JAXBException jaxbEX) {
            System.out.println("XMLImporter: Mapping error.");
            jaxbEX.printStackTrace();
            return false;
        }

        return true;
    }

    public beans.Node getTree() {
        return rootNode;
    }
}
