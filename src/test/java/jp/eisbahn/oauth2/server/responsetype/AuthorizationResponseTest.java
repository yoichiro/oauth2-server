package jp.eisbahn.oauth2.server.responsetype;

import static org.junit.Assert.*;
import jp.eisbahn.oauth2.server.responsetype.AuthorizationHandler.AuthorizationResponse;

import org.junit.Test;

public class AuthorizationResponseTest {

	@Test
	public void testProperties() throws Exception {
		AuthorizationResponse target = new AuthorizationResponse();
		target.setAccessToken("accessToken");
		target.setCode("code");
		target.setExpiresIn("expiresIn");
		target.setIdToken("idToken");
		target.setState("state");
		target.setTokenType("tokenType");
		assertEquals("accessToken", target.getAccessToken());
		assertEquals("code", target.getCode());
		assertEquals("expiresIn", target.getExpiresIn());
		assertEquals("idToken", target.getIdToken());
		assertEquals("state", target.getState());
		assertEquals("tokenType", target.getTokenType());
	}

}
