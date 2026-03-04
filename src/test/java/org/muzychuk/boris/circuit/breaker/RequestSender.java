package org.muzychuk.boris.circuit.breaker;

public class RequestSender {


    public  Response successRequest() {
        return new Response("SUCCESS");
    }

    public Response failRequest() {
        throw new RuntimeException("Request is failed");
    }
}
