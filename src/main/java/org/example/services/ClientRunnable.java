package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.User;

import java.io.*;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private User user;
    private final ServicesOfServerImpl services;

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
                    services.removeObserver(this);
                    services.notifyObservers(user.getNickname() + ": " + messageFromClient);
                    services.addObserver(this);
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
                user = new User(nickname); // Создание юзера и присваивание ему никнейма
//                addUser(user);
                return true;
            }
        }
        return false;
    }

//    public void addUser(User user){
//        services.listOFUsers.add(user);
//    }
//
//    public void removeUser(User user){
//        services.listOFUsers.remove(user);
//    }
//
//    public void notifyUsers(String message){
//        for (User user: services.listOFUsers){
//
//        }
//    }

    @SneakyThrows
    @Override
    public void notifyMe(String message) {
        PrintWriter writerForClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        writerForClient.println(message);
        writerForClient.flush();
    }
}
