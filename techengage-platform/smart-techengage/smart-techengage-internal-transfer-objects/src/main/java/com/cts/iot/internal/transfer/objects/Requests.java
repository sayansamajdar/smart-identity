package com.cts.iot.internal.transfer.objects;

import java.util.ArrayList;
import java.util.List;

public class Requests {

    private List<Request> requests = new ArrayList<>();

    public void addRequest(Request request) {
	requests.add(request);
    }

    public List<Request> getRequests() {
	return requests;
    }

    public void setRequests(List<Request> requests) {
	this.requests = requests;
    }
}
