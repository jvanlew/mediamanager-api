package net.vanlew.mediamanager.library.renamers;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import lombok.SneakyThrows;
import net.vanlew.mediamanager.library.base.MediaRenamerBase;
import net.vanlew.mediamanager.library.common.MediaHelpers;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class PhotoRenamer extends MediaRenamerBase {
    private final Logger log;

    public PhotoRenamer(Logger logger) {
        this.log = (logger != null) ? logger : Logger.getLogger(PhotoRenamer.class.getName());
    }

    public PhotoRenamer() {
        this(null);
    }

    @Override
    public void execute() {
        Path sourcePath = Paths.get(getSourceDirectory());
        int maxDepth = isRecurseSourceDirectory() ? Integer.MAX_VALUE : 1;

        List<Path> photos;
        try (Stream<Path> s = Files.walk(sourcePath, maxDepth)) {
            photos = s.filter(Files::isRegularFile)
                    .filter(p -> MediaHelpers.SupportedPhotoExtensions.toList()
                            .contains(getExtensionLower(p)))
                    .sorted()
                    .toList();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Failed to enumerate files in " + getSourceDirectory(), e);
            return;
        }

        log.info("   Processing " + photos.size() + " photo candidate(s)...");

        int actualCount = 0;
        int skippedCount = 0;

        for (Path photoPath : photos) {
            File photoFile = photoPath.toFile();
            try {
                boolean needsRename = isForceRename() || !MediaHelpers.fileNameFormatIsCorrect(photoFile.getName());
                if (needsRename) {

                    var dateOriginal = extractDate(photoFile);

                    if (dateOriginal != null) {
                        String newPhotoName = MediaHelpers.getNewFileName(dateOriginal, photoFile);
                        Path newPhotoPath = photoPath.getParent().resolve(newPhotoName);

                        // If EXIF writing were implemented, it would occur here.
                        if (!photoFile.getName().equals(newPhotoName) || isForceRename()) {
                            newPhotoPath = Paths.get(getUniqueFilename(newPhotoPath.toString()));
                            newPhotoName = newPhotoPath.getFileName().toString();
                            log.info("      Renaming '" + photoFile.getName() + "' to '" + newPhotoName + "'");
                            if (isWhatIfScenario()) {
                                try {
                                    Files.move(photoPath, newPhotoPath, StandardCopyOption.ATOMIC_MOVE);
                                } catch (AtomicMoveNotSupportedException ame) {
                                    Files.move(photoPath, newPhotoPath);
                                }
                            }
                            log.fine("         Done");
                            actualCount++;
                        } else {
                            log.fine("      Skipping file '" + photoPath + "'");
                            skippedCount++;
                        }
                    } else {
                        log.info("      Unable to extract date for '" + photoPath + "'... Skipping");
                        skippedCount++;
                    }
                } else {
                    log.fine("      Skipping file '" + photoPath + "'");
                    skippedCount++;
                }
            } catch (Exception ex) {
                log.log(Level.SEVERE, "Error processing file " + photoPath, ex);
            }
        }

        log.info("   " + actualCount + " photo(s) renamed");
        log.info("   " + skippedCount + " photo(s) skipped");
    }

    @SneakyThrows
    public Date extractDate(File photoFile) {
        if (photoFile==null || !photoFile.exists()) {
            return null;
        }

        var metadata = ImageMetadataReader.readMetadata(photoFile);
        // obtain the Exif directory
        ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);

        // query the tag's value
        Date date = directory.getDate(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED, TimeZone.getDefault());
        if (date == null) {
            date = directory.getDateDigitized();
        }
        return date;
    }

    public String getUniqueFilename(String photoPath) {
        Path path = Paths.get(photoPath);
        String originalCandidateName = removeExtension(path.getFileName().toString());
        int count = 1;
        while (Files.exists(path)) {
            String oldCandidateName = removeExtension(path.getFileName().toString());
            String newCandidateName = originalCandidateName + "-" + count;
            String newFileName = newCandidateName + getExtensionWithDot(path.getFileName().toString());
            path = path.getParent().resolve(newFileName);
            count++;
        }
        return path.toString();
    }

    private String getExtensionLower(Path p) {
        String name = p.getFileName().toString();
        int idx = name.lastIndexOf('.');
        return (idx >= 0) ? name.substring(idx).toLowerCase(Locale.ROOT) : "";
    }

    private String getExtensionWithDot(String filename) {
        int idx = filename.lastIndexOf('.');
        return (idx >= 0) ? filename.substring(idx) : "";
    }

    private String removeExtension(String filename) {
        int idx = filename.lastIndexOf('.');
        return (idx >= 0) ? filename.substring(0, idx) : filename;
    }
}
