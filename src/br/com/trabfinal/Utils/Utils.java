package br.com.trabfinal.Utils;

import com.sun.istack.internal.NotNull;

import javax.swing.*;

public class Utils {

    public static void message(String msg,String title){
        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.PLAIN_MESSAGE);
    }

    public static void message(String msg,String title,@NotNull boolean error){
        JOptionPane.showMessageDialog(null,msg,title,JOptionPane.ERROR_MESSAGE);
    }
}
