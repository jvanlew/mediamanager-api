package net.vanlew.mediamanager.library.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MediaHelpers {

    public static class SupportedPhotoExtensions {
        public static final String JPG = ".jpg";
        public static final String JPEG = ".jpeg";
        public static final String HEIC = ".heic";
        public static final String PNG = ".png";

        public static List<String> toList() {
            return Arrays.asList(
                    JPG, JPEG, HEIC, PNG,
                    JPG.toUpperCase(), JPEG.toUpperCase(), HEIC.toUpperCase(), PNG.toUpperCase()
            );
        }
    }

    public static class SupportedVideoExtensions {
        public static final String AVI = ".avi";
        public static final String MP4 = ".mp4";
        public static final String MOV = ".mov";
        public static final String MPG = ".mpg";
        public static final String MTS = ".mts";
        public static final String M4V = ".m4v";
        public static final String THREE_GP = ".3gp";

        public static List<String> toList() {
            return Arrays.asList(
                    AVI, MP4, MOV, MPG, MTS, M4V, THREE_GP,
                    AVI.toUpperCase(), MP4.toUpperCase(), MOV.toUpperCase(),
                    MPG.toUpperCase(), MTS.toUpperCase(), M4V.toUpperCase(),
                    THREE_GP.toUpperCase()
            );
        }
    }

    /**
     * Attempts to fix weird file extension issues and also tries to standardize on the lowercase extension format
     *
     * @param fullPath Full path of the directory to scan for files to fix
     */
    public static void fixExtensions(String fullPath) throws IOException {
        Path dir = Paths.get(fullPath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.JPG")) {
            for (Path photo : stream) {
                Path newFileName = photo.resolveSibling(photo.getFileName().toString().replace(".JPG", ".jpg"));
                Files.move(photo, newFileName, StandardCopyOption.REPLACE_EXISTING);
            }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.jpg.jpg")) {
            for (Path photo : stream) {
                Path newFileName = photo.resolveSibling(photo.getFileName().toString().replace(".jpg.jpg", ".jpg"));
                Files.move(photo, newFileName, StandardCopyOption.REPLACE_EXISTING);
            }
        }
    }

    /**
     * Determines whether the fileName is in a correct format.
     *
     * @param fileName The name of the file with extension but excluding directory information
     * @return true if format is correct, false otherwise
     */
    public static boolean fileNameFormatIsCorrect(String fileName) {
        String matchPattern = "(\\d{4})-(\\d{2})-(\\d{2}) (\\d{2})[.](\\d{2})[.](\\d{2})[.]([a-zA-Z][a-zA-Z][a-zA-Z0-9][a-zA-Z]?)";
        return Pattern.matches(matchPattern, fileName);
    }

    /**
     * Returns a new file name given the targetDate
     *
     * @param targetDate The date to be applied to the new file name format
     * @param file       The original file that is to be renamed
     * @return new file name
     */
    public static String getNewFileName(Date targetDate, File file) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(targetDate);

        String extension = file.getName().substring(file.getName().lastIndexOf('.')).toLowerCase();

        String newName = String.format("%04d-%02d-%02d %02d.%02d.%02d%s",
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),
                cal.get(Calendar.SECOND),
                extension);

        File dir = file.getParentFile();
        File newFile = new File(dir, newName);

        int suffix = 1;
        while (newFile.exists()) {
            newName = String.format("%s-%d%s",
                    newName.substring(0, newName.lastIndexOf(extension)),
                    suffix,
                    extension);
            newFile = new File(dir, newName);
            suffix++;
        }

        return newFile.getName();
    }
}
