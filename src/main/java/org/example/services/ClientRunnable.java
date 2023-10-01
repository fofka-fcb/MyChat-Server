package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.MessageDao;
import org.example.dao.UserDao;

import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable {
    private final Socket socket;
    private final ServicesOfServerImpl services;
    private final UserDao userDao;
    private final MessageDao messageDao;

    @SneakyThrows
    @Override
    public void run() {
        ClientServicesImpl client = new ClientServicesImpl(userDao, socket);

        while (true) {
            MenuServices menuServices = new MenuServicesImpl(services, client, socket);
            menuServices.menu();

            ChatServices chatServices = new ChatServicesImpl(services, client, socket, messageDao);
            chatServices.chat();
        }
    }
}
