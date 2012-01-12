package nl.rug.search.odr.export;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import nl.rug.search.odr.entities.Project;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class TeXExport {

    private static final String[] FILE_NAMES = new String[]{"main",
        "concerns",
        "decisions",
        "description",
        "iterations",
        "members"};
    private static final String DIRECTORY_NAME = "odr-tex-export/";
    private final ZipOutputStream zipOut;
    private final StringTemplateGroup templateGroup;
    private final Project project;

    public TeXExport(OutputStream out, Project project) {
        templateGroup = new StringTemplateGroup("texExportGroup");
        templateGroup.registerRenderer(Date.class, new SimpleDateRenderer());
        templateGroup.registerRenderer(String.class, new TeXEscapeRenderer());
        zipOut = new ZipOutputStream(out);
        this.project = project;
        project.orderLists();
    }

    private void newEntry(String name) throws IOException {
        zipOut.putNextEntry(new ZipEntry(DIRECTORY_NAME + name));
    }
    
    public void export() {
        try {
            zipOut.putNextEntry(new ZipEntry(DIRECTORY_NAME));
            for(String fileName : FILE_NAMES) {
                export(fileName);
            }
        } catch (IOException ex) {
            throw new TeXExportException(ex);
        } finally {
            try {
                zipOut.flush();
                zipOut.close();
            } catch (IOException ex) {
                throw new TeXExportException(ex);
            }
        }
    }
    
    private void export(String fileName) throws IOException {
        newEntry(fileName + ".tex");
        StringTemplate template = templateGroup.getInstanceOf(fileName);
        template.setAttribute("project", project);
        IOUtils.copy(new StringReader(template.toString()), zipOut);
    }
}
