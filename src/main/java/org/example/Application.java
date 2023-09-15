package org.example;

import org.example.services.ServicesOfServer;
import org.example.services.ServicesOfServerImpl;


public class Application {

    public static void main(String[] args){
        ServicesOfServer server = new ServicesOfServerImpl();
        server.start();
    }
}