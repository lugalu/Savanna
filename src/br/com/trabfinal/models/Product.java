package br.com.trabfinal.models;

public class Product {
    private String productName;
    private String productId;
    private float productPrice;
    private int productQuantity;

    public Product(String productName,String productId,float productPrice,int productQuantity){
        setProductName(productName);
        setProductId(productId);
        setProductPrice(productPrice);
        setProductQuantity(productQuantity);
    }

    public String getProductName() {
        return productName;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public String getProductId() {
        return productId;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    private void setProductName(String productName) {
        this.productName = productName;
    }

    private void setProductId(String productId) {
        this.productId = productId;
    }

    private void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductQuantity(int quantity) {
        productQuantity = quantity;
    }

    @Override
    public String toString() {                                                                      //Uses HTML to format the JList included in UI class.
        return "<html>Produto:" + productName +
                " Codigo do produto: " + productId +
                "<br>Preço do produto:" + productPrice+" Quantidade:"
                +productQuantity+"<html>";
    }
}
