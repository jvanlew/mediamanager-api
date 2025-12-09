package net.vanlew.mediamanager.library.renamers;

import java.io.File;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.logging.Logger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PhotoRenamerTests {

    private Logger logger = Logger.getLogger(PhotoRenamerTests.class.getName());

    @Test
    @Order(1)
    void photoRenamer_CanBeConstructed() {
        PhotoRenamer renamer = new PhotoRenamer();
        assertThat(renamer).isNotNull();

        PhotoRenamer renamerWithLogger = new PhotoRenamer(logger);
        assertThat(renamerWithLogger).isNotNull();
    }

    @Test
    @Order(2)
    void photoRenamer_ExtractDate_InvalidFiles() {
        PhotoRenamer renamer = new PhotoRenamer();
        var result = renamer.extractDate(null);
        assertThat(result).isNull();

        result = renamer.extractDate(new java.io.File("nonexistentfile.jpg"));
        assertThat(result).isNull();

        // test for invalid file type
        result = renamer.extractDate(new java.io.File("invalidfile.txt"));
        assertThat(result).isNull();
    }

    @Test
    @Order(3)
    void photoRenamer_ExtractDate_ValidFile() {
        PhotoRenamer renamer = new PhotoRenamer();
        var renameFilesDir = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testFiles").toString();

        var testImagePath = Paths.get(renameFilesDir, "extract-date-photo.jpg").toString();
        var jpgDate = LocalDateTime.ofInstant(renamer.extractDate(new File(testImagePath)).toInstant(), java.time.ZoneId.systemDefault());
        assertThat(jpgDate).isNotNull();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        LocalDateTime expectedJpgDate = LocalDateTime.parse("2020-08-01 06:32:06.000", formatter);
        assertThat(jpgDate).isEqualTo(expectedJpgDate);

        var testHeicPath = Paths.get(renameFilesDir, "file1.heic").toString();
        var heicDate = LocalDateTime.ofInstant(renamer.extractDate(new File(testHeicPath)).toInstant(), java.time.ZoneId.systemDefault());
        assertThat(heicDate).isNotNull();
        DateTimeFormatter heicFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH);
        LocalDateTime heicDateExpected = LocalDateTime.parse("2019-03-02 09:33:25.000", heicFormatter);
        assertThat(heicDate).isEqualTo(heicDateExpected);
    }

    @Test
    @Order(4)
    void photoRenamer_getUniqueFilename_WorksAsExpected() {
        PhotoRenamer renamer = new PhotoRenamer();
        var basePath = Paths.get(System.getProperty("user.dir"), "src", "test", "resources", "testFiles", "uniqueTestFile.txt").toString();

        // First call should return the original path
        var uniquePath1 = renamer.getUniqueFilename(basePath);
        assertThat(uniquePath1).isEqualTo(basePath);
    }
}
