package com.bridgelabz.todo;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMethod;

public class JsonRequest {

	private String url;

	private Map<String, Object> headers;

	private Map<String, Object> body;

	private RequestMethod method;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, Object> headers) {
		this.headers = headers;
	}

	public Map<String, Object> getBody() {
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}

	public RequestMethod getMethod() {
		return method;
	}

	public void setMethod(RequestMethod method) {
		this.method = method;
	}

}
