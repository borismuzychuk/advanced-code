package org.muzychuk.boris.circuit.breaker;

public class RequestSender {


    public  Response successRequest() {
        return new Response("SUCCESS");
    }
}
