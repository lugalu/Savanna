package br.com.trabfinal.core;

import br.com.trabfinal.job.ProductHandlerJob;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {



    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9800);
        ChatServer chatServer = new ChatServer();                                                   //"Sub server" which handles the chat operations
        chatServer.start();

        while (true){
            Socket socket = serverSocket.accept();
            System.out.println("usuario conectou: "+socket);

            ProductHandlerJob productHandlerJob = new ProductHandlerJob(socket);
            productHandlerJob.start();

        }

    }
}
