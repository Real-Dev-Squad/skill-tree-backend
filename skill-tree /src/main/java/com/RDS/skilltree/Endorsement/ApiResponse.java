package com.RDS.skilltree.Endorsement;

import java.util.Map;

public record ApiResponse<T>(T data, int statusCode, String status, String message) {

}
