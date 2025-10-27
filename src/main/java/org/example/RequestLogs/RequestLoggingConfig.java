package org.example.RequestLogs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
public class RequestLoggingConfig {

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeClientInfo(true);  // logs IP and session
        filter.setIncludeQueryString(true); // logs ?params
        filter.setIncludePayload(true);     // logs request body (optional)
        filter.setMaxPayloadLength(1000);   // truncate large payloads
        filter.setIncludeHeaders(true);    // optional: set true to log headers
        return filter;
    }
}
