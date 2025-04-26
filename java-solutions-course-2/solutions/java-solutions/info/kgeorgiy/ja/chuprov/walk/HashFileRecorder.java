package info.kgeorgiy.ja.chuprov.walk;

import java.io.IOException;
import java.io.BufferedWriter;

public class HashFileRecorder {
    private final String errorHash;
    private final BufferedWriter writer;

    public HashFileRecorder(BufferedWriter writer, String errorHash) {
        this.errorHash = errorHash;
        this.writer = writer;
    }

    public void writeIn(String output, String fileName) {
        try {
            writer.write(output + " " + fileName);
            writer.newLine();
        } catch (IOException e) {
            System.out.format("Error writing file: %s\n", fileName);
        }
    }

    public void writeErrIn(String fileName) {
        writeIn(errorHash, fileName);
    }
}
