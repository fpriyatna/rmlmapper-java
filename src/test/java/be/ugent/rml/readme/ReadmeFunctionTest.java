package be.ugent.rml.readme;

import be.ugent.rml.Executor;
import be.ugent.rml.Utils;
import be.ugent.rml.functions.FunctionLoader;
import be.ugent.rml.functions.lib.GrelProcessor;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.QuadStore;
import be.ugent.rml.store.RDF4JStore;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.Test;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class ReadmeFunctionTest {

    @Test
    public void function() {
        try {
            String mapPath = "./src/test/resources/argument/mapping.ttl"; //path to the mapping file that needs to be executed
            File mappingFile = new File(mapPath);

            // Use custom functions.ttl file
            String functionPath = "./src/test/resources/rml-fno-test-cases/functions_test.ttl";

            // Get the mapping string stream
            InputStream mappingStream = new FileInputStream(mappingFile);

            // Load the mapping in a QuadStore
            Model model = Rio.parse(mappingStream, "", RDFFormat.TURTLE);
            QuadStore rmlStore = new RDF4JStore(model);

            // Set up the basepath for the records factory, i.e., the basepath for the (local file) data sources
            RecordsFactory factory = new RecordsFactory(mappingFile.getParent());

            // Set up the functions used during the mapping
            Map<String, Class> libraryMap = new HashMap<>();
            libraryMap.put("GrelFunctions", GrelProcessor.class);

            File functionsFile = Utils.getFile(functionPath);
            FunctionLoader functionLoader = new FunctionLoader(functionsFile, null, libraryMap);

            // Set up the outputstore (needed when you want to output something else than nquads
            QuadStore outputStore = new RDF4JStore();

            // Create the Executor
            Executor executor = new Executor(rmlStore, factory, functionLoader, outputStore, Utils.getBaseDirectiveTurtle(mappingStream));

            // Execute the mapping
            QuadStore result = executor.execute(null);

            // Output the result
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
            result.write(out, "turtle");
            out.close();
        } catch (Exception e) {
            fail("No exception was expected.");
        }
    }
}
