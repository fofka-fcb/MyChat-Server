package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class ChatServicesImpl implements ChatServices {
    private final ServicesOfServerImpl services;
    private final ClientServicesImpl client;
    private final Socket socket;

    @Override
    @SneakyThrows
    public void chat() {

        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        client.notifyMe("!chat!");
        String messageFromClient;
        while ((messageFromClient = readerFromClient.readLine()) != null) {
            if (messageFromClient.startsWith("!Exit")) {
                client.notifyMe("!Exit");
                services.removeObserver(client);
                break;
            }
            services.notifyObservers(client.user.getNickname(), messageFromClient);
        }
    }
}
