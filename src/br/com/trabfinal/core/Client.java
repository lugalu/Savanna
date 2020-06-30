package br.com.trabfinal.core;


import br.com.trabfinal.Utils.UI;
import br.com.trabfinal.Utils.Utils;
import br.com.trabfinal.models.Product;
import br.com.trabfinal.models.User;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private UI screens;
    private Chat chat;
    private static BufferedReader input;
    private static PrintWriter output;
    private static Socket socket;
    private static Socket chatSocket = null;
    private User user;

    public Client(){

    }

    //Getters and Setters
    public UI getTelas() {
        return screens;
    }

    public static PrintWriter getOutput() {
        return output;
    }

    public static BufferedReader getInput() {
        return input;
    }

    public User getUser() {
        return user;
    }

    public static Socket getSocket() {
        return socket;
    }

    public Chat getChat() {
        return chat;
    }

    private void setTelas(UI telas) {
        this.screens = telas;
    }

    //Main
    public static void main(String[] args) throws Exception {
        socket = new Socket("localhost",9800);
        Client client = new Client();

        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(),true);

        client.setTelas(new UI(client));
        client.getTelas().start();



        client.getTelas().loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getTelas().loginScreen();
            }
        });

        client.getTelas().registerUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.getTelas().registerScreen();
            }
        });



        client.getTelas().exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    output.println("-1"+": ");
                    socket.close();
                    input.close();
                    output.close();
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }


    public void tryLogin(String login) throws IOException {                                         //Sends the information to the productHandlerJob to log in

        output.println("0:"+login);
        String code[] = input.readLine().split(":");

        if (code[0].equals("LOGINACCEPTED")){
            String splittedLogin[] = login.split(":");
            user = new User(splittedLogin[0],splittedLogin[1],Integer.parseInt(code[1]));
            if (Integer.parseInt(code[1])>0) {
                int numOfElements = Integer.parseInt(code[1]);
                for (int i = 0; i < numOfElements; i++) {
                    String productCodes[] = input.readLine().split(":");
                    user.addProduct(new Product(productCodes[0], productCodes[1]
                            , Float.parseFloat(productCodes[2]),Integer.parseInt(productCodes[3])));
                }
            }
            getTelas().logged();
        }else {
            Utils.message("Login Invalido", "ERRO", true);
        }


    }

    public void tryRegister(String userData) throws IOException {                                   //Sends the information to ProductHandlerJob to register.
        output.println("1:"+userData);
        String response[] = input.readLine().split(":");

        if (response[0].equals("VALIDUSERNAME")){
            String splitedData[] = userData.split(":");
            user = new User(splitedData[0],splitedData[1],Integer.parseInt(response[1]));
            getTelas().logged();
        }else{
            Utils.message("Username Invalido","ERRO",true);
        }
    }



    public void tryRegisterProduct(String productData) throws IOException {                         //Sends the information to ProductHandlerJob to register.
        output.println("2:"+productData);
        String code = input.readLine();
        if (code.equals("PRODUCTACCEPTED")){
            String productDataSlipted[] = productData.split(":");
            getUser().addProduct(new Product(productDataSlipted[0]
                    ,productDataSlipted[1]
                    ,Float.parseFloat(productDataSlipted[2]),Integer.parseInt(productDataSlipted[3])));
            getTelas().logged();
        }else{
            Utils.message("O Produto nao foi aceito","ERRO",true);
        }
    }

    public void removeProduct(int index) throws IOException {                                       //Removes a product
        output.println("3:"+user.getUserCode()+":"+index);
        String code = input.readLine();
        if (code.equals("REMOVESUCESSFUL")){
            user.getProducts().remove(index);

        }else{
            Utils.message("O produto nao pode ser removido","erro",true);

        }
        getTelas().logged();
    }

    public void startChat(){                                                                        //start or run a new thread which handle the chat
        try {
            chatSocket = new Socket("localhost",9806);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (chat == null){
            chat = new Chat(chatSocket,user.getUsername());
            chat.start();
        }


        getChat().getChatWindow().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    getChat().getOutput().println(-1);
                    getChat().getOutput().close();
                    getChat().getInput().close();
                    getChat().getChatSocket().close();
                    getTelas().chatButton.setEnabled(true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                super.windowClosing(e);
            }
        });
    }

}
