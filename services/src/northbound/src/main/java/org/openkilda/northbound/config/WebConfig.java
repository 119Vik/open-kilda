/* Copyright 2017 Telstra Open Source
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.openkilda.northbound.config;

import static org.openkilda.messaging.Utils.CORRELATION_ID;
import static org.openkilda.messaging.Utils.DEFAULT_CORRELATION_ID;

import org.openkilda.northbound.utils.ExecutionTimeInterceptor;
import org.openkilda.northbound.utils.ExtraAuthInterceptor;
import org.slf4j.MDC;
import org.slf4j.MDC.MDCCloseable;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The Web Application configuration.
 */
@Configuration
@EnableWebMvc
@PropertySource({"classpath:northbound.properties"})
public class WebConfig extends WebMvcConfigurerAdapter {
    /**
     * {@inheritDoc}
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(timeExecutionInterceptor());
        registry.addInterceptor(extraAuthInterceptor());
    }

    /**
     * Request processing time counting interceptor.
     *
     * @return interceptor instance
     */
    @Bean
    public ExecutionTimeInterceptor timeExecutionInterceptor() {
        return new ExecutionTimeInterceptor();
    }

    @Bean
    public ExtraAuthInterceptor extraAuthInterceptor() {
        return new ExtraAuthInterceptor();
    }

    /**
     * Rest template for performing REST calls.
     */
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate restTemplate = builder.build();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_JSON));
        restTemplate.getMessageConverters().add(converter);

        return restTemplate;
    }

    /**
     * Swagger UI resources.
     *
     * @param registry resource registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Bean
    public OncePerRequestFilter requestCorrelationIdFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                    FilterChain filterChain)
                    throws ServletException, IOException {

                // Put the request's correlationId into the logger context.
                // MDC is picked up by the %X in log4j2 formatter .. resources/log4j2.xml
                String correlationId = Optional.ofNullable(request.getHeader(CORRELATION_ID)).orElse(DEFAULT_CORRELATION_ID);
                try(MDCCloseable closable = MDC.putCloseable(CORRELATION_ID, correlationId)) {
                    filterChain.doFilter(request, response);
                }
            }
        };
    }
}
