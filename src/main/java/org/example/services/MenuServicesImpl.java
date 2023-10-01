package org.example.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

@RequiredArgsConstructor
public class MenuServicesImpl implements MenuServices {
    private final ServicesOfServerImpl services;
    private final ClientServicesImpl client;
    private final Socket socket;
    public static final Logger log = LoggerFactory.getLogger(MenuServicesImpl.class.getSimpleName());

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
                        log.info("User '" + client.user.getNickname() + "' has authorization");
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
                        log.info("User '" + client.user.getNickname() + "' has registration");
                        chat = true;
                        break;
                    } else {
                        client.notifyMe("Reg again");
                    }
                }
            } else {
                client.notifyMe("Try again");
            }
            if (chat) {
                break;
            }
        }
    }
}
