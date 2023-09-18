package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.UserDao;
import org.example.domain.User;

import java.io.*;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable, Observer {
    private final Socket socket;
    private User user;
    private final ServicesOfServerImpl services;
    private final UserDao userDao;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("new connection accepted");

        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (true) {
            if (authorization(readerFromClient) == true) {
                notifyMe("Authorization accept");
                services.addObserver(this);
                String messageFromClient;
                while ((messageFromClient = readerFromClient.readLine()) != null) {
                    services.removeObserver(this);
                    services.notifyObservers(user.getNickname() + ": " + messageFromClient);
                    services.addObserver(this);
                }
            } else {
                notifyMe("Log in again");
            }
        }
    }

    @SneakyThrows
    private boolean authorization(BufferedReader readerFromClient) {
        String authorizationMessage;
        while ((authorizationMessage = readerFromClient.readLine()) != null) {
            if (authorizationMessage.startsWith("!auto!")) {
                String nickname = authorizationMessage.substring(6);
                if (userDao.findByNickname(nickname) == true) {
                    user = new User(nickname);
                    return true;
                } else {
                    return false;
                }
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
