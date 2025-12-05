package net.vanlew.mediamanager.library.renamers;

import net.vanlew.mediamanager.library.base.MediaRenamerBase;
import net.vanlew.mediamanager.library.common.MediaHelpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class VideoRenamer extends MediaRenamerBase {
    private final Logger log;

    public VideoRenamer(Logger logger) {
        this.log = (logger != null) ? logger : Logger.getLogger(VideoRenamer.class.getName());
    }

    public VideoRenamer() {
        this(null);
    }

    @Override
    public void execute() {
        Path sourcePath = Paths.get(getSourceDirectory());
        int maxDepth = isRecurseSourceDirectory() ? Integer.MAX_VALUE : 1;

        List<Path> videos;
        try (Stream<Path> s = Files.walk(sourcePath, maxDepth)) {
            videos = s.filter(Files::isRegularFile)
                    .filter(p -> MediaHelpers.SupportedVideoExtensions.toList()
                            .contains(getExtensionLower(p)))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to enumerate files in " + getSourceDirectory(), e);
            return;
        }

        log.info("   Processing " + videos.size() + " video candidate(s)...");

        int actualCount = 0;

        for (Path videoPath : videos) {
            File videoFile = videoPath.toFile();
            try {
                boolean needsRename = isForceRename() || !MediaHelpers.fileNameFormatIsCorrect(videoFile.getName());
                if (needsRename) {
                    Date mediaDate = extractDateEncoded(videoFile);
                    if (mediaDate != null) {
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(mediaDate);
                        String formattedDateTime = String.format("%04d:%02d:%02d %02d:%02d:%02d",
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH) + 1,
                                cal.get(Calendar.DAY_OF_MONTH),
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                cal.get(Calendar.SECOND));

                        String newVideoName = MediaHelpers.getNewFileName(mediaDate, videoFile);
                        Path newVideoPath = videoPath.getParent().resolve(newVideoName);

                        // Placeholder: integrate ExifTool for tag writing if needed
                        log.info("      Updating video '" + videoFile.getName() + "'");

                        if (isWhatIfScenario()) {
                            try {
                                Files.move(videoPath, newVideoPath, StandardCopyOption.ATOMIC_MOVE);
                            } catch (AtomicMoveNotSupportedException ame) {
                                Files.move(videoPath, newVideoPath);
                            }
                        }
                        log.fine("         Done");
                        actualCount++;
                    }
                }
            } catch (Exception ex) {
                log.log(Level.SEVERE, "Error processing file " + videoPath, ex);
            }
        }

        log.info("   " + actualCount + " video(s) renamed");
    }

    private Date extractDateEncoded(File videoFile) {
        // Use metadata-extractor or similar to get DateEncoded
        // Placeholder: returns last modified date if metadata not available
        try {
            return new Date(videoFile.lastModified());
        } catch (Exception e) {
            return null;
        }
    }

    private String getExtensionLower(Path p) {
        String name = p.getFileName().toString();
        int idx = name.lastIndexOf('.');
        return (idx >= 0) ? name.substring(idx).toLowerCase(Locale.ROOT) : "";
    }
}