package info.kgeorgiy.ja.chuprov.walk;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

public class HashWritingFileVisitor extends SimpleFileVisitor<Path> {
    private static final byte[] buffer = new byte[8192];
    private final boolean isRecursive;
    private final HashFileRecorder hashFileRecorder;
    private final MessageDigest digest;

    public HashWritingFileVisitor(HashFileRecorder hashFileRecorder, MessageDigest digest, boolean isRecursive) {
        super();
        this.isRecursive = isRecursive;
        this.hashFileRecorder = hashFileRecorder;
        this.digest = digest;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        Objects.requireNonNull(dir);
        Objects.requireNonNull(attrs);
        if (isRecursive) {
            return FileVisitResult.CONTINUE;
        }
        hashFileRecorder.writeErrIn(dir.toString());
        return FileVisitResult.SKIP_SUBTREE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(attrs);
        byte[] hash;

        try (InputStream is = Files.newInputStream(file)) {
            digest.reset();
            int len;
            while ((len = is.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            byte[] fullHash = digest.digest();
            hash = Arrays.copyOfRange(fullHash, 0, 8);
        } catch (IOException e) {
            System.out.format("Error reading file: %s\n", file);
            hashFileRecorder.writeErrIn(file.toString());
            return FileVisitResult.CONTINUE;
        }

        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        hashFileRecorder.writeIn(hexString.toString(), file.toString());

        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        Objects.requireNonNull(file);
        hashFileRecorder.writeErrIn(file.toString());
        System.err.format("Error visiting file %s: %s\n ", file, exc.getMessage());
        return FileVisitResult.CONTINUE;
    }
}
