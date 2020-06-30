package br.com.trabfinal.core;

import br.com.trabfinal.job.ConversationHandlerJob;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer extends Thread{

    public static ArrayList<String> usernames;
    public static ArrayList<PrintWriter> printWriters;
    public static ServerSocket chatServerSocket;

    public ChatServer(){

    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        usernames = new ArrayList<>();
        printWriters = new ArrayList<>();

        try {
            chatServerSocket = new ServerSocket(9806);

            while (true) {
                Socket socket = chatServerSocket.accept();

                ConversationHandlerJob conversationHandlerJob = new ConversationHandlerJob(socket);

                conversationHandlerJob.start();
            }

        } catch (IOException e) {

        }


    }

}
