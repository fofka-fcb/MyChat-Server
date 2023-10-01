package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.UserDao;
import org.example.domain.User;
import org.example.exceptions.UserNotCreatedException;
import org.example.exceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

@RequiredArgsConstructor
public class ClientServicesImpl implements ClientServices, Observer {
    private final UserDao userDao;
    private final Socket socket;
    protected User user;
    public static final Logger log = LoggerFactory.getLogger(ClientServicesImpl.class.getSimpleName());

    @Override
    @SneakyThrows
    public boolean authorization(BufferedReader readerFromClient) {
        while (true) {
            String login = readerFromClient.readLine();
            String password = readerFromClient.readLine();
            try {
                user = userDao.findUser(login, password);
                return true;
            } catch (UserNotFoundException exception) {
                log.error("Login = {} and password = {} not found, port = {}", login, password, socket.getPort());
                return false;
            }
        }
    }


    @Override
    @SneakyThrows
    public boolean registration(BufferedReader readerFromClient) {
        while (true) {
            String login = readerFromClient.readLine();
            if (userDao.checkLogin(login)) {
                notifyMe("Login accepted");
                String password = readerFromClient.readLine();
                String nickname;
                while ((nickname = readerFromClient.readLine()) != null) {
                    if (userDao.checkNickname(nickname)) {
                        notifyMe("Nickname accept");
                        try {
                            user = userDao.regNewNickname(login, password, nickname);
                            return true;
                        } catch (UserNotCreatedException exception) {
                            log.error("New user not created, port = {}", socket.getPort());
                            return false;
                        }
                    } else {
                        notifyMe("Nickname already exists");
                    }
                }
            } else {
                notifyMe("Email already exists");
            }
        }
    }

    @Override
    @SneakyThrows
    public void notifyUsers(String nickname, String message) {
        if (!nickname.equals(user.getNickname())) {
            PrintWriter writerForClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
            writerForClient.println(nickname + ": " + message);
            writerForClient.flush();
        }
    }

    @Override
    @SneakyThrows
    public void notifyMe(String message) {
        PrintWriter writerForClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        writerForClient.println(message);
        writerForClient.flush();
    }
}
