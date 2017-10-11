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

package org.openkilda.northbound.controller;

import static org.openkilda.messaging.Utils.CORRELATION_ID;
import static org.openkilda.messaging.Utils.DEFAULT_CORRELATION_ID;
import static org.openkilda.messaging.Utils.MAPPER;
import static org.openkilda.northbound.controller.TestMessageMock.ERROR_FLOW_ID;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.openkilda.messaging.error.ErrorType;
import org.openkilda.messaging.error.MessageError;
import org.openkilda.messaging.payload.flow.FlowIdStatusPayload;
import org.openkilda.messaging.payload.flow.FlowPathPayload;
import org.openkilda.messaging.payload.flow.FlowPayload;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = TestConfig.class)
@TestPropertySource("classpath:northbound.properties")
public class FlowControllerTest {
    private static final String USERNAME = "kilda";
    private static final String PASSWORD = "kilda";
    private static final String ROLE = "ADMIN";

    private static final MessageError AUTH_ERROR = new MessageError(DEFAULT_CORRELATION_ID, 0,
            ErrorType.AUTH_FAILED.toString(), "Kilda", "InsufficientAuthenticationException");
    private static final MessageError NOT_FOUND_ERROR = new MessageError(DEFAULT_CORRELATION_ID, 0,
            ErrorType.NOT_FOUND.toString(), "Flow was not found", TestMessageMock.ERROR_FLOW_ID);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void createFlow() throws Exception {
        MvcResult result = mockMvc.perform(put("/flows")
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(TestMessageMock.flow)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        FlowPayload response = MAPPER.readValue(result.getResponse().getContentAsString(), FlowPayload.class);
        assertEquals(TestMessageMock.flow, response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void getFlow() throws Exception {
        MvcResult result = mockMvc.perform(get("/flows/{flow-id}", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        FlowPayload response = MAPPER.readValue(result.getResponse().getContentAsString(), FlowPayload.class);
        assertEquals(TestMessageMock.flow, response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void deleteFlow() throws Exception {
        MvcResult result = mockMvc.perform(delete("/flows/{flow-id}", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        FlowPayload response = MAPPER.readValue(result.getResponse().getContentAsString(), FlowPayload.class);
        assertEquals(TestMessageMock.flow, response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void updateFlow() throws Exception {
        MvcResult result = mockMvc.perform(put("/flows/{flow-id}", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE)
                .content(MAPPER.writeValueAsString(TestMessageMock.flow)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        FlowPayload response = MAPPER.readValue(result.getResponse().getContentAsString(), FlowPayload.class);
        assertEquals(TestMessageMock.flow, response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void getFlows() throws Exception {
        MvcResult result = mockMvc.perform(get("/flows", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        List<FlowPayload> response = MAPPER.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<FlowPayload>>() {});
        assertEquals(Collections.singletonList(TestMessageMock.flow), response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void statusFlow() throws Exception {
        MvcResult result = mockMvc.perform(get("/flows/status/{flow-id}", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        FlowIdStatusPayload response =
                MAPPER.readValue(result.getResponse().getContentAsString(), FlowIdStatusPayload.class);
        assertEquals(TestMessageMock.flowStatus, response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void pathFlow() throws Exception {
        MvcResult result = mockMvc.perform(get("/flows/path/{flow-id}", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, testCorrelationId())
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();
        FlowPathPayload response = MAPPER.readValue(result.getResponse().getContentAsString(), FlowPathPayload.class);
        assertEquals(TestMessageMock.flowPath, response);
    }

    @Test
    @WithMockUser(username = USERNAME, password = PASSWORD, roles = ROLE)
    public void getNonExistingFlow() throws Exception {
        MvcResult result = mockMvc.perform(get("/flows/{flow-id}", ERROR_FLOW_ID)
                .header(CORRELATION_ID, DEFAULT_CORRELATION_ID)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        MessageError response = MAPPER.readValue(result.getResponse().getContentAsString(), MessageError.class);
        assertEquals(NOT_FOUND_ERROR, response);
    }

    @Test
    public void emptyCredentials() throws Exception {
        MvcResult result = mockMvc.perform(get("/flows/path/{flow-id}", TestMessageMock.FLOW_ID)
                .header(CORRELATION_ID, DEFAULT_CORRELATION_ID)
                .contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8_VALUE))
                .andReturn();

        MessageError response = MAPPER.readValue(result.getResponse().getContentAsString(), MessageError.class);
        assertEquals(AUTH_ERROR, response);
    }

    private static String testCorrelationId() {
        return UUID.randomUUID().toString();
    }
}