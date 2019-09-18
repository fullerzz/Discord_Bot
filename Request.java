package com.github.fullerzz;

public class Request {
    final String requesterName;
    final String request;

    public Request(String name, String userRequest) {
        requesterName = name;
        String regex = "!request\\b\\s*";
        request = userRequest.replaceAll(regex, ""); // Eliminates command from request
    }
}
