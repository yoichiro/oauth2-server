package jp.eisbahn.oauth2.server.models;

public class IdToken {

	private String iss;
	private String sub;
	private String[] aud;
	private Long exp;
	private Long lat;
	private Long authTime;
	private String nonce;
	private String atHash;
	private String cHash;
	private String acr;
	private String amr;
	private String azp;
	private String subJwk;

	/**
	 * Issuer Identifier for the Issuer of the response. The iss value is a case
	 * sensitive URL using the https scheme that contains scheme, host, and
	 * OPTIONALLY, port number and path components and no query or fragment
	 * components.
	 * 
	 * @return the iss
	 */
	public String getIss() {
		return iss;
	}

	/**
	 * Issuer Identifier for the Issuer of the response. The iss value is a case
	 * sensitive URL using the https scheme that contains scheme, host, and
	 * OPTIONALLY, port number and path components and no query or fragment
	 * components.
	 * 
	 * @param iss
	 *            the iss to set
	 */
	public void setIss(String iss) {
		this.iss = iss;
	}

	/**
	 * Subject identifier. A locally unique and never reassigned identifier
	 * within the Issuer for the End-User, which is intended to be consumed by
	 * the Client, e.g., 24400320 or AItOawmwtWwcT0k51BayewNvutrJUqsvl6qs7A4. It
	 * MUST NOT exceed 255 ASCII characters in length. The sub value is a case
	 * sensitive string.
	 * 
	 * @return the sub
	 */
	public String getSub() {
		return sub;
	}

	/**
	 * Subject identifier. A locally unique and never reassigned identifier
	 * within the Issuer for the End-User, which is intended to be consumed by
	 * the Client, e.g., 24400320 or AItOawmwtWwcT0k51BayewNvutrJUqsvl6qs7A4. It
	 * MUST NOT exceed 255 ASCII characters in length. The sub value is a case
	 * sensitive string.
	 * 
	 * @param sub
	 *            the sub to set
	 */
	public void setSub(String sub) {
		this.sub = sub;
	}

	/**
	 * Audience(s) that this ID Token is intended for. It MUST contain the OAuth
	 * 2.0 client_id of the Relying Party as an audience value. It MAY also
	 * contain identifiers for other audiences. In the general case, the aud
	 * value is an array of case sensitive strings. In the special case when
	 * there is one audience, the aud value MAY be a single case sensitive
	 * string.
	 * 
	 * @return the aud
	 */
	public String[] getAud() {
		return aud;
	}

	/**
	 * Audience(s) that this ID Token is intended for. It MUST contain the OAuth
	 * 2.0 client_id of the Relying Party as an audience value. It MAY also
	 * contain identifiers for other audiences. In the general case, the aud
	 * value is an array of case sensitive strings. In the special case when
	 * there is one audience, the aud value MAY be a single case sensitive
	 * string.
	 * 
	 * @param aud
	 *            the aud to set
	 */
	public void setAud(String[] aud) {
		this.aud = aud;
	}

	/**
	 * Expiration time on or after which the ID Token MUST NOT be accepted for
	 * processing. The processing of this parameter requires that the current
	 * date/time MUST be before the expiration date/time listed in the value.
	 * Implementers MAY provide for some small leeway, usually no more than a
	 * few minutes, to account for clock skew. The time is represented as the
	 * number of seconds from 1970-01-01T0:0:0Z as measured in UTC until the
	 * date/time. See RFC 3339 [RFC3339] for details regarding date/times in
	 * general and UTC in particular. The exp value is a number.
	 * 
	 * @return the exp
	 */
	public Long getExp() {
		return exp;
	}

	/**
	 * Expiration time on or after which the ID Token MUST NOT be accepted for
	 * processing. The processing of this parameter requires that the current
	 * date/time MUST be before the expiration date/time listed in the value.
	 * Implementers MAY provide for some small leeway, usually no more than a
	 * few minutes, to account for clock skew. The time is represented as the
	 * number of seconds from 1970-01-01T0:0:0Z as measured in UTC until the
	 * date/time. See RFC 3339 [RFC3339] for details regarding date/times in
	 * general and UTC in particular. The exp value is a number.
	 * 
	 * @param exp
	 *            the exp to set
	 */
	public void setExp(Long exp) {
		this.exp = exp;
	}

	/**
	 * Time at which the JWT was issued. The time is represented as the number
	 * of seconds from 1970-01-01T0:0:0Z as measured in UTC until the date/time.
	 * The iat value is a number.
	 * 
	 * @return the lat
	 */
	public Long getLat() {
		return lat;
	}

	/**
	 * Time at which the JWT was issued. The time is represented as the number
	 * of seconds from 1970-01-01T0:0:0Z as measured in UTC until the date/time.
	 * The iat value is a number.
	 * 
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(Long lat) {
		this.lat = lat;
	}

	/**
	 * Time when the End-User authentication occurred. The time is represented
	 * as the number of seconds from 1970-01-01T0:0:0Z as measured in UTC until
	 * the date/time. When a max_age request is made then this Claim is
	 * REQUIRED. The auth_time value is a number.
	 * 
	 * @return the authTime
	 */
	public Long getAuthTime() {
		return authTime;
	}

	/**
	 * Time when the End-User authentication occurred. The time is represented
	 * as the number of seconds from 1970-01-01T0:0:0Z as measured in UTC until
	 * the date/time. When a max_age request is made then this Claim is
	 * REQUIRED. The auth_time value is a number.
	 * 
	 * @param authTime
	 *            the authTime to set
	 */
	public void setAuthTime(Long authTime) {
		this.authTime = authTime;
	}

	/**
	 * String value used to associate a Client session with an ID Token, and to
	 * mitigate replay attacks. The value is passed through unmodified from the
	 * Authorization Request to the ID Token. The Client MUST verify that the
	 * nonce Claim Value is equal to the value of the nonce parameter sent in
	 * the Authorization Request. If present in the Authorization Request,
	 * Authorization Servers MUST include a nonce Claim in the ID Token with the
	 * Claim Value being the nonce value sent in the Authorization Request. Use
	 * of the nonce is OPTIONAL when using the code flow. The nonce value is a
	 * case sensitive string.
	 * 
	 * @return the nonce
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * String value used to associate a Client session with an ID Token, and to
	 * mitigate replay attacks. The value is passed through unmodified from the
	 * Authorization Request to the ID Token. The Client MUST verify that the
	 * nonce Claim Value is equal to the value of the nonce parameter sent in
	 * the Authorization Request. If present in the Authorization Request,
	 * Authorization Servers MUST include a nonce Claim in the ID Token with the
	 * Claim Value being the nonce value sent in the Authorization Request. Use
	 * of the nonce is OPTIONAL when using the code flow. The nonce value is a
	 * case sensitive string.
	 * 
	 * @param nonce
	 *            the nonce to set
	 */
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	/**
	 * Access Token hash value. This is OPTIONAL when the ID Token is issued
	 * from the Token Endpoint, which is the case for this profile; nonetheless,
	 * an at_hash Claim MAY be present. Its value is the base64url encoding of
	 * the left-most half of the hash of the octets of the ASCII representation
	 * of the access_token value, where the hash algorithm used is the hash
	 * algorithm used in the alg parameter of the ID Token's JWS [JWS] header.
	 * For instance, if the alg is RS256, hash the access_token value with
	 * SHA-256, then take the left-most 128 bits and base64url encode them. The
	 * at_hash value is a case sensitive string.
	 * 
	 * @return the atHash
	 */
	public String getAtHash() {
		return atHash;
	}

	/**
	 * Access Token hash value. This is OPTIONAL when the ID Token is issued
	 * from the Token Endpoint, which is the case for this profile; nonetheless,
	 * an at_hash Claim MAY be present. Its value is the base64url encoding of
	 * the left-most half of the hash of the octets of the ASCII representation
	 * of the access_token value, where the hash algorithm used is the hash
	 * algorithm used in the alg parameter of the ID Token's JWS [JWS] header.
	 * For instance, if the alg is RS256, hash the access_token value with
	 * SHA-256, then take the left-most 128 bits and base64url encode them. The
	 * at_hash value is a case sensitive string.
	 * 
	 * @param atHash
	 *            the atHash to set
	 */
	public void setAtHash(String atHash) {
		this.atHash = atHash;
	}

	/**
	 * Authentication Context Class Reference. String specifying an
	 * Authentication Context Class Reference value that identifies the
	 * Authentication Context Class that the authentication performed satisfied.
	 * The value "0" indicates the End-User authentication did not meet the
	 * requirements of ISO/IEC 29115 [ISO29115] level 1. Authentication using a
	 * long-lived browser cookie, for instance, is one example where the use of
	 * "level 0" is appropriate. Authentications with level 0 SHOULD never be
	 * used to authorize access to any resource of any monetary value. An
	 * absolute URI or a registered name [RFC6711] SHOULD be used as the acr
	 * value; registered names MUST NOT be used with a different meaning than
	 * that which is registered. Parties using this claim will need to agree
	 * upon the meanings of the values used, which may be context-specific. The
	 * acr value is a case sensitive string.
	 * 
	 * @return the acr
	 */
	public String getAcr() {
		return acr;
	}

	/**
	 * Authentication Context Class Reference. String specifying an
	 * Authentication Context Class Reference value that identifies the
	 * Authentication Context Class that the authentication performed satisfied.
	 * The value "0" indicates the End-User authentication did not meet the
	 * requirements of ISO/IEC 29115 [ISO29115] level 1. Authentication using a
	 * long-lived browser cookie, for instance, is one example where the use of
	 * "level 0" is appropriate. Authentications with level 0 SHOULD never be
	 * used to authorize access to any resource of any monetary value. An
	 * absolute URI or a registered name [RFC6711] SHOULD be used as the acr
	 * value; registered names MUST NOT be used with a different meaning than
	 * that which is registered. Parties using this claim will need to agree
	 * upon the meanings of the values used, which may be context-specific. The
	 * acr value is a case sensitive string.
	 * 
	 * @param acr
	 *            the acr to set
	 */
	public void setAcr(String acr) {
		this.acr = acr;
	}

	/**
	 * Authentication Methods References. JSON array of strings that are
	 * identifiers for authentication methods used in the authentication. For
	 * instance, values might indicate that both password and OTP authentication
	 * methods were used. The definition of particular values to be used in the
	 * amr Claim is beyond the scope of this specification. Parties using this
	 * claim will need to agree upon the meanings of the values used, which may
	 * be context-specific. The amr value is an array of case sensitive strings.
	 * 
	 * @return the amr
	 */
	public String getAmr() {
		return amr;
	}

	/**
	 * Authentication Methods References. JSON array of strings that are
	 * identifiers for authentication methods used in the authentication. For
	 * instance, values might indicate that both password and OTP authentication
	 * methods were used. The definition of particular values to be used in the
	 * amr Claim is beyond the scope of this specification. Parties using this
	 * claim will need to agree upon the meanings of the values used, which may
	 * be context-specific. The amr value is an array of case sensitive strings.
	 * 
	 * @param amr
	 *            the amr to set
	 */
	public void setAmr(String amr) {
		this.amr = amr;
	}

	/**
	 * Authorized Party - the party to which the ID Token was issued. If
	 * present, it MUST contain the OAuth 2.0 client_id of the party that will
	 * be using it. This Claim is only REQUIRED when the party requesting the ID
	 * Token is not the same as the sole audience of the ID Token. It MAY be
	 * included even when the Authorized Party is the same as the sole audience.
	 * The azp value is a case sensitive string containing a StringOrURI value.
	 * 
	 * @return the azp
	 */
	public String getAzp() {
		return azp;
	}

	/**
	 * Authorized Party - the party to which the ID Token was issued. If
	 * present, it MUST contain the OAuth 2.0 client_id of the party that will
	 * be using it. This Claim is only REQUIRED when the party requesting the ID
	 * Token is not the same as the sole audience of the ID Token. It MAY be
	 * included even when the Authorized Party is the same as the sole audience.
	 * The azp value is a case sensitive string containing a StringOrURI value.
	 * 
	 * @param azp
	 *            the azp to set
	 */
	public void setAzp(String azp) {
		this.azp = azp;
	}

	/**
	 * Code hash value. If the ID Token is issued from the Authorization
	 * Endpoint with a code, this is REQUIRED. Its value is the base64url
	 * encoding of the left-most half of the hash of the octets of the ASCII
	 * representation of the code value, where the hash algorithm used is the
	 * hash algorithm used in the alg parameter of the ID Token's JWS [JWS]
	 * header. For instance, if the alg is HS512, hash the code value with
	 * SHA-512, then take the left-most 256 bits and base64url encode them. The
	 * c_hash value is a case sensitive string.
	 * 
	 * @return the cHash
	 */
	public String getCHash() {
		return cHash;
	}

	/**
	 * Code hash value. If the ID Token is issued from the Authorization
	 * Endpoint with a code, this is REQUIRED. Its value is the base64url
	 * encoding of the left-most half of the hash of the octets of the ASCII
	 * representation of the code value, where the hash algorithm used is the
	 * hash algorithm used in the alg parameter of the ID Token's JWS [JWS]
	 * header. For instance, if the alg is HS512, hash the code value with
	 * SHA-512, then take the left-most 256 bits and base64url encode them. The
	 * c_hash value is a case sensitive string.
	 * 
	 * @param cHash
	 *            the cHash to set
	 */
	public void setCHash(String cHash) {
		this.cHash = cHash;
	}

	/**
	 * Public key value used to check the signature of an ID Token issued by a
	 * Self-Issued OpenID Provider, as specified in Section 6. The key is a bare
	 * key in JWK [JWK] format (not an X.509 certificate value). Use of the
	 * sub_jwk Claim is REQUIRED when the OP is a Self-Issued OP and is NOT
	 * RECOMMENDED when the OP is not Self-Issued.
	 * 
	 * @return the subJwk
	 */
	public String getSubJwk() {
		return subJwk;
	}

	/**
	 * Public key value used to check the signature of an ID Token issued by a
	 * Self-Issued OpenID Provider, as specified in Section 6. The key is a bare
	 * key in JWK [JWK] format (not an X.509 certificate value). Use of the
	 * sub_jwk Claim is REQUIRED when the OP is a Self-Issued OP and is NOT
	 * RECOMMENDED when the OP is not Self-Issued.
	 * 
	 * @param subJwk
	 *            the subJwk to set
	 */
	public void setSubJwk(String subJwk) {
		this.subJwk = subJwk;
	}

}
