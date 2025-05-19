package com.jovan.erp_v1.response;

import java.time.LocalDateTime;

public record ErrorResponse(String msg, LocalDateTime timestamp, int status) {

}
