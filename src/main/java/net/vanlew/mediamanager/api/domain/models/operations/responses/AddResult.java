package net.vanlew.mediamanager.api.domain.models.operations.responses;

import net.vanlew.mediamanager.api.domain.models.enumerations.OperationActionTypes;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;

public class AddResult<T> extends OperationResultBase<T> {

    private static final OperationActionTypes operation = OperationActionTypes.ADD;

    public AddResult() {
        super(operation);
    }

    public AddResult(OperationStatusTypes status) {
        super(operation, status);
    }
}
