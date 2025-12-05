package net.vanlew.mediamanager.library.base;

import lombok.Setter;

/**
 * Interface defining the contract for media renaming operations.
 */
public interface MediaRenamer {
    void execute();

    String getSourceDirectory();
    void setSourceDirectory(String sourceDirectory);

    boolean isUseSourceForDestination();
    void setUseSourceForDestination(boolean useSourceForDestination);

    boolean isRecurseSourceDirectory();
    void setRecurseSourceDirectory(boolean recurseSourceDirectory);

    boolean isForceRename();
    void setForceRename(boolean forceRename);

    boolean isWhatIfScenario();
    void setWhatIfScenario(boolean whatIfScenario);
}
