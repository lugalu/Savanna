package br.com.trabfinal.models;

import java.util.ArrayList;

public class User {
    private String username;
    private String password;
    private int userCode;
    private ArrayList<Product> products;

    public User(String username,String password,int userCode){
        setUsername(username);
        setPassword(password);
        setUserCode(userCode);
        products = new ArrayList<Product>();
    }

    public int getUserCode() {
        return userCode;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    private void setUsername(String username) {
        this.username = username;
    }

    private void setPassword(String password) {
        this.password = password;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }

    public void addProduct(Product product){
        this.getProducts().add(product);
    }
}
