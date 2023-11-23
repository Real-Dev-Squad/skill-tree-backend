package com.RDS.skilltree.Endorsement;

import java.util.Map;

public record ApiResponse(Map<String, Object> data, int statusCode, String status, String message) {

}
