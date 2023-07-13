package controller;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import javax.swing.*;
import java.io.*;
import java.net.Socket;

public class ChatWallForm {
    public AnchorPane clientPane;
    public JFXTextArea txtClientAreamassage;
    public JFXTextField txtwriteMassage;
    public Label lblUserName;

    private String username;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    Socket socket;

    final File fileToSend[] = new File[1];
    JFileChooser jFileChooser = new JFileChooser();

    public ChatWallForm(){
        new Thread(()-> {
            try {
                socket = new Socket("localhost", 5000);
                this.username = LoginForm.getUserName();
                System.out.println(username);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                Client client = new Client(socket,username, dataOutputStream,dataInputStream);
             //   lblUserName.setText(LoginForm.getUserName());
                listenForMessages();
                client.sendUserName();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void btnSentMassageOnAction(ActionEvent event) {
         String clientMessage = txtwriteMassage.getText();

        try {
            dataOutputStream.writeUTF(username +" : "+clientMessage);
            dataOutputStream.flush();
            txtClientAreamassage.appendText("\n"+clientMessage);
            txtwriteMassage.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void listenForMessages() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String messageFromGroupChat;
                while (socket.isConnected()) {
                    try {
                        messageFromGroupChat = dataInputStream.readUTF();
                        txtClientAreamassage.appendText("\n"+messageFromGroupChat);
                    } catch (IOException e) {
                        closeAll(socket, dataInputStream, dataOutputStream);
                    }
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        try {
            if (dataInputStream != null) {
                dataInputStream.close();
            }
            if (dataOutputStream != null) {
                dataOutputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void cameraBtnOnAction(ActionEvent event) {
        jFileChooser.setDialogTitle("Choose File To Send");

        if(jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
            fileToSend[0] = jFileChooser.getSelectedFile();
            System.out.println(fileToSend[0].getName());
        }
    }

    public void btnSendImage(ActionEvent event) {
        if(fileToSend[0] == null){
            System.out.println("pleace choose image a first");
        }else{
            try {
                FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                String fileName = fileToSend[0].getName();
                byte[] fileNameBytes = fileName.getBytes();

                byte[] fileContentBytes = new byte[(int)fileToSend[0].length()];
                fileInputStream.read(fileContentBytes);

                dataOutputStream.writeInt(fileNameBytes.length);
                dataOutputStream.write(fileNameBytes);

                dataOutputStream.writeInt(fileContentBytes.length);
                dataOutputStream.write(fileContentBytes);
                dataOutputStream.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void txtOnAction(ActionEvent event) {
        btnSendImage(event);
    }
}
































//
//package controller;
//
//        import com.jfoenix.controls.JFXTextArea;
//        import com.jfoenix.controls.JFXTextField;
//        import javafx.event.ActionEvent;
//        import javafx.scene.control.Label;
//        import javafx.scene.layout.AnchorPane;
//
//        import java.io.DataInputStream;
//        import java.io.DataOutputStream;
//        import java.io.File;
//        import java.io.IOException;
//        import java.net.Socket;
//
//public class ChatWallForm {
//    public AnchorPane clientPane;
//    public JFXTextArea txtClientAreamassage;
//    public JFXTextField txtwriteMassage;
//    public Label lblUserName;
//
//    private String username;
//    private DataInputStream dataInputStream;
//    private DataOutputStream dataOutputStream;
//    Socket socket;
//
//    public ChatWallForm(){
//        new Thread(()-> {
//            try {
//                socket = new Socket("localhost", 5000);
//                this.username = LoginForm.getUserName();
//                System.out.println(username);
//                dataOutputStream = new DataOutputStream(socket.getOutputStream());
//                dataInputStream = new DataInputStream(socket.getInputStream());
//                Client client = new Client(socket,username, dataOutputStream,dataInputStream);
//                lblUserName.setText(LoginForm.getUserName());
//                listenForMessages();
//                client.sendUserName();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }).start();
//    }
//
//    public void btnSentMassageOnAction(ActionEvent event) {
//        String clientMessage = txtwriteMassage.getText();
//
//        try {
//            dataOutputStream.writeUTF(username +" : "+clientMessage);
//            dataOutputStream.flush();
//            txtClientAreamassage.appendText("\n"+clientMessage);
//            txtwriteMassage.clear();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public void listenForMessages() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String messageFromGroupChat;
//                while (socket.isConnected()) {
//                    try {
//                        messageFromGroupChat = dataInputStream.readUTF();
//                        txtClientAreamassage.appendText("\n"+messageFromGroupChat);
//                    } catch (IOException e) {
//                        closeAll(socket, dataInputStream, dataOutputStream);
//                    }
//                }
//            }
//        }).start();
//    }
//
//    public void closeAll(Socket socket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
//        try {
//            if (dataInputStream != null) {
//                dataInputStream.close();
//            }
//            if (dataOutputStream != null) {
//                dataOutputStream.close();
//            }
//            if (socket != null) {
//                socket.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void cameraBtnOnAction(ActionEvent event) {
//
//    }
//}
