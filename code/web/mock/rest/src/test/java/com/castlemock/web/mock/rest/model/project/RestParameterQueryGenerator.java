/*
 * Copyright 2015 Karl Dahlgren
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

package com.castlemock.web.mock.rest.model.project;


import com.castlemock.core.mock.rest.model.project.domain.RestParameterQuery;

/**
 * @author Karl Dahlgren
 * @since 1.0
 */
public class RestParameterQueryGenerator {
    
    public static RestParameterQuery generateRestParameterQuery(){
        final RestParameterQuery parameterQuery = new RestParameterQuery();
        parameterQuery.setParameter("Parameter");
        parameterQuery.setQuery("Query");
        parameterQuery.setMatchAny(false);
        parameterQuery.setMatchCase(false);
        parameterQuery.setMatchRegex(false);

        return parameterQuery;
    }
}
