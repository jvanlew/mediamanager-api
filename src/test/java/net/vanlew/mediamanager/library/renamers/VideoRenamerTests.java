package net.vanlew.mediamanager.library.renamers;

import org.junit.jupiter.api.Test;

public class VideoRenamerTests {

    @Test
    void videoRenamer_Execute_ShouldCompleteSuccessfully() {
        // Arrange
        VideoRenamer renamer = new VideoRenamer();
        renamer.setSourceDirectory("path/to/test/videos");
        renamer.setRecurseSourceDirectory(true);
        renamer.setForceRename(false);

        // Act
        renamer.execute();

        // Assert
        // (Assertions would go here based on expected outcomes)
    }
}