package com.assignment.drones.handler;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.assignment.drones.util.Constants.CORRELATION_ID_LOG_ATTRIBUTE;
import static com.assignment.drones.util.Constants.CORRELATION_ID_REQUEST_HEADER;

@Component
public class CorrelationIdInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        if (handler instanceof HandlerMethod) {
            String correlationId = request.getHeader(CORRELATION_ID_REQUEST_HEADER);
            MDC.put(CORRELATION_ID_LOG_ATTRIBUTE, correlationId);

            if (!StringUtils.hasLength(correlationId)) {
                /*
                if generateCorrelationId is not null, and the X-Syy-Correlation-Id has not been passed in the request header,
                generate a unique id manually
                 */
                correlationId = generateUniqueCorrelationId();
                MDC.put(CORRELATION_ID_LOG_ATTRIBUTE, correlationId);
            }
        }

        response.addHeader(CORRELATION_ID_REQUEST_HEADER, MDC.get(CORRELATION_ID_LOG_ATTRIBUTE));
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex) {
        response.addHeader(CORRELATION_ID_REQUEST_HEADER, MDC.get(CORRELATION_ID_LOG_ATTRIBUTE));
        MDC.remove(CORRELATION_ID_LOG_ATTRIBUTE);
    }

    private String generateUniqueCorrelationId() {
        return UUID.randomUUID().toString();
    }
}
