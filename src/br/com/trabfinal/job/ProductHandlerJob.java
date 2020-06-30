package br.com.trabfinal.job;

import br.com.trabfinal.models.Product;
import br.com.trabfinal.models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ProductHandlerJob extends Thread{

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private static ArrayList<User> users;



    public ProductHandlerJob(Socket socket) throws IOException {
        setSocket(socket);
        input = new BufferedReader(new InputStreamReader(this.getSocket().getInputStream()));
        output = new PrintWriter(this.getSocket().getOutputStream(),true);

        setUsers();
    }

    private void setSocket(Socket server) {
        this.socket = server;
    }

    private void setUsers(){
        if (users == null){
            users = new ArrayList<>();
        }
    }

    public Socket getSocket() {
        return this.socket;
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        boolean flag = true;
        while (flag){
            String code[] ={""};                                                                    //run until it's interrupted
            try {
                 code =input.readLine().split(":");                                                 //receives the code and other information needed to work
            } catch (IOException e) {
                e.printStackTrace();
            }
            int option = Integer.parseInt(code[0]);                                                 //get's the option as a integer
            switch (option) {
                case -1:
                    try {
                        flag = false;
                        input.close();
                        output.close();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0:                                                                             //Login option
                    System.out.println("Tentando logar");
                    checkLogin(code);
                    break;
                case 1:                                                                             //register user
                    System.out.println("Tentando registrar");
                    registerNewUser(code);
                    break;
                case 2:                                                                             //register product
                    System.out.println("Tentando registrar Produto");
                    registerNewProduct(code);
                    break;
                case 3:                                                                             //remove product
                    removeProduct(code);
                    break;
            }
        }
    }

    private void checkLogin(String login[]){                                                        //Checks the existence of the name if match both username and password it advances to next screen
        boolean flag = false;
        if (!users.isEmpty()) {
            for (User user : users) {
                if (user.getUsername().equals(login[1]) && user.getPassword().equals(login[2])) {
                    System.out.println("Login aceito");
                    output.println("LOGINACCEPTED:" + user.getProducts().size()+user.getUserCode());
                    if (user.getProducts().size() > 0) {
                        System.out.println("ha produtos cadastrados");
                        for (Product product : user.getProducts()) {                                //If the user already had products it gives them back so the user don't loose data
                            output.println(product.getProductName() + ":" + product.getProductId()
                                    + ":" + product.getProductPrice()+":"+product.getProductQuantity());
                        }
                    }
                    flag = true;
                }
            }
        }

       if (!flag){
           System.out.println("login nao reconhecido");
           output.println("LOGINREJECTED:0");
           System.out.println("mandei msg");
       }
    }

    private void registerNewUser(String credentials[]){
        boolean flag = true;

        if (!users.isEmpty()) {
            for (User user: users){
                if (user.getUsername().equals(credentials[1])){                                     //checks if the username is available.
                    flag = false;
                }
            }
            if(flag){
                System.out.println("cadastrado com sucesso");
                users.add(new User(credentials[1],credentials[2],users.size()));
                int opt = users.size()-1;
                output.println("VALIDUSERNAME:"+opt);
            }else{
                System.out.println("username invalido");
                output.println("INVALIDUSERNAME");
            }
        }else{
            System.out.println("cadastrado com sucesso ");
            users.add(new User(credentials[1],credentials[2],0));

            output.println("VALIDUSERNAME:"+0);

        }

    }

    private void registerNewProduct(String productData[]){                                          //checks if the product code is available
        boolean flag = false;
        for (User user: users){
            if (!user.getProducts().isEmpty()) {
                for (Product product : user.getProducts()) {
                    if (product.getProductId().equals(productData[2])) {
                        flag = true;
                        break;
                    }

                }
                if (flag){
                    break;
                }
            }
        }

        if (!flag){
            User user = users.get(Integer.parseInt(productData[5]));
            user.addProduct(new Product(productData[1],productData[2],Float.parseFloat(productData[3]),Integer.parseInt(productData[4])));
            output.println("PRODUCTACCEPTED");
        }else{
            output.println("PRODUCTDENIED");
        }
    }

    private void removeProduct(String removeData[]){                                                //removes product
        boolean flag = false;
        User user = users.get(Integer.parseInt(removeData[1]));
        if (user.getProducts().get(Integer.parseInt(removeData[2])) != null){
            output.println("REMOVESUCESSFUL");
            user.getProducts().remove(Integer.parseInt(removeData[2]));
            flag = true;
            }


        if (!flag){
            output.println("REMOVEFAILED");
        }
    }
}


