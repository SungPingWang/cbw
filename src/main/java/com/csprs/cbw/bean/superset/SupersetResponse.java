package com.csprs.cbw.bean.superset;

public class SupersetResponse {
	
	private String access_token;
	private String refresh_token;
	@Override
	public String toString() {
		return "SupersetResponse [access_token=" + access_token + ", refresh_token=" + refresh_token + "]";
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getRefresh_token() {
		return refresh_token;
	}
	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}
	public SupersetResponse(String access_token, String refresh_token) {
		super();
		this.access_token = access_token;
		this.refresh_token = refresh_token;
	}
	public SupersetResponse() {
		super();
	}
}
