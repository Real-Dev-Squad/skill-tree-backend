package com.RDS.skilltree.Endorsement;

import com.RDS.skilltree.EndorsementList.EndorsementListModel;
import com.RDS.skilltree.Skill.SkillModel;
import com.RDS.skilltree.User.UserModel;

import java.lang.reflect.Field;
import java.util.*;

public class JsonApiResponseConverter {

    public static Map<String, Object> convertToHashMap(EndorsementDTO endorsementDTO) {
        Map<String, Object> result = new HashMap<>();
        result.put("id", endorsementDTO.getId());
        result.put("type", "endorsement");
        result.put("links", createLinksMap(endorsementDTO.getId().toString()));
        result.put("attributes", createAttributesHashMap(endorsementDTO));
        result.put("relationships",createRelationshipsHashMap(endorsementDTO));
        result.put("included",createIncludedList(endorsementDTO));

        return result;
    }

    private static Map<String, Object> createLinksMap(String id) {
        Map<String, Object> links = new HashMap<>();
        links.put("self", "/endorsements/" + id);
        return links;
    }

    private static Map<String, Object> createAttributesHashMap(EndorsementDTO endorsementDTO) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("status", endorsementDTO.getStatus());
        attributes.put("createdAt", endorsementDTO.getCreatedAt());
        attributes.put("updatedAt", endorsementDTO.getUpdatedAt());
        return attributes;
    }

    private static Map<String, Object> createRelationshipsHashMap(EndorsementDTO endorsementDTO) {
        Map<String, Object> relationships = new HashMap<>();

        Map<String, Object> userRelationship = getIdHashmap(endorsementDTO.getUser(),"user");
        relationships.put("user", userRelationship);


        Map<String, Object> skillRelationship = getIdHashmap(endorsementDTO.getSkill(),"skill");
        relationships.put("skill", skillRelationship);

        Map<String, Object> createdBy = getIdHashmap(endorsementDTO.getCreatedBy(),"user");
        relationships.put("createdBy", createdBy);

        Map<String, Object> updatedBy = getIdHashmap(endorsementDTO.getUpdatedBy(),"user");
        relationships.put("updatedBy", updatedBy);

        List<Map<String, Object>> endorsersList = new ArrayList<>();
        for (EndorsementListModel endorserListModel : endorsementDTO.getEndorsersList()) {
            Map<String, Object> data = new HashMap<>();
            data.put("id",endorserListModel.getId());
            data.put("type","endorser");
            endorsersList.add(data);
        }

        Map<String, Object> endorseRelationships = new HashMap<>();
        endorseRelationships.put("data",endorsersList);
        relationships.put("endorsersList",endorseRelationships);

        return relationships;
    }

    private static List<Map<String, Object>> createIncludedList(EndorsementDTO endorsementDTO) {
        List<Map<String, Object>> includedList = new ArrayList<>();
        includedList.add(extractSkillsAttributes(endorsementDTO.getSkill()));
        includedList.add(extractUsersAttributes(endorsementDTO.getUser()));
        for (EndorsementListModel endorserListModel : endorsementDTO.getEndorsersList()) {
            includedList.add(extractEndorsementListModelAttributes(endorserListModel));
        }
        return includedList;
    }
    private static Map<String,Object> extractEndorsementListModelAttributes(EndorsementListModel endorsementListModel) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("type",endorsementListModel.getType());
        attributes.put("deleted",endorsementListModel.isDeleted());
        attributes.put("description",endorsementListModel.getDescription());
        attributes.put("createdAt",endorsementListModel.getCreatedAt());
        attributes.put("updatedAt",endorsementListModel.getUpdatedAt());

        Map<String, Object> relationships = new HashMap<>();
        relationships.put("endorsersList",getIdHashmap(endorsementListModel.getEndorser(),"user"));
        relationships.put("createdBy",getIdHashmap(endorsementListModel.getCreatedBy(),"user"));
        relationships.put("updatedBy",getIdHashmap(endorsementListModel.getUpdatedBy(),"user"));

        result.put("id",endorsementListModel.getId());
        result.put("type","EndorsementList");
        result.put("attributes",attributes);
        result.put("relationships",relationships);
        return result;
    }
    private static Map<String,Object> extractSkillsAttributes(SkillModel skillModel) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name",skillModel.getName());
        attributes.put("type",skillModel.getType());
        attributes.put("createdAt",skillModel.getCreatedAt());
        attributes.put("updatedAt",skillModel.getUpdatedAt());
        attributes.put("isDeleted",skillModel.isDeleted());

        Map<String, Object> relationships = new HashMap<>();
        relationships.put("createdBy",getIdHashmap(skillModel.getCreatedBy(),"user"));
        relationships.put("updatedBy",getIdHashmap(skillModel.getUpdatedBy(),"user"));

        result.put("id",skillModel.getId());
        result.put("type","skill");
        result.put("attributes",attributes);
        result.put("relationships",relationships);

        return result;
    }
    private static Map<String,Object> extractUsersAttributes(UserModel userModel) {
        Map<String, Object> result = new HashMap<>();

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName",userModel.getFirstName());
        attributes.put("lastName",userModel.getLastName());
        attributes.put("rdsUserId",userModel.getRdsUserId());
        attributes.put("role",userModel.getRole());
        attributes.put("imageUrl",userModel.getImageUrl());
        attributes.put("createdAt",userModel.getCreatedAt());
        attributes.put("updatedAt",userModel.getUpdatedAt());

        Map<String, Object> relationships = new HashMap<>();
        relationships.put("createdBy",getIdHashmap(userModel.getCreatedBy(),"user"));
        relationships.put("updatedBy",getIdHashmap(userModel.getUpdatedBy(),"user"));
        result.put("id",userModel.getId());
        result.put("type","user");
        result.put("attributes",attributes);
        result.put("relationships",relationships);
        return result;
    }
    private static Map<String, Object> getIdHashmap(Object dto, String type) {
        Map<String, Object> relationship = new HashMap<>();
        Map<String, Object> data = new HashMap<>();
        try {
            if (dto == null) {
                data.put("type", type);
                data.put("id", null);
            } else {
                Field idField = dto.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                Object id = idField.get(dto);

                data.put("type", type);
                data.put("id", id);

            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        relationship.put("data", data);
        return relationship;
    }

}
