package jp.eisbahn.oauth2.server.responsetype;

import static org.junit.Assert.*;
import jp.eisbahn.oauth2.server.responsetype.AuthorizationHandler.AuthorizationRequest;

import org.junit.Test;

public class AuthorizationRequestTest {

	@Test
	public void testProperties() throws Exception {
		AuthorizationRequest target = new AuthorizationRequest();
		target.setClientId("clientId");
		target.setScope("scope");
		target.setRedirectUri("redirectUri");
		target.setState("state");
		target.setNonce("nonce");
		target.setDisplay("display");
		target.setPrompt("prompt");
		target.setMaxAge("maxAge");
		target.setUiLocales("uiLocales");
		target.setClaimsLocales("claimsLocales");
		target.setIdTokenHint("idTokenHint");
		target.setLoginHint("loginHint");
		target.setAcrValues("acrValues");
		target.setClaims("claims");
		target.setRegistration("registration");
		target.setRequest("request");
		target.setRequestUri("requestUri");
		assertEquals("clientId", target.getClientId());
		assertEquals("scope", target.getScope());
		assertEquals("redirectUri", target.getRedirectUri());
		assertEquals("state", target.getState());
		assertEquals("nonce", target.getNonce());
		assertEquals("display", target.getDisplay());
		assertEquals("prompt", target.getPrompt());
		assertEquals("maxAge", target.getMaxAge());
		assertEquals("uiLocales", target.getUiLocales());
		assertEquals("claimsLocales", target.getClaimsLocales());
		assertEquals("idTokenHint", target.getIdTokenHint());
		assertEquals("loginHint", target.getLoginHint());
		assertEquals("acrValues", target.getAcrValues());
		assertEquals("claims", target.getClaims());
		assertEquals("registration", target.getRegistration());
		assertEquals("request", target.getRequest());
		assertEquals("requestUri", target.getRequestUri());
	}
	
}
