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

import java.util.Date;

/**
 * This model class has an information about an access token.
 * The access token is issued related on one authorization information.
 * Actually, an instance of this model is created by an DataHandler.
 * The DataHandler implementation may store this to your database or
 * something to cache in memory or other lightweight storage.
 * 
 * @author Yoichiro Tanaka
 *
 */
public class AccessToken {

	private String authId;
	private String token;
	private long expiresIn;
	private Date createdOn;

	/**
	 * Set the ID of the authorization information to relate between the
	 * information and this access token.
	 * @param authId The ID of the authorization information.
	 */
	public void setAuthId(String authId) {
		this.authId = authId;
	}

	/**
	 * Retrieve the ID of the authorization information.
	 * @return The ID string.
	 */
	public String getAuthId() {
		return authId;
	}

	/**
	 * Set the issued access token.
	 * @param token The access token string.
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Retrieve the access token.
	 * @return The access token string.
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Set the expiration time of this access token.
	 * If this access token has a time limitation, this value must be positive.
	 * @param expiresIn The expiration time value. The unit is second.
	 */
	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	/**
	 * Retrieve the expiration time of this access token.
	 * @return The expiration time value. This unit is second.
	 */
	public long getExpiresIn() {
		return expiresIn;
	}

	/**
	 * Set the time when this access token is created.
	 * @param createdOn The date and time value.
	 */
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	/**
	 * Retrieve the time when this access token is created.
	 * @return The date and time value when this is created.
	 */
	public Date getCreatedOn() {
		return createdOn;
	}

}
