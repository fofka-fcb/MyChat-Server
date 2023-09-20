package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.UserDao;

import java.net.Socket;

@RequiredArgsConstructor
public class ClientRunnable implements Runnable {
    private final Socket socket;
    private final ServicesOfServerImpl services;
    private final UserDao userDao;

    @SneakyThrows
    @Override
    public void run() {
        System.out.println("new connection accepted");

        ClientServicesImpl client = new ClientServicesImpl(userDao, socket);

        MenuServices menuServices = new MenuServicesImpl(services, client, socket);
        menuServices.menu();
    }
}
