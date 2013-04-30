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

/**
 * This model class has an information of a client credential.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class ClientCredential {

	private String clientId;
	private String clientSecret;

	/**
	 * Initialize this instance with arguments.
	 * @param clientId The client ID.
	 * @param clientSecret The client secret string.
	 */
	public ClientCredential(String clientId, String clientSecret) {
		super();
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	/**
	 * Retrieve the client ID.
	 * @return The client ID.
	 */
	public String getClientId() {
		return clientId;
	}

	/**
	 * Retrieve the client secret.
	 * @return The client secret.
	 */
	public String getClientSecret() {
		return clientSecret;
	}

}
