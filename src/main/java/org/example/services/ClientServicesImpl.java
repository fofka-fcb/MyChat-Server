package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.UserDao;
import org.example.domain.User;
import org.example.exceptions.UserNotFoundException;

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

    @Override
    @SneakyThrows
    public boolean authorization(BufferedReader readerFromClient) {
        while (true) {
            String login = readerFromClient.readLine();
            if (login.startsWith("!log!")) {
                login = login.substring(5);
                String password = readerFromClient.readLine();
                if (password.startsWith("!pas!")) {
                    password = password.substring(5);
                    try {
                        user = userDao.findUser(login, password);
                        return true;
                    } catch (UserNotFoundException exception){
                        System.out.println("User not found");
                        return false;
                    }
                }
            }
        }
    }

    @Override
    @SneakyThrows
    public boolean registration(BufferedReader readerFromClient) {
        String registrationMessage;
        while ((registrationMessage = readerFromClient.readLine()) != null) {
            if (registrationMessage.startsWith("!reg!")) {
                String nickname = registrationMessage.substring(5);
                if (userDao.regNewNickname(nickname) == true) {
                    user = new User(nickname);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    @SneakyThrows
    public void notifyMe(String message) {
        PrintWriter writerForClient = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        writerForClient.println(message);
        writerForClient.flush();
    }
}
