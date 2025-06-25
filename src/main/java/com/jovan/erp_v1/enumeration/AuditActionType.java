package com.jovan.erp_v1.enumeration;

public enum AuditActionType {

    LOGIN_EMAIL,
    SEND_EMAIL,
    READ_EMAIL,
    DELETE_EMAIL,
    CREATE_USER,
    UPDATE_USER,
    DELETE_USER,
    VIEW_PASSWORD_COLLECTOR, // samo ako se koristi
    EXPORT_EMAILS,
    FAILED_LOGIN
}
