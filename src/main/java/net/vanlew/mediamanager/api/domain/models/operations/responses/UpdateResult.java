package net.vanlew.mediamanager.api.domain.models.operations.responses;

import net.vanlew.mediamanager.api.domain.models.enumerations.OperationActionTypes;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;

public class UpdateResult<T> extends OperationResultBase<T> {

    private static final OperationActionTypes operation = OperationActionTypes.UPDATE;

    public UpdateResult() {
        super(operation);
    }

    public UpdateResult(OperationStatusTypes status) {
        super(operation, status);
    }
}