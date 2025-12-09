package net.vanlew.mediamanager.api.domain.models.operations.responses;

import net.vanlew.mediamanager.api.domain.models.enumerations.OperationActionTypes;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;

public class FindResult<T> extends OperationResultBase<T> {

    private static final OperationActionTypes operation = OperationActionTypes.GET;

    public FindResult() {
        super(operation);
    }

    public FindResult(OperationStatusTypes status) {
        super(operation, status);
    }
}