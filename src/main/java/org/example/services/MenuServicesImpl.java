package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class MenuServicesImpl implements MenuServices {
    private final ServicesOfServerImpl services;
    private final ClientServicesImpl client;
    private final Socket socket;

    @SneakyThrows
    @Override
    public void menu() {

        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        while (true) {
            boolean chat = false;
            String menuNum = readerFromClient.readLine();
            if (menuNum.startsWith("1")) {
                client.notifyMe("!autho!");
                while (true) {
                    if (client.authorization(readerFromClient)) {
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
                    if (client.registration(readerFromClient)) {
                        client.notifyMe("Registration accepted");
                        services.addObserver(client);
                        chat = true;
                        break;
                    } else {
                        client.notifyMe("Reg is not accepted");
                    }
                }
            }
            if (chat) {
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
