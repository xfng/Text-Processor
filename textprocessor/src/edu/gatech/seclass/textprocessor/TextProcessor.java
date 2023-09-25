package edu.gatech.seclass.textprocessor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextProcessor implements TextProcessorInterface{

    private String inputFile;

    private HashMap<String, List<String>> options = new HashMap<>();


    @Override
    public void reset() {

    }

    @Override
    public void setFilepath(String filepath) {
        this.inputFile = filepath;
    }

    @Override
    public void setOutputFile(String outputFile) {
        List<String> parameters = new ArrayList<String>(1);
        parameters.add(outputFile);
        this.options.put("o", parameters);
    }

    @Override
    public void setCaseInsensitive(boolean caseInsensitive) {
        List<String> parameters = new ArrayList<String>(1);
        this.options.put("i", parameters);
    }

    @Override
    public void setKeepLines(String keepLines) {
        List<String> parameters = new ArrayList<String>(1);
        parameters.add(keepLines);
        this.options.put("k", parameters);
    }

    @Override
    public void setReplaceText(String oldString, String newString) {
        List<String> parameters = new ArrayList<String>(2);
        parameters.add(oldString);
        parameters.add(newString);
        this.options.put("r", parameters);
    }

    @Override
    public void setAddPaddedLineNumber(int padding) {
        List<String> parameters = new ArrayList<String>(1);
        parameters.add(String.valueOf(padding));
        this.options.put("n", parameters);
    }

    @Override
    public void setRemoveWhitespace(boolean removeWhitespace) {
        List<String> parameters = new ArrayList<String>(1);
        this.options.put("w", parameters);
    }

    @Override
    public void setSuffixLines(String suffixLines) {
        List<String> parameters = new ArrayList<String>(1);
        parameters.add(suffixLines);
        this.options.put("s", parameters);
    }

    @Override
    public void textprocessor() throws TextProcessorException {

//        if (this.inputFile == null || !Files.exists(Path.of(this.inputFile)) || Files.isDirectory(Path.of(this.inputFile))){
//            throw new TextProcessorException("test");
//        }

        if(this.inputFile == null){
            throw new TextProcessorException("test2");
        }

        if(!Files.exists(Path.of(this.inputFile))){
            throw new TextProcessorException("test3");
        }

        if (Files.isDirectory(Path.of(this.inputFile))){
            throw new TextProcessorException("test3");
        }




        // check empty input
        File file = new File(this.inputFile);
        if (file.length() == 0){
            try {
                processEmptyInput();
                return;
            } catch (IOException e) {
                throw new TextProcessorException("this is a test");
            }
        }


        try {
            if (checkCommand() && checkTerminated(Path.of(this.inputFile))) {
                System.out.println("check0");
                processText();
            } else{
                throw new TextProcessorException("test4");
            }
        } catch (IOException e) {
            throw new TextProcessorException("this is a test");
        }
    }

    private void processText() throws TextProcessorException{

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(this.inputFile));
            String line;
            List<String> newLines = new ArrayList<>();
            int lineNumber = 1;
            while((line=br.readLine())!=null){
                String newLine = processLine(line, lineNumber);
                newLines.add(newLine);
//                if (newLine != ""){
//                    newLines.add(newLine);
//                }
                lineNumber ++;
            }
            br.close();
            System.out.println("check2");
            if (hasOption("o")){
                String outputFile = getParameters("o").get(0);
                saveOutput(newLines, outputFile);
            } else {
                for (String l : newLines) {
                    saveOutput(newLines, this.inputFile);
//                    System.out.print(l + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            throw new TextProcessorException("test5");
        }
    }

    public String processLine(String line, int lineNumber){

        if (hasOption("k")){
            String subStr = getParameters("k").get(0);
            if (subStr == ""){
                line = line;
            } else {
                if (hasOption("i")){
                    if (line.toLowerCase().contains(subStr.toLowerCase())){
                        line = line;
                    } else {
//                        return "";
                        line = "";
                        System.out.println(line);
                    }
                } else {
                    if (line.contains(subStr)){
                        line = line;
                    } else {
//                        return "";
                        line = "";
                    }
                }
            }
        }

        if (hasOption("r")) {
            String oldStr = getParameters("r").get(0);
            String newStr = getParameters("r").get(1);

            if (hasOption("i")){
                if (line.toLowerCase().contains(oldStr.toLowerCase())){
                    line = replaceFirstIgnoreCase(line, oldStr, newStr);
                }
            } else {
                if (line.contains(oldStr)){
                    line = replaceFirst(line, oldStr, newStr);
                }
            }
        }

        if (hasOption("n")) {
            String nPadding = getParameters("n").get(0);
            int n = Integer.parseInt(nPadding);
            DecimalFormat df = new DecimalFormat("0".repeat(n));
            String paddingLineNumber = df.format(lineNumber);
            line = paddingLineNumber + " " + line;
        } else if (hasOption("w")) {
            line = line.replaceAll("\\s+","");
        }

        if (hasOption("s")){
            String suffixArg = getParameters("s").get(0);
            line += suffixArg;
        }

        return line;
    }

    public static String replaceFirst(String s, String pattern, String replacement) {
        int idx = s.indexOf(pattern);
        return s.substring(0, idx) + replacement + s.substring(idx + pattern.length());
    }

    public static String replaceFirstIgnoreCase(String s, String pattern, String replacement) {
        String sLower = s.toLowerCase();
        String patternLower = pattern.toLowerCase();
        int idx = sLower.indexOf(patternLower);
        return s.substring(0, idx) + replacement + s.substring(idx + pattern.length());
    }

    private void saveOutput(List<String> lines, String outputFile) throws IOException {
        FileWriter writer = new FileWriter(outputFile);
        for(String line: lines) {
            writer.write(line + System.lineSeparator());
        }
        writer.close();
    }

    private Boolean hasOption(String option){
        if (this.options.containsKey(option)){
            return true;
        }
        return false;
    }

    private List<String> getParameters(String option){
        return this.options.get(option);
    }

    private boolean checkCommand(){
        if ((hasOption("r") && hasOption("k")) || (hasOption("n") && hasOption("w"))){
            return false;
        }
        if (hasOption("i")){
            if (!hasOption("r") && !hasOption("k")) {
                return false;
            }
        }
        if (hasOption("o")){
            String parameter = getParameters("o").get(0);
            Path path = Paths.get(parameter);
            if (Files.exists(path) || Files.isDirectory(path)) {
                return false;
            }
        }

        return true;
    }

    private boolean checkTerminated(Path filepath) throws IOException {

        String content = Files.readString(filepath);
        if (content != "" && !content.contains(System.lineSeparator())){
            return false;
        }

        return true;
    }

    private void processEmptyInput() throws IOException {
        if (hasOption("o")){
            FileWriter writer = new FileWriter(getParameters("o").get(0));
            writer.write("");
            writer.close();
        } else {
            System.out.print("");
        }
    }

}
