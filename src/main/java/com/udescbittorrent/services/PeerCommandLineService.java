package com.udescbittorrent.services;

import lombok.var;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

public class PeerCommandLineService extends Thread {
    public void run() {
        String command = null;
        while(!"/sair".equals(command)) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Digite o comando: ");
            String userInput = getUserInput(br);
            if(!userInput.startsWith("/")){
                System.out.println("Comando inválido!");
            } else {
                var splitUserInput = Arrays.stream(userInput.split(" ")).collect(Collectors.toList());
                command = splitUserInput.get(0);
                if("/send".equals(command)){
                    String sendType = splitUserInput.get(1);
                    String destinationUser = splitUserInput.get(2);
                    String text = splitUserInput.stream().skip(3).collect(Collectors.joining(" "));
                    if("message".equals(sendType)){
                        PeerClientService.sendMessage(text, destinationUser);
                    } else if("file".equals(sendType)) {
                        PeerClientService.sendFile(text, destinationUser);
                    }
                } else if ("/users".equals(command)) {
                    System.out.println("Usuários: ");
                    PeerClientService.get().getTracker().getPeerTable().values()
                        .forEach(e-> System.out.println(e.getUserName()));
                }
            }
        }
    }

    private static String getUserInput(BufferedReader br) {
        String userInput = null;
        try {
            userInput = br.readLine();
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
        return userInput;
    }
}
