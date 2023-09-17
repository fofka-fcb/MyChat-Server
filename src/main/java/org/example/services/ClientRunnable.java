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
                //user = new User(nickname); // Создание юзера и присваивание ему никнейма
                user = userDao.findByNickname(nickname); // Создание юзера после проверки существования его в базе данных
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
