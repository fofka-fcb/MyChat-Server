package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.MessageDao;
import org.example.dao.MessageDaoImpl;
import org.example.dao.UserDao;
import org.example.utils.Props;

import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable {
    private final Socket socket;
    private final ServicesOfServerImpl services;
    private final UserDao userDao;

    @SneakyThrows
    @Override
    public void run() {
        ClientServicesImpl client = new ClientServicesImpl(userDao, socket);

        MessageDao messageDao = new MessageDaoImpl(new Props(), client);

        while (true) {
            MenuServices menuServices = new MenuServicesImpl(services, client, socket);
            menuServices.menu();

            ChatServices chatServices = new ChatServicesImpl(services, client, socket, messageDao);
            chatServices.chat();
        }
    }
}
