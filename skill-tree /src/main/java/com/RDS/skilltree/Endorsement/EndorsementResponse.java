package com.RDS.skilltree.Endorsement;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EndorsementResponse<T> {
    private T data;
    private String message;
}
