import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;

import java.awt.BorderLayout;

import java.net.*;

class Server extends JFrame
{
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);
    

    public Server()
    {
        
        try {
            server = new ServerSocket(7771);
            System.out.println("server is waiting");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out = new PrintWriter(socket.getOutputStream());

            startReading();
            // startWriting();

            createGUI();

            handleEvents();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleEvents() {

        messageInput.addKeyListener(new KeyListener(){

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if(e.getKeyCode() == 10)
                {
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me :"+contentToSend+"\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });
    }

    private void createGUI() {

        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setIcon(new ImageIcon("chat.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);


        heading.setHorizontalAlignment(SwingConstants.CENTER);

        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        this.setLayout(new BorderLayout());

        this.add(heading,BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane,BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);


        this.setVisible(true);
    }

    public void startReading()
    {
        Runnable r1 = () -> {
            System.out.println("reader started");
            
           try{
                while(true)
                {
                
                    String msg = br.readLine();
                    if(msg.equals("exit"))
                    {
                        System.out.println("chat terminated");
                        socket.close();
                        break;
                    }

                    // System.out.println("Client : "+msg);
                    messageArea.append("Client : "+msg+"\n");

                } 
            }catch (Exception e) {
                    
                // e.printStackTrace();
                System.out.println("connection closed");
            }
                
            
        };
        new Thread(r1).start();
    }

    public void startWriting()
    {
        Runnable r2 = () -> {
            System.out.println("writer started..");
            try{
                while(!socket.isClosed())
                {
                

                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

                    String content = br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        System.out.println("chat terminated");
                        socket.close();
                        break;
                    }
                } 
                System.out.println("connection closed");   
                
           
        }catch(Exception e){
            e.printStackTrace();
        }

        };
        new Thread(r2).start();

    }

    public static void main(String[] args) {
       
        new Server();


    }

}