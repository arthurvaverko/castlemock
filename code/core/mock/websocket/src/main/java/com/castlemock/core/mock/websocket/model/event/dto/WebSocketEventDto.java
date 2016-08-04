/*
 * Copyright 2016 Karl Dahlgren
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.castlemock.core.mock.websocket.model.event.dto;

import com.castlemock.core.basis.model.event.dto.EventDto;
import org.dozer.Mapping;

/**
 * @author Karl Dahlgren
 * @since 1.5
 */
public class WebSocketEventDto extends EventDto {

    @Mapping("request")
    private WebSocketRequestDto request;

    @Mapping("response")
    private WebSocketResponseDto response;

    @Mapping("projectId")
    private String projectId;

    @Mapping("topicId")
    private String topicId;

    @Mapping("resourceId")
    private String resourceId;

    /**
     * Default constructor for the WebSocket event DTO
     */
    public WebSocketEventDto() {
    }

    /**
     * Default constructor for the WebSocket event DTO
     */
    public WebSocketEventDto(final EventDto eventDto) {
        super(eventDto);
    }

    public WebSocketRequestDto getRequest() {
        return request;
    }

    public void setRequest(WebSocketRequestDto request) {
        this.request = request;
    }

    public WebSocketResponseDto getResponse() {
        return response;
    }

    public void setResponse(WebSocketResponseDto response) {
        this.response = response;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }
}
