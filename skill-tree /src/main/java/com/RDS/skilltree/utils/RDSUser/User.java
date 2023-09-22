package com.RDS.skilltree.utils.RDSUser;

import lombok.Data;

@Data
public class User{
	private boolean incompleteUserDetails;
	private String discordJoinedAt;
	private String discordId;
	private Roles roles;
	private String last_name;
	private long createdAt;
	private String linkedinId;
	private boolean nicknameSynced;
	private Picture picture;
	private long githubCreatedAt;
	private String githubDisplayName;
	private long updatedAt;
	private String githubId;
	private String company;
	private String id;
	private String designation;
	private String twitterId;
	private String first_name;
	private String username;
}