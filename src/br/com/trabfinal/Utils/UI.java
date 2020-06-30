package br.com.trabfinal.Utils;

import br.com.trabfinal.core.Client;
import br.com.trabfinal.models.Product;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.regex.Pattern;

/**
 * A classe UI inicializa e cuida de atualizar as telas do Java swing
 */
public class UI extends Thread{
    //Attributes
    private Client client;
    final int[] removeIndex = new int[1];

    public UI(){

    }


    //general
    public static String savannaLogo = "/image/savannalogoSmall.png";                               //icon logo.
    public static String savannaBanner = "/image/savannalogoBig.png";                               //banner.
    public static URL icon = UI.class.getResource(savannaLogo);                                     //string to path.
    public static URL banner = UI.class.getResource(savannaBanner);                                 //string to path.
    public static JFrame generalWindow = new JFrame("");                                            //window that will be updated during the execution of the program.
    public static JPasswordField passwordField = new JPasswordField();                              //Password Field for login or register.
    public static JTextField username = new JTextField();                                           //username.
    JList productList = new JList();
    DefaultListModel listContent = new DefaultListModel();

    //buttons
    public static JButton loginButton = new JButton("login");                                       //login button take multiple roles over the classes such as change of screen and sending information to other method.
    public static JButton registerUserButton = new JButton("cadastrar");                            //Register new users.
    public static JButton exitButton = new JButton("sair");                                         //close the connection to the server and exit the program with code: 0.
    public static JButton registerProductsButton = new JButton("cadastrar produtos");               //register new product if the product obey the rules.
    public static JButton removeProducts = new JButton("remover produtos");                            //edit products.
    public static JButton chatButton = new JButton("chat");

    public UI(Client client){
        this.client = client;
    }

    public void generateLoginWindow(){
        configureButtons();

        Image imageIcon = new ImageIcon(icon).getImage();

        generalWindow.setLayout(new BoxLayout(generalWindow.getContentPane(),BoxLayout.Y_AXIS));    //define the general window as a box layout.
        windowColor(152, 85, 33, 255);                                                              //background color #FCD528.
        generalWindow.setIconImage(imageIcon);                                                      //icon on taskbar or at the top right.

        addBanner();


        generalWindow.add(Box.createRigidArea(new Dimension(5,5)));                                  //rigid area is here to move the buttons.
        generalWindow.add(loginButton);                                                              //login button.
        generalWindow.add(registerUserButton);                                                       //Register users button.
        generalWindow.add(exitButton);

        generalWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                               //Set the 'X' to exit when pressed.
        generalWindow.setMaximumSize(new Dimension(475,400));                                       //Set the maximum size of the window.
        generalWindow.setMinimumSize(new Dimension(475,400));                                       //Set the minimum size of the window.

        packWindow();
        generalWindow.setVisible(true);


    }

    private void configureButtons(){
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);                                      //make the buttons align in the center.
        registerUserButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        setButtonSize(loginButton,90,20,3);
        setButtonBackground(loginButton,255,255,255,255);

        setButtonSize(registerUserButton,90,20,3);
        setButtonBackground(registerUserButton,255,255,255,255);

        setButtonSize(exitButton,90,20,3);
        setButtonBackground(exitButton,255,255,255,255);
   }

    private void addBanner(){                                                                       //Configure and add banners to/the general window.
       ImageIcon bannerIcon = new ImageIcon(banner);                                                //transform the path to image icon.
       Image img = bannerIcon.getImage();                                                           //transform the image icon to image.
        img = img.getScaledInstance(475,250,Image.SCALE_SMOOTH);                                    //resize the image.
       bannerIcon = new ImageIcon(img);                                                             //after resizing transform in image icon again.

       JLabel bannerLabel= new JLabel(bannerIcon);                                                  //banner label.
       bannerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);                                       //center the banner.
       generalWindow.add(bannerLabel);                                                              //add to the general window.
   }

    public void loginScreen(){                                                                      //login screen.
        erase();

        String[] login = {""};                                                                      //string that receives the user data.
        JLabel textLabel = new JLabel("Digite seus dados.");                                        //simple text to appear in the window.
        JLabel usernameLabel = new JLabel("username:");
        JLabel passwordLabel = new JLabel("password:");
        JButton backButton = new JButton("voltar");                                                 //Back button
        JButton loginBtn = new JButton("login");                                                    //login button send the information to be validated on the server.

        gridFormat();
        windowColor(152, 131, 66, 255);                                                             //weaker color for the background

        username.setAlignmentX(Component.CENTER_ALIGNMENT);                                         //set the alignment to the center of the window.
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);                                      //set the alignment to the left of the window.
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        setButtonBackground(loginBtn,255,255,255,255);
        setButtonSize(loginBtn,90,20,1);

        setButtonBackground(backButton,255,255,255,255);
        setButtonSize(backButton,90,20,1);

        generalWindow.add(textLabel,center());
        generalWindow.add(usernameLabel,left());                                                      //Organizes and set the position in the grid layout.
        generalWindow.add(username,right());
        generalWindow.add(passwordLabel,left());
        generalWindow.add(passwordField,right());
        generalWindow.add(loginBtn,center());
        generalWindow.add(backButton,center());
        packWindow();

        loginBtn.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               login[0] = username.getText()+":"+passwordField.getText();                           //get the username and password as string.
               username.setText(null);                                                              //blank the label
               passwordField.setText(null);

               try {
                   client.tryLogin(login[0]);                                                       //tries to login with the provided information
               } catch (IOException ex) {
                   ex.printStackTrace();
               }

           }
       });
       backButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               erase();
               username.setText(null);                                                              //blank the label
               passwordField.setText(null);
               client.getTelas().run();                                                             //goes back to the start screen;
           }
       });

    }

    public void registerScreen(){
        erase();

        String userData[] ={""};
        JLabel textLabel = new JLabel("<html>Digite seus dados para cadastro.<br>Senha deve ter no minimo 2 caracteres.<html>");
        JLabel usernameLabel = new JLabel("username:");
        JLabel passwordLabel = new JLabel("password:");
        JButton backButton = new JButton("voltar");
        JButton registerBtn = new JButton("register");

        gridFormat();
        windowColor(152, 131, 66, 255);

        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);                                      //set the alignment to the left of the window.
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        setButtonSize(backButton,90,20,1);
        setButtonBackground(backButton,255,255,255,255);

        setButtonSize(registerBtn,90,20,1);
        setButtonBackground(registerBtn,255,255,255,255);

        generalWindow.add(textLabel,center());
        generalWindow.add(usernameLabel,left());
        generalWindow.add(username,right());
        generalWindow.add(passwordLabel,left());
        generalWindow.add(passwordField,right());
        generalWindow.add(registerBtn,center());
        generalWindow.add(backButton,center());

        packWindow();

        registerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (passwordField.getText().length()>=2) {
                    userData[0] = username.getText() + ":" + passwordField.getText();               //get the username and password as string.
                    username.setText(null);                                                         //blank the label
                    passwordField.setText(null);
                    try {
                        client.tryRegister(userData[0]);                                            //tries to login with the provided information
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }else{
                    username.setText(null);                                                         //blank the label
                    passwordField.setText(null);
                    Utils.message("Senha muito curta","ERRO",true);
                }
            }
        });



        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                erase();
                username.setText(null);                                                              //blank the label
                passwordField.setText(null);
                client.getTelas().run();                                                             //goes back to the start screen;
            }
        });
    }

    public void logged(){
        erase();

        generalWindow.setLayout(new BorderLayout(4,4));

        Image imageIcon = new ImageIcon(icon).getImage();
        imageIcon = imageIcon.getScaledInstance(80,80,Image.SCALE_SMOOTH);

        CardLayout scrollerCard = new CardLayout();
        JPanel card = new JPanel(scrollerCard);

        JPanel buttonsGrid = new JPanel(new GridBagLayout());
        JPanel buttonsCentered = new JPanel(new GridLayout(0, 1, 10, 10));


        addList(listContent);

        productList.setModel(listContent);

        buttonsGrid.setBackground(new Color(152, 131, 66, 255));
        buttonsCentered.setBackground(new Color(152, 131, 66, 255));

        productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productList.setLayoutOrientation(JList.VERTICAL_WRAP);
        productList.setVisibleRowCount(0);

        JScrollPane listScroller = new JScrollPane(productList);
        listScroller.setPreferredSize(new Dimension(330,210));

        setButtonSize(chatButton,150,10,5);
        setButtonBackground(chatButton,255,255,255,255);
        setButtonSize(registerProductsButton,150,10,5);
        setButtonBackground(registerProductsButton,255,255,255,255);
        setButtonSize(removeProducts,150,10,5);
        setButtonBackground(removeProducts,255,255,255,255);

        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerProductsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeProducts.setAlignmentX(Component.CENTER_ALIGNMENT);

        removeProducts.setMultiClickThreshhold(2000);
        removeProducts.setEnabled(false);

        buttonsCentered.add(new JLabel(new ImageIcon(imageIcon)));
        buttonsCentered.add(registerProductsButton);
        buttonsCentered.add(removeProducts);
        buttonsCentered.add(chatButton);
        buttonsCentered.add(exitButton);

        card.add(listScroller);

        buttonsGrid.add(buttonsCentered);

        generalWindow.add(card);
        generalWindow.add(buttonsGrid,BorderLayout.LINE_START);

        packWindow();

        productList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                removeIndex[0] = productList.getSelectedIndex();
                removeProducts.setEnabled(true);
            }
        });

        registerProductsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerProductScreen();
            }
        });

        removeProducts.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeProducts.setEnabled(false);
                if (!productList.isSelectionEmpty()){
                    try {
                        client.removeProduct(removeIndex[0]);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                removeProducts.setEnabled(true);
            }
        });


        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                client.startChat();
                chatButton.setEnabled(false);

            }
        });


    }

    public void registerProductScreen(){
        erase();
        JLabel productNameLabel = new JLabel("Nome do produto");;
        JTextField productName = new JTextField();
        JLabel productCodeLabel = new JLabel("Codigo do produto");
        JTextField productCode = new JTextField();
        JLabel productPriceLabel = new JLabel("Preco do produto");
        JLabel productQuantitylabel = new JLabel("Quantidade Disponivel");

        NumberFormat productPrice = NumberFormat.getCurrencyInstance();
        productPrice.setMaximumFractionDigits(2);

        NumberFormatter productPriceFormatter = new NumberFormatter(productPrice);
        productPriceFormatter.setMinimum(0.0);
        productPriceFormatter.setMaximum(100000000.0);
        productPriceFormatter.setAllowsInvalid(false);
        productPriceFormatter.setOverwriteMode(true);

        JFormattedTextField productPriceFormatted = new JFormattedTextField(productPriceFormatter);


        NumberFormat productQuantity = NumberFormat.getInstance();


        NumberFormatter productQuantityFormatter = new NumberFormatter(productQuantity);
        productQuantityFormatter.setMinimum(0);
        productQuantityFormatter.setMaximum(Integer.MAX_VALUE);
        productQuantityFormatter.setAllowsInvalid(false);
        productQuantityFormatter.setOverwriteMode(true);

        JFormattedTextField productQuantityFormatted = new JFormattedTextField(productQuantityFormatter);
        productPriceFormatted.setValue(0);

        JButton sendBtn = new JButton("enviar");
        JButton backBtn = new JButton("voltar");

        gridFormat();
        windowColor(152, 131, 66, 255);                                                             //weaker color for the background

        setButtonBackground(sendBtn,255,255,255,255);
        setButtonSize(sendBtn,90,20,1);

        setButtonBackground(backBtn,255,255,255,255);
        setButtonSize(backBtn,90,20,1);

        generalWindow.add(productNameLabel,left());
        generalWindow.add(productName,right());
        generalWindow.add(productCodeLabel,left());
        generalWindow.add(productCode,right());
        generalWindow.add(productQuantitylabel,left());
        generalWindow.add(productQuantityFormatted,right());
        generalWindow.add(productPriceLabel,left());
        generalWindow.add(productPriceFormatted,right());
        generalWindow.add(sendBtn,center());
        generalWindow.add(backBtn,center());

        packWindow();

        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String productPriceSplited[] = productPriceFormatted.getText().split(Pattern.quote("$"));
                if (productName.getText().length() >2
                        && productCode.getText().length() >2
                        && Float.parseFloat(productPriceSplited[1])>0
                        && Integer.parseInt(productQuantityFormatted.getText())>0){
                    try {
                        client.tryRegisterProduct(productName.getText()+":"
                                +productCode.getText()+":"
                                +productPriceSplited[1]+":"+productQuantityFormatted.getText()+":"+client.getUser().getUserCode());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    productName.setText(null);
                    productCode.setText(null);
                    productPriceFormatted.setText(null);
                    productQuantityFormatted.setText(null);
                }else{
                    productName.setText(null);
                    productCode.setText(null);
                    productPriceFormatted.setText(null);
                    productQuantityFormatted.setText(null);
                    Utils.message("tamanho ou valor inserido invalido","ERRO",true);
                }

            }
        });

        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productName.setText(null);
                productCode.setText(null);
                productPriceFormatted.setText(null);
                logged();
            }
        });
    }

    public void addList(DefaultListModel listContent){
        listContent.removeAllElements();
        for (Product product: client.getUser().getProducts()) {
            listContent.addElement(product);

        }
    }

    //generic functions
    public void gridFormat(){
        generalWindow.setLayout(new GridBagLayout());
    }

    public void windowColor(int R,int G,int B, int Opacity){
        generalWindow.getContentPane().setBackground(new Color(R, G, B, Opacity));
    }

    public void setButtonSize(JButton button,int width,int height,int option){
        switch (option){
            case 1:
                button.setPreferredSize(new Dimension(width,height));                               //set the preferred size of the button.
                break;
            case 2:
                button.setMinimumSize(new Dimension(width,height));                                 //set the minimum size possible of the button.
                break;
            case 3:
                button.setMaximumSize(new Dimension(width,height));                                 //set the maximum size possible of the button.
                break;
            case 4:
                button.setSize(new Dimension(width,height));                                        //set the size of the button.
                break;
            case 5:
                button.setPreferredSize(new Dimension(width,height));                               //in case of the buttons not behaving correctly
                button.setMinimumSize(new Dimension(width,height));                                 //this limits all of the option to the same size "Brute Force".
                button.setMaximumSize(new Dimension(width,height));
                break;
        }

    }

    public void setButtonBackground(JButton button, int R, int G, int B, int Opacity){
        button.setBackground(new Color(R,G,B,Opacity));                                             //change the buttons color.
        button.setFocusPainted(false);                                                              //removes the weird black border around the button text.
    }

    public void packWindow(){
        generalWindow.pack();                                                                       //set the correct size for painting the window.
    }

    public void erase(){                                                                             //generic function to erase and repaint all content of general window.
       this.generalWindow.getContentPane().removeAll();
       this.generalWindow.repaint();
   }

   public GridBagConstraints left(){
       GridBagConstraints left = new GridBagConstraints();                                          //GridBad custom alignments.
       left.anchor = GridBagConstraints.EAST;                                                       //put the constraints in the east side of the grid bag layout
       return left;
   }

   public GridBagConstraints right(){
       GridBagConstraints right = new GridBagConstraints();

       right.weightx = 2.0;                                                                        //Change grid distribution
       right.fill = GridBagConstraints.HORIZONTAL;                                                 //Fill the display area horizontally
       right.gridwidth = GridBagConstraints.REMAINDER;                                             //This component is the last one in the row.

       return right;
   }

   public GridBagConstraints center(){
       GridBagConstraints center = new GridBagConstraints();

       center.weightx = 2.0;
       center.gridwidth = GridBagConstraints.REMAINDER;

       return center;
   }

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        super.run();
        generateLoginWindow();                                  //start the window

        generalWindow.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override
            public void windowClosing(WindowEvent e) {
                client.getOutput().println(-1+": ");

                try{
                    client.getOutput().close();
                    client.getInput().close();
                    client.getSocket().close();
                }catch (Exception ex){
                    ex.printStackTrace();
                }


                super.windowClosing(e);
            }
        });
    }



}

