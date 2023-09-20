package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.UserDao;

import java.io.*;
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

        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        ClientServicesImpl client = new ClientServicesImpl(userDao, socket);

        //Логика полноценной авторизации/регистрации
        while (true) {
            boolean chat = false;
            String menuNum = readerFromClient.readLine();
            if (menuNum.startsWith("1")) {
                client.notifyMe("!autho!");
                while (true) {
                    if (client.authorization(readerFromClient) == true) {
                        client.notifyMe("Authorization accepted");
                        services.addObserver(client);
                        chat = true;
                        break;
                    } else {
                        client.notifyMe("Log in again");
                    }
                }
            } else if (menuNum.startsWith("2")) {
                client.notifyMe("!reg!");
                while (true) {
                    if (client.registration(readerFromClient) == true) {
                        client.notifyMe("Registration accepted");
                        services.addObserver(client);
                        chat = true;
                        break;
                    } else {
                        client.notifyMe("Reg is not accepted");
                    }
                }
            }
            if (chat == true) {
                client.notifyMe("!chat!");
                String messageFromClient;
                while ((messageFromClient = readerFromClient.readLine()) != null) {
                    services.removeObserver(client);
                    services.notifyObservers(client.user.getNickname() + ": " + messageFromClient);
                    services.addObserver(client);
                }
            }
        }
    }
}
