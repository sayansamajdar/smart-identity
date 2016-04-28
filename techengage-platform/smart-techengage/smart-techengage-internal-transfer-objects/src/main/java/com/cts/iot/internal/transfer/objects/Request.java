package com.cts.iot.internal.transfer.objects;

public class Request {

    private String method;
    private String path;
    private Object body;

    public String getMethod() {
	return method;
    }

    public String getPath() {
	return path;
    }

    public Object getBody() {
	return body;
    }

    public void setMethod(String method) {
	this.method = method;
    }

    public void setPath(String path) {
	this.path = path;
    }

    public void setBody(Object body) {
	this.body = body;
    }

}
