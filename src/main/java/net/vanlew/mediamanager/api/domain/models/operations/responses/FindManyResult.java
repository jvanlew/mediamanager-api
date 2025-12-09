package net.vanlew.mediamanager.api.domain.models.operations.responses;

import net.vanlew.mediamanager.api.domain.models.enumerations.OperationActionTypes;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;

public class FindManyResult<T> extends OperationResultBase<T> {

    private static final OperationActionTypes operation = OperationActionTypes.GET_MANY;

    public FindManyResult() {
        super(operation);
    }

    public FindManyResult(OperationStatusTypes status) {
        super(operation, status);
    }
}