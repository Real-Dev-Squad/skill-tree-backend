package com.RDS.skilltree.response;

import com.RDS.skilltree.enums.EndorsementType;
import com.RDS.skilltree.enums.Status;

import java.util.List;

public class EndorsementResponse {

    private static class EndorserList{
        private String endorserId;
        private String description;
        private String userType; //TODO check if this has to be changed
    }
    private String endorseeId;
    private String skillName;
    private Status status;
    private EndorsementType endorsementType;
    private List<EndorserList> endorsersList;
}