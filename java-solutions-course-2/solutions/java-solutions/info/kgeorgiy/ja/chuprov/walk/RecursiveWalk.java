package info.kgeorgiy.ja.chuprov.walk;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RecursiveWalk {
    private static final String errorHash = "0".repeat(16);
    private static final String algorithm = "SHA-256";
    protected static volatile boolean isRecursive = true; // :NOTE: ??????

    private static void handlePath(String name, BufferedWriter writer) {
        HashFileRecorder hashFileRecorder = new HashFileRecorder(writer, errorHash);
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.err.format("Hash algorithm not found: %s", e.getMessage());
            return;
        }

        try {
            Files.walkFileTree(Path.of(name), new HashWritingFileVisitor(hashFileRecorder, digest, isRecursive));
        } catch (InvalidPathException e) {
            System.err.println("No such file or directory: " + name);
            hashFileRecorder.writeErrIn(name);
        } catch (IOException e) {
            System.err.format("IO exception processing file/directory %s: %s%n", name, e.getMessage());
        }
    }

    private static boolean checkArgsAreValid(String[] args) {
        if (args == null) {
            System.err.println("Args is null");
            return false;
        }
        if (args.length != 2) {
            System.err.println("Usage Walk <input> <output>");
            return false;
        }
        if (args[0] == null || args[1] == null) {
            System.err.println("First or second argument is null");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        if (!checkArgsAreValid(args)) {
            return;
        }

        Path inputPath;
        Path outputPath;
        try {
            inputPath = Path.of(args[0]);
            outputPath = Path.of(args[1]);
            Path outputDir = outputPath.getParent();
            if (outputDir != null && !Files.exists(outputDir)) { // :NOTE: Files.exists(outputDir) redundant
                Files.createDirectories(outputDir);
            }
        } catch (InvalidPathException e) {
            System.err.println("Invalid path: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Error creating output directory: " + e.getMessage());
            return;
        }


        try (BufferedReader reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
                String line;
                try {
                    while ((line = reader.readLine()) != null) {
                        handlePath(line, writer);
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                }

            } catch (IOException e) {
                System.err.format("Output file opening error with file %s: %s\n", outputPath, e.getMessage());
            }
        } catch (IOException e) {
            System.err.format("Input file opening error with file %s: %s\n", inputPath, e.getMessage());
        }
    }
}
