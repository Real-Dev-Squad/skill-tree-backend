package utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.ResultMatcher;

public class CustomResultMatchers {

    public static ResultMatcher hasSkillRequest(String skillName, String endorseId, String status) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            // Find the request for the given skillName and endorseId
            JsonNode matchingSkillRequest = findMatchingSkillRequest(root, skillName, endorseId);
            assertThat(matchingSkillRequest).isNotNull().isNotEmpty();

            assertThat(matchingSkillRequest.get("endorseId").asText()).isEqualTo(endorseId);
            assertThat(matchingSkillRequest.get("status").asText()).isEqualTo(status);
        };
    }

    public static ResultMatcher hasEndorsement(
            String skillName, String endorseId, String endorserId, String message) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            // Find the request for the given skillName and endorseId
            JsonNode matchingSkillRequest = findMatchingSkillRequest(root, skillName, endorseId);
            assertThat(matchingSkillRequest).isNotNull().isNotEmpty();

            // Check endorsements
            JsonNode endorsements = matchingSkillRequest.get("endorsements");
            assertThat(endorsements).isNotNull().isNotEmpty();

            // Find matching endorsement by endorserId
            JsonNode matchingEndorsement = findByField(endorsements, "endorserId", endorserId);
            assertThat(matchingEndorsement).isNotNull();

            // Assert endorsement details
            assertThat(matchingEndorsement.get("endorserId").asText()).isEqualTo(endorserId);
            assertThat(matchingEndorsement.get("message").asText()).isEqualTo(message);
        };
    }

    public static ResultMatcher hasUser(String userId, String name) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            JsonNode users = root.get("users");
            assertThat(users).isNotNull().isNotEmpty();

            // Find user by userId
            JsonNode matchingUser = findByField(users, "id", userId);
            assertThat(matchingUser).isNotNull();

            // Assert user details
            assertThat(matchingUser.get("id").asText()).isEqualTo(userId);
            assertThat(matchingUser.get("name").asText()).isEqualTo(name);
        };
    }

    public static ResultMatcher doesNotHaveSkillRequest(String skillName, String endorseId) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            JsonNode requests = root.get("requests");
            if (requests == null || requests.isEmpty()) {
                // If requests is null or empty, the test passes as the request definitely doesn't exist
                return;
            }

            // Find the request for the given skillName and endorseId
            JsonNode matchingRequest = findBySkillAndEndorseId(requests, skillName, endorseId);

            // Assert that the request is null
            assertThat(matchingRequest).isNull();
        };
    }

    public static ResultMatcher doesNotHaveEndorsement(
            String skillName, String endorseId, String endorserId) {
        return result -> {
            String json = result.getResponse().getContentAsString();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(json);

            JsonNode requests = root.get("requests");
            if (requests == null || requests.isEmpty()) {
                // If requests is null or empty, the test passes
                return;
            }

            // Find the request for the given skillName and endorseId
            JsonNode matchingSkillRequest = findBySkillAndEndorseId(requests, skillName, endorseId);
            if (matchingSkillRequest == null) {
                // If the request doesn't exist, the endorsement definitely doesn't exist
                return;
            }

            // Check endorsements
            JsonNode endorsements = matchingSkillRequest.get("endorsements");
            if (endorsements == null || endorsements.isEmpty()) {
                // If no endorsements, the test passes
                return;
            }

            // Find matching endorsement by endorserId
            JsonNode matchingEndorsement = findByField(endorsements, "endorserId", endorserId);

            // Assert that the endorsement is null
            assertThat(matchingEndorsement).isNull();
        };
    }

    private static JsonNode findByField(JsonNode array, String fieldName, String value) {
        for (JsonNode node : array) {
            if (node.has(fieldName) && node.get(fieldName).asText().equals(value)) {
                return node;
            }
        }
        return null;
    }

    private static JsonNode findMatchingSkillRequest(
            JsonNode root, String skillName, String endorseId) {
        JsonNode requests = root.get("requests");
        assertThat(requests).isNotNull().isNotEmpty();

        return findBySkillAndEndorseId(requests, skillName, endorseId);
    }

    private static JsonNode findBySkillAndEndorseId(
            JsonNode array, String skillName, String endorseId) {
        for (JsonNode node : array) {
            if (node.has("skillName")
                    && node.get("skillName").asText().equals(skillName)
                    && node.has("endorseId")
                    && node.get("endorseId").asText().equals(endorseId)) {
                return node;
            }
        }
        return null;
    }
}
