package org.example.services;

import java.io.BufferedReader;

public interface ClientServices {
    boolean authorization(BufferedReader readerFromClient);

    boolean registration(BufferedReader readerFromClient);
}
