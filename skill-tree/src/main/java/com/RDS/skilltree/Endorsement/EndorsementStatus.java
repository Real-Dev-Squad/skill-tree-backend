package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.Exceptions.InvalidParameterException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum EndorsementStatus {
    APPROVED,
    PENDING,
    REJECTED;

    private static final Map<String, EndorsementStatus> endorsementStatusMap =
            Stream.of(values())
                    .collect(Collectors.toMap(EndorsementStatus::toString, Function.identity()));

    public static EndorsementStatus fromString(final String name) {
        EndorsementStatus endorsementStatus = endorsementStatusMap.get(name);
        if (endorsementStatus == null) {
            throw new InvalidParameterException("endorsement status", name);
        }
        return endorsementStatus;
    }
}
