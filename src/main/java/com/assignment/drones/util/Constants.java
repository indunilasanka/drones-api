package com.assignment.drones.util;

/**
 * Define application constants
 */
public class Constants {
    public static final int LOW_BATTERY_CAPACITY_THRESHOLD = 25;
    public static final int SCHEDULER_DEFAULT_DELAY = 600000; //10mins
    public static final String CORRELATION_ID_REQUEST_HEADER = "X-Syy-Correlation-Id";
    public static final String CORRELATION_ID_LOG_ATTRIBUTE = "CORRELATION_ID";

    private Constants() {
    }

    public static class RequestStatus {
        public static final String SUCCESS = "success";
        public static final String ERROR = "error";
        public static final String FAILURE = "failure";
        public static final String WARNING = "warning";

        private RequestStatus() {
        }
    }
}
