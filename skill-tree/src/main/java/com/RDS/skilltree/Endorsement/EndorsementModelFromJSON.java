package com.RDS.skilltree.Endorsement;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

/* TODO:Dummy JSON code, needs to be changed as part of #103 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EndorsementModelFromJSON {
    private UUID id;

    @JsonProperty("user_id")
    private UUID userID;

    @JsonProperty("skill_id")
    private UUID skillId;

    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("created_by")
    private UUID createdBy;

    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;

    @JsonProperty("updated_by")
    private UUID updatedBy;
}
