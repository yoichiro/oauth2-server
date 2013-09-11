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

package jp.eisbahn.oauth2.server.responsetype;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This provider class provides supported handlers for Authorization Endpoint.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class AuthorizationHandlerProvider {

	private Map<String, AuthorizationHandler> handlerMap;
	
	public AuthorizationHandlerProvider() {
		super();
		handlerMap = new HashMap<String, AuthorizationHandler>();
	}
	
	public void setAuthorizationHandlers(List<AuthorizationHandler> handlers) {
		for (AuthorizationHandler handler : handlers) {
			handlerMap.put(handler.getResponseType(), handler);
		}
	}
	
	public AuthorizationHandler getAuthorizationHandler(String responseType) {
		return handlerMap.get(responseType);
	}
	
}
