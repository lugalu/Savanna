package br.com.trabfinal.core;

import br.com.trabfinal.Utils.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Chat extends Thread {

    private String username;

    private Socket chatSocket;
    public static BufferedReader input;
    public static PrintWriter output;

    public static JFrame chatWindow = new JFrame("Chat de fornecedores");
    public static JTextArea chatArea = new JTextArea(20,40);
    public static JTextField textField = new JTextField(40);
    public static JLabel blankLabel = new JLabel("                              ");
    public static JButton sendButton = new JButton("enviar");

    public Chat(Socket chatSocket,String username){
        setChatSocket(chatSocket);
        setUsername(username);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                output.println(textField.getText());
                textField.setText(null);
            }
        });

        chatWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                try {
                    output.println("LEAVING");
                    output.close();
                    input.close();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                super.windowClosing(e);
            }
        });
    }

    public String getUsername() {
        return username;
    }

    public static BufferedReader getInput() {
        return input;
    }

    public static PrintWriter getOutput() {
        return output;
    }

    public static JFrame getChatWindow() {
        return chatWindow;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getChatSocket() {
        return chatSocket;
    }

    public void setChatSocket(Socket chatSocket) {
        this.chatSocket = chatSocket;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();

        try {
            input = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
            output = new PrintWriter(chatSocket.getOutputStream(),true);
            startWindow();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true){
               String serverMsg = input.readLine();
                if (serverMsg.equals("USERNAME")){
                    output.println(getUsername());
                }else if (serverMsg.equals("ACCEPTED")) {
                    textField.setEditable(true);
                }else if(serverMsg.equals("REJECTED")){
                    Utils.message("Usuario ja esta logado no chat","ERRO",true);
                }else if(serverMsg.equals("LEAVING")){
                    break;
                }else {
                    chatArea.append(serverMsg+"\n");
                }
            }
        } catch (IOException e) {

        }



    }

    public void startWindow(){
        chatWindow.setLayout(new FlowLayout());
        chatWindow.getContentPane().setBackground(new Color(152, 131,66,255));

        sendButton.setBackground(new Color(255,255,255,255));
        sendButton.setFocusPainted(false);

        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);

        chatWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chatWindow.setSize(475,500);
        chatWindow.setVisible(true);


        chatArea.setEditable(false);
        textField.setEditable(false);

    }
}
