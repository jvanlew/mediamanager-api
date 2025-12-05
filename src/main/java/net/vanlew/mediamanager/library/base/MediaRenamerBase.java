package net.vanlew.mediamanager.library.base;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class MediaRenamerBase implements MediaRenamer {
    private String sourceDirectory;
    private boolean useSourceForDestination = true;
    private boolean recurseSourceDirectory = false;
    private boolean forceRename = false;
    private boolean whatIfScenario = false;

    public abstract void execute();

}