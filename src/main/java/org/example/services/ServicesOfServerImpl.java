package org.example.services;

import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ServicesOfServerImpl implements ServicesOfServer {
    public static int PORT = 4444;
    public final List<Observer> clients = new LinkedList<>();

    @SneakyThrows
    @Override
    public void start() {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("=SERVER START=");

        while (true) {
            Socket socket = server.accept();
            if (socket != null) {
                Thread thread = new Thread(new ClientRunnable(socket, this));
                thread.start();
            }
        }
    }

    @Override
    public void addObserver(Observer observer) {
        clients.add(observer);
        System.out.println("client connected to server");
    }

    @Override
    public void removeObserver(Observer observer) {
        clients.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer: clients){
            observer.notifyMe(message);
        }
    }
}
