package sem.mission.controlles.mclncontroller;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import mcln.model.MclnModelRetriever;
import mcln.model.MclnProject;
import org.w3c.dom.Document;
import sem.app.AppConstants;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by Admin on 11/9/2017.
 */
class SemMclnRetriever {

    static MclnProject retrieveMclnController(String fileClassPath) {
        InputStream inputStream = SemMclnRetriever.class.getResourceAsStream(fileClassPath);
        if (inputStream == null) {
            return null;
        }
        Document mclnAsDocument = MclnModelRetriever.readMclnProjectXml(inputStream);
        MclnProject mclnProject = MclnModelRetriever.createMclnProjectFromXmlDom(mclnAsDocument);
        String formattedXmlString = xmlDocumentToFormattedString(mclnAsDocument);
        System.out.println("McLN Controller: " + formattedXmlString);
        return mclnProject;
    }

    public static final String xmlDocumentToFormattedString(Document xmlDocument) {
        try {
            OutputFormat format = new OutputFormat(xmlDocument);
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(xmlDocument);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    //
    // testing
    //

    public static void main(String[] args) {
        SemMclnRetriever.retrieveMclnController(AppConstants.MCLN_CONTROLLER_CLASS_PATH);
    }
}
