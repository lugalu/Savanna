package br.com.trabfinal.job;


import br.com.trabfinal.core.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ConversationHandlerJob extends Thread {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String username;

    public ConversationHandlerJob(Socket socket){
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername(){
        return this.username;
    }

    public BufferedReader getInput() {
        return input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

       public void setInput(BufferedReader input) {
        this.input = input;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        try {

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true);

            output.println("USERNAME");
            setUsername(input.readLine());

            if (getUsername() == null){
                return;
            }

            if (!ChatServer.usernames.contains(getUsername())){
                ChatServer.usernames.add(getUsername());
                getOutput().println("ACCEPTED");
                ChatServer.printWriters.add(getOutput());
            }else {
                getOutput().println("REJECTED");
            }

            while (true){
                String clientMessage = getInput().readLine();
                if (clientMessage == null){
                    return;
                }

                if (clientMessage.equals("LEAVING")){
                    ChatServer.usernames.remove(getUsername());
                    getOutput().println("LEAVING");
                    getOutput().close();
                    getInput().close();
                    getSocket().close();
                    break;
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM HH:mm");
                LocalDateTime now = LocalDateTime.now();

                for (PrintWriter printWriter: ChatServer.printWriters){
                    printWriter.println(dtf.format(now)+" - "+getUsername()+":"+clientMessage);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
