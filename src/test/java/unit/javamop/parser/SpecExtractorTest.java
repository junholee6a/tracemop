package javamop.parser;

import examples.ExamplesIT;
import javamop.NodeEquivalenceChecker;
import javamop.helper.MOP_Serialization;
import javamop.parser.ast.MOPSpecFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertTrue;

/**
 * Created by He Xiao on 3/19/2016.
 * Check the parsing function provided in javamop.parser.SpecExtractor class:
 * The method 'static public MOPSpecFile parse(final File file);' will
 * parse an mop specification file and get a MOPSpecFile object.
 * The unit tests defined here will compare the MOPSpecFile output (AST)
 * produced by current implementation of the parser module and the trusted one (AST)
 * produced by JavaMOP V4.4 (Github)
 */
@RunWith(Parameterized.class)
public class SpecExtractorTest {
    private String mopFilePath;
    private String expected_AST_Path;
    public static final String astPrefix = "src" + File.separator + "test" + File.separator
            + "resources" + File.separator;

    public SpecExtractorTest(int testId) {
        this.mopFilePath = (String) ((ArrayList<Object[]>) ExamplesIT.data()).get(testId)[0];
        String testName = this.mopFilePath.substring
                (this.mopFilePath.lastIndexOf(File.separator) + 1);
        this.expected_AST_Path = astPrefix + testName + ".ser";
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        int[] inputArr = MOP_Serialization.selectedTestCases;
        Object[][] inputObjArr = new Object[inputArr.length][1];
        for (int i = 0; i < inputArr.length; i++) {
            inputObjArr[i][0] = inputArr[i];
        }
        return Arrays.asList(inputObjArr);
    }


    @Test
    public void parse() throws Exception {
        //the MOPSpecFile ast generated by current parser
        MOPSpecFile specFile = SpecExtractor.parse(new File(this.mopFilePath));

        MOPSpecFile expectedSpecFileAST = MOP_Serialization.
                readMOPSpecObjectFromFile(this.expected_AST_Path);

        assertTrue("The MOPSpecFile object of " + this.mopFilePath + " is not as expected.",
                NodeEquivalenceChecker.equalMOPSpecFiles(specFile, expectedSpecFileAST));
    }
}