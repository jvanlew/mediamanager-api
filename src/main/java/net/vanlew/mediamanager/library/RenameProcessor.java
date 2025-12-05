package net.vanlew.mediamanager.library;

import lombok.Getter;
import lombok.Setter;
import net.vanlew.mediamanager.library.renamers.PhotoRenamer;
import net.vanlew.mediamanager.library.renamers.VideoRenamer;
import net.vanlew.mediamanager.library.enumerations.ConvertOnlyTypes;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Getter
@Setter
public class RenameProcessor implements AutoCloseable {
    private final Logger log;


    private String sourceDirectory;
    private boolean useSourceForDestination;
    private boolean recurseSourceDirectory;
    private boolean forceRename;
    private boolean whatIfScenario;
    private ConvertOnlyTypes convertOnly;

    public RenameProcessor(Logger logger) {
        this.log = (logger != null) ? logger : Logger.getLogger(RenameProcessor.class.getName());
    }

    public RenameProcessor() {
        this(null);
    }

    @Override
    public void close() {
        // No resources to clean up
    }

    public CompletableFuture<Void> execute(Consumer<String> startCallback, Consumer<Integer> finishCallback) {
        return CompletableFuture.runAsync(() -> {
            long allStart = System.currentTimeMillis();

            log.info("Starting Rename Processor with options:");
            log.info("   Convert option: " + this.convertOnly);
            log.info("   Recurse source directory: " + this.recurseSourceDirectory);
            log.info("   Force rename: " + this.forceRename);
            log.info("   What-If Scenario: " + this.whatIfScenario);
            log.info("Starting media renamer on directory '" + this.sourceDirectory + "'");

            if (this.convertOnly == ConvertOnlyTypes.ALL || this.convertOnly == ConvertOnlyTypes.PHOTOS) {
                PhotoRenamer pRenamer = new PhotoRenamer(log);
                pRenamer.setSourceDirectory(this.sourceDirectory);
                pRenamer.setForceRename(this.forceRename);
                pRenamer.setRecurseSourceDirectory(this.recurseSourceDirectory);
                pRenamer.setWhatIfScenario(this.whatIfScenario);

                long pStart = System.currentTimeMillis();
                pRenamer.execute();
                long pEnd = System.currentTimeMillis();
                log.fine("   Finished renaming photos in " + humanizeDuration(pEnd - pStart));
            }

            if (this.convertOnly == ConvertOnlyTypes.ALL || this.convertOnly == ConvertOnlyTypes.VIDEOS) {
                long vStart = System.currentTimeMillis();
                VideoRenamer vRenamer = new VideoRenamer(log);
                vRenamer.setSourceDirectory(this.sourceDirectory);
                vRenamer.setForceRename(this.forceRename);
                vRenamer.setRecurseSourceDirectory(this.recurseSourceDirectory);
                vRenamer.setWhatIfScenario(this.whatIfScenario);

                vRenamer.execute();
                long vEnd = System.currentTimeMillis();
                log.fine("   Finished renaming videos in " + humanizeDuration(vEnd - vStart));
            }

            long allEnd = System.currentTimeMillis();
            log.info("All done. Completed in " + humanizeDuration(allEnd - allStart));
        });
    }

    private String humanizeDuration(long millis) {
        long seconds = millis / 1000;
        long min = seconds / 60;
        long sec = seconds % 60;
        return min + "m " + sec + "s";
    }
}
