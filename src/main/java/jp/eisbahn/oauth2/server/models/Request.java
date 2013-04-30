/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package jp.eisbahn.oauth2.server.models;

import java.util.Map;

/**
 * This interface defines some method to retrieve the information from the request
 * for processing each OAuth2.0 authorization.
 * 
 * @author Yoichiro Tanaka
 *
 */
public interface Request {

	/**
	 * Retrieve the parameter value specified by the parameter name
	 * from the request.
	 * @param name The parameter name.
	 * @return The value against the name.
	 */
	public String getParameter(String name);

	/**
	 * Retrieve all parameter names and values from the request as a Map instance.
	 * @return  The map instance which has all parameter names and values.
	 */
	public Map<String, String> getParameterMap();
	
	/**
	 * Retrieve the request header value from the request.
	 * @param name The header's name.
	 * @return The value against the name.
	 */
	public String getHeader(String name);

}
