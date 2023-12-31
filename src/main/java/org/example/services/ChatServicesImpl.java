package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.example.dao.MessageDao;
import org.example.domain.Message;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class ChatServicesImpl implements ChatServices {
    private final ServicesOfServerImpl services;
    private final ClientServicesImpl client;
    private final Socket socket;
    private final MessageDao messageDao;

    @Override
    @SneakyThrows
    public void chat() {
        BufferedReader readerFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Message message = new Message();

        client.notifyMe("!chat!");

        messageDao.notifyMeAboutMessages();

        String messageFromClient;
        while ((messageFromClient = readerFromClient.readLine()) != null) {
            if (messageFromClient.startsWith("!Exit")) {
                client.notifyMe("!Exit");
                services.removeObserver(client);
                break;
            }
            message.setFrom(client.user.getNickname());
            message.setTo_text(messageFromClient);

            messageDao.recordMessage(message.getFrom(), message.getTo_text());

            services.notifyObservers(message.getFrom(), message.getTo_text());
        }
    }
}
