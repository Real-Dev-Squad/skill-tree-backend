package com.RDS.skilltree.viewmodels;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdsUserViewModel {
    private String id;
    private boolean incomplete_user_details;
    private String discord_joined_at;
    private String discord_id;
    private Roles roles;
    private String linkedin_id;
    private Picture picture;
    private float yoe;
    private long github_created_at;
    private String github_display_name;
    private String github_id;
    private String twitter_id;
    private String username;
    private String github_user_id;
    private String first_name;
    private String profile_url;
    private String website;
    private String last_name;
    private String company;
    private String designation;
    private String instagram_id;
    private String profile_status;
    private long updated_at;
    private long created_at;

    @Getter
    @Setter
    public static class Roles {
        private boolean archived;
        private boolean in_discord;
        private boolean member;
        private boolean super_user;
    }

    @Getter
    @Setter
    public static class Picture {
        private String url;
        private String public_id;
    }
}
