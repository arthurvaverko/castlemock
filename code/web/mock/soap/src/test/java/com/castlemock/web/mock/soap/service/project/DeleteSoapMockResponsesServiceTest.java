/*
 * Copyright 2018 Karl Dahlgren
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

package com.castlemock.web.mock.soap.service.project;

import com.castlemock.core.basis.model.ServiceResult;
import com.castlemock.core.basis.model.ServiceTask;
import com.castlemock.core.mock.soap.model.project.domain.SoapMockResponse;
import com.castlemock.core.mock.soap.service.project.input.DeleteSoapMockResponsesInput;
import com.castlemock.core.mock.soap.service.project.output.DeleteSoapMockResponsesOutput;
import com.castlemock.web.mock.soap.repository.project.SoapMockResponseRepository;
import org.dozer.DozerBeanMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

public class DeleteSoapMockResponsesServiceTest {

    @Spy
    private DozerBeanMapper mapper;

    @Mock
    private SoapMockResponseRepository mockResponseRepository;

    @InjectMocks
    private DeleteSoapMockResponsesService service;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testProcess(){
        final String projectId = "projectId";
        final String portId = "portId";
        final String operationId = "operationId";

        final SoapMockResponse response1 = new SoapMockResponse();
        response1.setId("Response1");
        final SoapMockResponse response2 = new SoapMockResponse();
        response2.setId("Response2");

        final List<SoapMockResponse> responses = Arrays.asList(response1, response2);


        final DeleteSoapMockResponsesInput input =
                new DeleteSoapMockResponsesInput(projectId, portId, operationId, responses);
        final ServiceTask<DeleteSoapMockResponsesInput> serviceTask = new ServiceTask<DeleteSoapMockResponsesInput>(input);
        final ServiceResult<DeleteSoapMockResponsesOutput> serviceResult = service.process(serviceTask);


        Mockito.verify(mockResponseRepository, Mockito.times(1)).delete("Response1");
        Mockito.verify(mockResponseRepository, Mockito.times(1)).delete("Response2");
    }

}
