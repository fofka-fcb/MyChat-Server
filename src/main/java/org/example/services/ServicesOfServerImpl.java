package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.utils.Props;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

@RequiredArgsConstructor
public class ServicesOfServerImpl implements ServicesOfServer {
    public static int PORT = 4444;
    public final List<Observer> clients = new LinkedList<>();
    private final UserDao userDao = new UserDaoImpl(new Props());

    @SneakyThrows
    @Override
    public void start() {
        ServerSocket server = new ServerSocket(PORT);
        System.out.println("=SERVER START=");

        while (true) {
            Socket socket = server.accept();
            if (socket != null) {
                Thread thread = new Thread(new ClientRunnable(socket, this, userDao));
                thread.start();
            }
        }
    }

    @Override
    public void addObserver(Observer observer) {
        clients.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        clients.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observerInList : clients) {
            observerInList.notifyMe(message);
        }
    }
}
