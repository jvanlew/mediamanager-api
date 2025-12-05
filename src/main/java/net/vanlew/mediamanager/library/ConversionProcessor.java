package net.vanlew.mediamanager.library;

import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Getter
@Setter
public class ConversionProcessor implements AutoCloseable {
    private String inputDirectory;
    private String outputDirectory;
    private boolean recurseInputDirectory;
    private final Logger log;

    public ConversionProcessor(Logger logger) {
        this.log = (logger != null) ? logger : Logger.getLogger(ConversionProcessor.class.getName());
    }

    public ConversionProcessor() {
        this(null);
    }

    @Override
    public void close() {
        // No resources to clean up
    }

    public CompletableFuture<Void> execute(Consumer<String> startCallback, Consumer<Integer> finishCallback) {
        return CompletableFuture.runAsync(() -> {
            long allStart = System.currentTimeMillis();

            log.info("Starting Conversion Processor");
            log.info("Input directory: " + this.inputDirectory);
            log.info("Recurse input directory: " + this.recurseInputDirectory);
            log.info("Output directory: " + this.outputDirectory);

            Path outputDir = Paths.get(this.outputDirectory);
            if (!Files.exists(outputDir)) {
                log.fine("Directory '" + this.outputDirectory + "' does not exist, creating");
                try {
                    Files.createDirectories(outputDir);
                } catch (IOException e) {
                    log.log(Level.SEVERE, "Failed to create output directory", e);
                    return;
                }
            }

            int maxDepth = this.recurseInputDirectory ? Integer.MAX_VALUE : 1;
            List<Path> heicPhotos;
            try (Stream<Path> s = Files.walk(Paths.get(this.inputDirectory), maxDepth)) {
                heicPhotos = s.filter(Files::isRegularFile)
                        .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".heic"))
                        .toList();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Failed to enumerate HEIC files", e);
                return;
            }

            log.info("   Processing " + heicPhotos.size() + " photo conversion candidate(s)...");

            int actualCount = 0;
            long pStart = System.currentTimeMillis();

            for (Path heic : heicPhotos) {
                File photoFile = heic.toFile();
                String originalFilename = photoFile.getName();
                String newFile = outputDir.resolve(originalFilename.replaceAll("(?i)\\.heic$", ".jpg")).toString();

                log.info("      Converting '" + originalFilename + "' to '" + newFile + "'");
                try {
                    // Placeholder: Use JMagick, im4java, or external process to convert HEIC to JPG
                    // Example: MagickImage image = new MagickImage(heic.toString());
                    // image.write(newFile);
                } catch (Exception e) {
                    log.log(Level.SEVERE, "Failed to convert " + originalFilename, e);
                    continue;
                }
                log.fine("         Done");
                actualCount++;
            }

            long pEnd = System.currentTimeMillis();
            log.info("   " + actualCount + " photo(s) converted");
            log.info("All done. Completed in " + humanizeDuration(pEnd - allStart));
        });
    }

    private String humanizeDuration(long millis) {
        long seconds = millis / 1000;
        long min = seconds / 60;
        long sec = seconds % 60;
        return min + "m " + sec + "s";
    }
}