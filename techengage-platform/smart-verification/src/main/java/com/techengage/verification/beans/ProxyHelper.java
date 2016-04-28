package com.techengage.verification.beans;

public class ProxyHelper {
    public boolean isProxyEnabled() {
	String strProxyEnabled = System.getProperty("proxyEnabled");
	return Boolean.getBoolean(strProxyEnabled);
    }
}
