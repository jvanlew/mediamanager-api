package net.vanlew.mediamanager.api.domain.models.operations.responses;

import net.vanlew.mediamanager.api.domain.models.enumerations.OperationActionTypes;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;

public class DeleteResult<T> extends OperationResultBase<T> {

    private static final OperationActionTypes operation = OperationActionTypes.DELETE;

    public DeleteResult() {
        super(operation);
    }

    public DeleteResult(OperationStatusTypes status) {
        super(operation, status);
    }
}