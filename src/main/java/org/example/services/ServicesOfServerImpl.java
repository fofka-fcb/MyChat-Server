package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.MessageDao;
import org.example.dao.MessageDaoImpl;
import org.example.dao.UserDao;
import org.example.dao.UserDaoImpl;
import org.example.utils.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
public class ServicesOfServerImpl implements ServicesOfServer {
    public static int PORT = 4444;
    public final List<Observer> clients = new LinkedList<>();
    private final UserDao userDao = new UserDaoImpl(new Props());
    private final MessageDao messageDao = new MessageDaoImpl(new Props());
    public static final Logger log = LoggerFactory.getLogger(ServicesOfServerImpl.class.getSimpleName());

    @SneakyThrows
    @Override
    public void start() {
        ServerSocket server = new ServerSocket(PORT);
        log.info("Server has started, time = {}", LocalDateTime.now());

        while (true) {
            Socket socket = server.accept();
            if (socket != null) {
                log.info("New user connected, time = {}, port = {}", LocalDateTime.now(), socket.getPort());
                new Thread(new ClientRunnable(socket, this, userDao, messageDao)).start();
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
    public void notifyObservers(String nickname, String message) {
        for (Observer observerInList : clients) {
            observerInList.notifyUsers(nickname, message);
        }
    }
}
