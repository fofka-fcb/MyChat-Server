package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.domain.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class ChatServicesImpl implements ChatServices {
    private final ServicesOfServerImpl services;
    private final ClientServicesImpl client;
    private final Socket socket;
    private Message message;

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
            message = new Message(client.user.getNickname(), messageFromClient);
            services.notifyObservers(message.getFrom(), message.getTo_text());
        }
    }
}
