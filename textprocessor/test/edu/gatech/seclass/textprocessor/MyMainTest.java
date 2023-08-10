package edu.gatech.seclass.textprocessor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.api.io.TempDir;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;


@Timeout(value = 1, threadMode = Timeout.ThreadMode.SEPARATE_THREAD)
public class MyMainTest {
    // Place all  of your tests in this class, optionally using MainTest.java as an example
    private final String usageStr =
            "Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE"
                    + System.lineSeparator();

    @TempDir
    Path tempDirectory;

    @RegisterExtension
    OutputCapture capture = new OutputCapture();

    /*
     * Test Utilities
     */

    private Path createFile(String contents) throws IOException {
        return createFile(contents, "input.txt");
    }

    private Path createFile(String contents, String fileName) throws IOException {
        Path file = tempDirectory.resolve(fileName);
        Files.write(file, contents.getBytes(StandardCharsets.UTF_8));

        return file;
    }

    private String getFileContent(Path file) {
        try {
            return Files.readString(file, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Test Cases
     */
    // Frame #: 1
    @Test
    public void textprocessorTest1() throws IOException {
        String input = "" + System.lineSeparator();
        String expected = "" + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 2
    @Test
    public void textprocessorTest2() throws IOException {
        String input = "This is the test2";
        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(!capture.stderr().isEmpty());
    }


    // Frame #: 3
    @Test
    public void textprocessorTest3() throws IOException {
        String input = "This is the test3." + System.lineSeparator();
        String expected = "This is the test3." + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stdout().isEmpty());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(expected, getFileContent(outputFile));
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 4
    @Test
    public void textprocessorTest4() throws IOException {
        String input = "test the repeated option -i." + System.lineSeparator();
        String expected = "test the repeated option -i." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "abc", "ABC", "-i", "-r", "Test", "test", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    // Frame #: 5
    @Test
    public void textprocessorTest5() throws IOException {
        String input = "Test the option -i." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-i", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 6
    @Test
    public void textprocessorTest6() throws IOException {
        String input = "Test option -r and -k." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "new", "New", "-k", "new", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 7
    @Test
    public void textprocessorTest7() throws IOException {
        String input = "test repeated option -r." + System.lineSeparator();
        String expected = "Test repeated option -r." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "new", "New", "-r", "test", "Test", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 8
    @Test
    public void textprocessorTest8() throws IOException {
        String input = "Test repeated option -k." + System.lineSeparator()
                + "test again." + System.lineSeparator();
        String expected = "Test repeated option -k." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", "new", "-k", "Test", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 9
    @Test
    public void textprocessorTest9() throws IOException {
        String input = "Test option -n and -w." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());

    }

    // Frame #: 10
    @Test
    public void textprocessorTest10() throws IOException {
        String input = "Test repeated option -n." + System.lineSeparator();
        String expected = "01 Test repeated option -n." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-n", "2", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 11
    @Test
    public void textprocessorTest11() throws IOException {
        String input = "Test repeated option -w." + System.lineSeparator();
        String expected = "Testrepeatedoption-w." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-w", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 12
    @Test
    public void textprocessorTest12() throws IOException {
        String input = "Test repeated option -s." + System.lineSeparator();
        String expected = "Test repeated option -s.222" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "#", "-s", "222", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 13
    @Test
    public void textprocessorTest13() throws IOException {
        String input = "Happy Friday." + System.lineSeparator();
        String expected = "Happy Friday." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertEquals(input, getFileContent(inputFile));
        Assertions.assertTrue(capture.stderr().isEmpty());
    }

    // Frame #: 14
    @Test
    public void textprocessorTest14() throws IOException {
        String input = "Happy Friday." + System.lineSeparator();
        String expected = "Happy Friday." + System.lineSeparator();
        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());

    }

    // Frame #: 15
    @Test
    public void textprocessorTest15() throws IOException {
        String input = "Happy Friday." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 16
    @Test
    public void textprocessorTest16() throws IOException {
        String input = "Happy Friday." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 17
    @Test
    public void textprocessorTest17() throws IOException {
        String input = "Happy Friday." + System.lineSeparator()
                + "Wonderful Friday." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-k", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 18
    @Test
    public void textprocessorTest18() throws IOException {
        String input = "Organic Raisins1." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 19
    @Test
    public void textprocessorTest19() throws IOException {
        String input = "Organic Raisins2." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "5.5", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }


    // Frame #: 20
    @Test
    public void textprocessorTest20() throws IOException {
        String input = "Organic Raisins3." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "20", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 21
    @Test
    public void textprocessorTest21() throws IOException {
        String input = "Organic Raisins4." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 22
    @Test
    public void textprocessorTest22() throws IOException {
        String input = "Organic Raisins4." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    // Frame #: 23
    @Test
    public void textprocessorTest23() throws IOException {
        String input = "delicious jalapeno chips." + System.lineSeparator();
        String expected = "1 Delicious jalapeno chips.#" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "delicious", "Delicious", "-n", "1", "-s", "#",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 24
    @Test
    public void textprocessorTest24() throws IOException {
        String input = "delicious jalapeno chips." + System.lineSeparator();
        String expected = "1 Delicious jalapeno chips." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "delicious", "Delicious", "-n", "1", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    // Frame #: 25
    @Test
    public void textprocessorTest25() throws IOException {
        String input = "delicious jalapeno chips." + System.lineSeparator();
        String expected = "Deliciousjalapenochips.#" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "delicious", "Delicious","-w", "-s", "#", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 26
    @Test
    public void textprocessorTest26() throws IOException {
        String input = "delicious jalapeno chips." + System.lineSeparator();
        String expected = "Deliciousjalapenochips." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "delicious", "Delicious", "-w", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 27
    @Test
    public void textprocessorTest27() throws IOException {
        String input = "delicious Jalapeno chips." + System.lineSeparator();
        String expected = "Delicious Jalapeno chips.#" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "delicious", "Delicious", "-s", "#",inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 28
    @Test
    public void textprocessorTest28() throws IOException {
        String input = "delicious jalapeno chips." + System.lineSeparator();
        String expected = "Delicious jalapeno chips." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-r", "delicious", "Delicious", inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 29
    @Test
    public void textprocessorTest29() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "01 A good sentence is a complete sentence.#" + System.lineSeparator()
                + "02 A bad sentence is not completed.#" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", "-n", "2", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 30
    @Test
    public void textprocessorTest30() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "1 A good sentence is a complete sentence.#" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "good", "-n", "1", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 31
    @Test
    public void textprocessorTest31() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "1 A good sentence is a complete sentence." + System.lineSeparator()
                + "2 A bad sentence is not completed." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", "-n", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 32
    @Test
    public void textprocessorTest32() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "2 A bad sentence is not completed." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "bad", "-n", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 33
    @Test
    public void textprocessorTest33() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "Agoodsentenceisacompletesentence.#" + System.lineSeparator()
                + "Abadsentenceisnotcompleted.#" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", "-w", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 34
    @Test
    public void textprocessorTest34() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "Agoodsentenceisacompletesentence.#" + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-k", "good", "-w", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 35
    @Test
    public void textprocessorTest35() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "Agoodsentenceisacompletesentence." + System.lineSeparator()
                + "Abadsentenceisnotcompleted." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 36
    @Test
    public void textprocessorTest36() throws IOException {
        String input = "A good sentence is a complete sentence." + System.lineSeparator()
                + "A bad sentence is not completed." + System.lineSeparator();
        String expected = "Abadsentenceisnotcompleted." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "bad", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 37
    @Test
    public void textprocessorTest37() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paint a picture.#" + System.lineSeparator()
                + "Play the music.#" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 38
    @Test
    public void textprocessorTest38() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected =  "Play the music.#" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "music", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    // Frame #: 39
    @Test
    public void textprocessorTest39() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-k", "", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }


    // Frame #: 40
    @Test
    public void textprocessorTest40() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paint a picture." + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-k", "picture", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 41
    @Test
    public void textprocessorTest41() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "1 Paint a picture.#" + System.lineSeparator()
                + "2 Play the music.#" + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 42
    @Test
    public void textprocessorTest42() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "1 Paint a picture." + System.lineSeparator()
                + "2 Play the music." + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-n", "1", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 43
    @Test
    public void textprocessorTest43() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paintapicture.#" + System.lineSeparator()
                + "Playthemusic.#" + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-w", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 44
    @Test
    public void textprocessorTest44() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paintapicture." + System.lineSeparator()
                + "Playthemusic." + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 45
    @Test
    public void textprocessorTest45() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paint a picture.#" + System.lineSeparator()
                + "Play the music.#" + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 46
    @Test
    public void textprocessorTest46() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {inputFile.toString()};
        Main.main(args);
        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 47
    @Test
    public void textprocessorTest47() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "1 play a picture.#" + System.lineSeparator()
                + "2 Play the music.#" + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "paint", "play", "-n", "1", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 48
    @Test
    public void textprocessorTest48() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "play the music." + System.lineSeparator();
        String expected = "1 play a picture." + System.lineSeparator()
                + "2 play the music." + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "paint", "play", "-n", "1",  inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 49
    @Test
    public void textprocessorTest49() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "playapicture.#" + System.lineSeparator()
                + "Playthemusic.#" + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "paint", "play", "-w", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 50
    @Test
    public void textprocessorTest50() throws IOException {
        String input = "Paint a picture." + System.lineSeparator()
                + "Play the music." + System.lineSeparator();
        String expected = "playapicture." + System.lineSeparator()
                + "Playthemusic." + System.lineSeparator();


        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "paint", "play", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 51
    @Test
    public void textprocessorTest51() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Good evening.#" + System.lineSeparator()
                + "Good evening.#" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "Morning", "evening", "-s", "#", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 52
    @Test
    public void textprocessorTest52() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Good evening." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-r", "Morning", "evening", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    // Frame #: 53
    @Test
    public void textprocessorTest53() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "01 Good morning.!" + System.lineSeparator()
                + "02 Good evening.!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "", "-n", "2", "-s", "!",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));



    }

    // Frame #: 54
    @Test
    public void textprocessorTest54() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "01 Good morning.!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "Morning", "-n", "2", "-s", "!",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }


    // Frame #: 55
    @Test
    public void textprocessorTest55() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "01 Good morning." + System.lineSeparator()
                +"02 Good evening." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "", "-n", "2",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 56
    @Test
    public void textprocessorTest56() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "01 Good morning." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "Morning", "-n", "2",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 57
    @Test
    public void textprocessorTest57() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Goodmorning.!" + System.lineSeparator()
                + "Goodevening.!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "", "-w", "-s","!", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 58
    @Test
    public void textprocessorTest58() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Goodmorning.!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "Morning", "-w", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 59
    @Test
    public void textprocessorTest59() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Goodmorning." + System.lineSeparator()
                + "Goodevening." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 60
    @Test
    public void textprocessorTest60() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Goodmorning." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "Morning", "-w", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 61
    @Test
    public void textprocessorTest61() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Good morning.@" + System.lineSeparator()
                + "Good evening.@" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "", "-s", "@", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 62
    @Test
    public void textprocessorTest62() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Good morning.@" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "Morning", "-s", "@", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 63
    @Test
    public void textprocessorTest63() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    // Frame #: 64
    @Test
    public void textprocessorTest64() throws IOException {
        String input = "Good morning." + System.lineSeparator()
                + "Good evening." + System.lineSeparator();
        String expected = "Good morning." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-k", "Morning",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    @Test
    public void textprocessorTest65() throws IOException {
        String input = "Good morning." + System.lineSeparator();
        String expected = "01 Good morning." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-n", "2",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    @Test
    public void textprocessorTest66() throws IOException {
        String input = "Good morning." + System.lineSeparator();
        String expected = "Good morning.!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-s", ".", "-s", "!",inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    @Test
    public void textprocessorTest67() throws IOException {
        String input = "Good morning." + System.lineSeparator();
        String expected = "01 Good morning.!" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-n", "2", "-s", ".", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    @Test
    public void textprocessorTest68() throws Exception {
        String input = "This list contains words that start with -k:" + System.lineSeparator()
                + "-ksde" + System.lineSeparator();
        String expected = "This list contains words that start with -s:" + System.lineSeparator()
                + "-ssde" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", "-k", "-s", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void textprocessorTest69() throws Exception {
        String input = "This list contains words that start with -k:" + System.lineSeparator()
                + "-ksde" + System.lineSeparator();
        String expected = "This list contains words that start with -s:" + System.lineSeparator()
                + "-ssde" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-r", "-k", "-s", "-i", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
    }

    @Test
    public void textprocessorTest70() throws IOException {
        String input = "Good morning." + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-i", "-n", "1", "-n", "2", "-s", ".", "-s", "!", inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    @Test
    public void textprocessorTest71() throws Exception {
        String input = "This list contains words that start with -k:" + System.lineSeparator()
                + "-ksde" + System.lineSeparator();
        String expected = "This list contains words that start with -s:" + System.lineSeparator()
                + "-ssde" + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-r", "-k", "-s", "-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
        Assertions.assertEquals(expected, getFileContent(outputFile));
    }

    @Test
    public void textprocessorTest72() throws Exception {
        String input = "" + System.lineSeparator();
        String expected = "" + System.lineSeparator();

        Path inputFile = createFile(input);
        Path outputFile = tempDirectory.resolve("output.txt");
        String[] args = {"-r", "-k", "-s", "-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
        Assertions.assertEquals(expected, getFileContent(outputFile));
    }

    @Test
    public void textprocessorTest73() throws IOException {
        String input = "Organic Raisins3." + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-n", "string", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

    @Test
    public void textprocessorTest74() throws IOException {
        String input = "Good morning." + System.lineSeparator();
        String expected = "01 Good morning.-k" + System.lineSeparator();

        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-n", "2", "-s", ".", "-s", "-k", inputFile.toString()};
        Main.main(args);

        Assertions.assertEquals(expected, capture.stdout());
        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));

    }

    @Test
    public void textprocessorTest75() throws IOException {
        String input = "Good morning." + System.lineSeparator();
        String expected = "01 Good morning.-k" + System.lineSeparator();
        Path outputFile = tempDirectory.resolve("output.txt");
        Path inputFile = createFile(input);
        String[] args = {"-n", "1", "-n", "2", "-s", ".", "-s", "-k", "-o", outputFile.toString(), inputFile.toString()};
        Main.main(args);

        Assertions.assertTrue(capture.stderr().isEmpty());
        Assertions.assertEquals(input, getFileContent(inputFile));
        Assertions.assertEquals(expected, getFileContent(outputFile));
    }

    @Test
    public void textprocessorTest76() throws IOException {
        String input = "Organic Raisins3. test" + System.lineSeparator();
        Path inputFile = createFile(input);
        String[] args = {"-s", "-k", "test", inputFile.toString()};
        Main.main(args);
        Assertions.assertTrue(!capture.stderr().isEmpty());
    }

}
