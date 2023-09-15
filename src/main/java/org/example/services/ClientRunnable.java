package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.User;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private User user;
    private final ServicesOfServerImpl services;
//    private final List<User> listOfUsers = new LinkedList<>();


    @SneakyThrows
    @Override
    public void run() {
        System.out.println("new connection accepted");

        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (true) {
            if (authorization(readerFromClient)) {
                services.addObserver(this);
                String messageFromClient;
                while ((messageFromClient = readerFromClient.readLine()) != null) {
                    services.notifyObservers(user.getNickname() + ": " + messageFromClient);
                }
            }
        }
    }

    @SneakyThrows
    private boolean authorization(BufferedReader readerFromClient) {
        String authorizationMessage;
        while ((authorizationMessage = readerFromClient.readLine()) != null) {
            if (authorizationMessage.startsWith("!auto!")) {
                String nickname = authorizationMessage.substring(6);
                user = new User(nickname);
                return true;
            }
        }
        return false;
    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter writerForClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        writerForClient.println(message);
        writerForClient.flush();
    }
}
