package edu.gatech.seclass.textprocessor;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            usage();
            System.exit(1);
        }

        String inputFile = args[args.length - 1];
        TextProcessor textProcessor = new TextProcessor();
        textProcessor.setFilepath(inputFile);

        for (int i = 0; i < args.length - 1; i++) {
            String option = args[i];
            switch (option) {
                case "-i":
                    textProcessor.setCaseInsensitive(true);
                    break;
                case "-k":
                    if (i + 1 < args.length - 1) {
                        textProcessor.setKeepLines(args[i + 1]);
                        i++;
                    } else {
                        System.err.println("Missing argument for -k option.");
                        System.exit(1);
                    }
                    break;
                case "-r":
                    if (i + 2 < args.length - 1) {
                        textProcessor.setReplaceText(args[i + 1], args[i + 2]);
                        i += 2;
                    } else {
                        System.err.println("Missing arguments for -r option.");
                        System.exit(1);
                    }
                    break;
                case "-n":
                    if (i + 1 < args.length - 1) {
                        textProcessor.setAddPaddedLineNumber(Integer.parseInt(args[i + 1]));
                        i++;
                    } else {
                        System.err.println("Missing argument for -n option.");
                        System.exit(1);
                    }
                    break;
                case "-w":
                    textProcessor.setRemoveWhitespace(true);
                    break;
                case "-s":
                    if (i + 1 < args.length - 1) {
                        textProcessor.setSuffixLines(args[i + 1]);
                        i++;
                    } else {
                        System.err.println("Missing argument for -s option.");
                        System.exit(1);
                    }
                    break;
                case "-o":
                    if (i + 1 < args.length - 1){
                        textProcessor.setOutputFile(args[i + 1]);
                        i ++;
                    } else {
                        System.err.println("Missing argument for -o option.");
                        System.exit(1);
                    }
                    break;
                default:
                    usage();
                    System.exit(1);
            }
        }

        try {
            textProcessor.textprocessor();
        } catch (TextProcessorException e) {
            System.err.println("Text processing failed: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void usage() {
        System.err.println("Usage: textprocessor [ -o filename | -i | -k substring | -r old new | -n padding | -w | -s suffix ] FILE");
    }
}
