package net.vanlew.mediamanager.api.domain.models.operations.responses;

import lombok.Getter;
import lombok.Setter;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationActionTypes;
import net.vanlew.mediamanager.api.domain.models.enumerations.OperationStatusTypes;

@Getter
@Setter
public abstract class OperationResultBase<T> {

    private OperationStatusTypes status;

    private OperationActionTypes operation;

    private String message;

    private T entity;

    public OperationResultBase(OperationActionTypes operation) {
        this(operation, OperationStatusTypes.UNKNOWN);
    }

    public OperationResultBase(OperationActionTypes operation, OperationStatusTypes status) {
        this.setStatus(status);
        this.setOperation(operation);
    }
}