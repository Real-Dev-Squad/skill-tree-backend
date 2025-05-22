package com.RDS.skilltree.utils;

public class Constants {
    private Constants() {}

    public static final class ExceptionMessages {
        public static final String SELF_ENDORSEMENT_NOT_ALLOWED = "Self endorsement not allowed";
        public static final String SKILL_NOT_FOUND = "Skill does not exist";
        public static final String ENDORSEMENT_ALREADY_EXISTS = "Endorsement already exists";
        public static final String ENDORSEMENT_NOT_FOUND = "Endorsement not found";
        public static final String ENDORSEMENT_MESSAGE_EMPTY = "Endorsement message cannot be empty";
        public static final String USER_NOT_FOUND = "Error getting user details";
        public static final String UNAUTHORIZED_ENDORSEMENT_UPDATE =
                "Not authorized to update this endorsement";
    }
}
