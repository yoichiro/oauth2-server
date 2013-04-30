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

package jp.eisbahn.oauth2.server.granttype;

import java.util.Map;

/**
 * This class provides a grant handler instance by specified grant type name.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class GrantHandlerProvider {

	private Map<String, GrantHandler> handlers;

	/**
	 * Retrieve the grant handler instance for the specified grant type.
	 * 
	 * @param type The grant type string.
	 * @return The grant handler instance. The null is returned when the target
	 * grant handler is not found for the specified grant type.
	 */
	public GrantHandler getHandler(String type) {
		return handlers.get(type);
	}
	
	/**
	 * Retrieve the map instance which has grant handlers
	 * This method is provided for an unit test.
	 * @return The map object which has grant handlers.
	 */
	public Map<String, GrantHandler> getHandlers() {
		return handlers;
	}

	/**
	 * Set the map instance which has grant handlers.
	 * The key means a grant type. The value is each grant handler instance.
	 * @param handlers The map object which has grant handlers.
	 */
	public void setGrantHandlers(Map<String, GrantHandler> handlers) {
		this.handlers = handlers;
	}

}
