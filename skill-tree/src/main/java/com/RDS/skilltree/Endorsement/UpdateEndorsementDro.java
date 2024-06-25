package com.RDS.skilltree.Endorsement;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UpdateEndorsementDro {
    @NotNull(message = "Message cannot be empty")
    private String message;
}
