package net.vanlew.mediamanager.library.common;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class MediaHelpersTests {

    //private final Logger log = Logger.getLogger(MediaHelpersTests.class.getName());

    @Test
    void mediaHelpers_SupportedPhotoExtensions_ShouldReturnExpectedExtensions() {
        assertThat(MediaHelpers.SupportedPhotoExtensions.toList())
                .contains(MediaHelpers.SupportedPhotoExtensions.HEIC, MediaHelpers.SupportedPhotoExtensions.JPG.toUpperCase());
    }

    @Test
    void mediaHelpers_SupportedVideoExtensions_ShouldReturnExpectedExtensions() {
        assertThat(MediaHelpers.SupportedVideoExtensions.toList())
                .contains(MediaHelpers.SupportedVideoExtensions.MP4, MediaHelpers.SupportedVideoExtensions.MOV.toUpperCase());
    }

    @Test
    void mediaHelpers_FileNameFormat_ShouldMatchExpectedPattern() {
        String fileName = "2025-12-01 23.05.34.jpg";
        assertThat(MediaHelpers.fileNameFormatIsCorrect(fileName)).isTrue();
    }

    @Test
    @SneakyThrows
    void mediaHelpers_FixExtensions_ShouldExecuteSuccessfully() {
        MediaHelpers mediaHelpers = mock(MediaHelpers.class);
        doNothing().when(mediaHelpers);
        MediaHelpers.fixExtensions(anyString());
    }

    @Test
    void mediaHelpers_GetNewFileName_ShouldReturnExpectedFileName() {
        String originalFileName = "IMG_20240101_123456.jpg";
        String actualNewFileName = MediaHelpers.getNewFileName(Date.from(Instant.now()), new File(originalFileName));
        assertThat(actualNewFileName).isNotNull().isNotBlank();
    }
}
