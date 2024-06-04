package com.RDS.skilltree.Endorsement;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EndorsementDRO {
    @NotNull(message = "user id cannot be null")
    private UUID endorseeId;

    @NotNull(message = "skill id cannot be null")
    private UUID skillId;
}
