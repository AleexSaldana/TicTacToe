package clientejugador;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author manue
 */
public class ClienteJugador implements Runnable{
    
    private Socket client;
    private DataOutputStream out;
    private DataInputStream in;
    private int port = 2626;
    private String host = "localhost"; 
    private String msj;
    private Vista frame;
    private JButton[][] buttons;
    private ActionListener actionList;
    private Image X;
    private Image O;
   
    private boolean turno;
    
    
    public ClienteJugador(Vista frame){
        try {
            this.frame = frame;
            
            X = ImageIO.read(getClass().getResource("X.png"));
            O = ImageIO.read(getClass().getResource("O.png"));
            
            client = new Socket(host,port);
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());
            
            buttons = this.frame.getBotones();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try{
            
            msj =  in.readUTF();
            String split[] = msj.split(";");
            frame.cambioTexto(split[0]);
            String XO = split[0].split(" ")[1];
            turno = Boolean.valueOf(split[1]);
            
            
            while(true){
                
                msj = in.readUTF();
                
                String[] mensajes = msj.split(";");
                int xo = Integer.parseInt(mensajes[0]);
                int f = Integer.parseInt(mensajes[1]);
                int c = Integer.parseInt(mensajes[2]);
                
            
                if(xo == 1)
                    buttons[f][c].setIcon(new ImageIcon(X));
                else
                   buttons[f][c].setIcon(new ImageIcon(O));
                
                buttons[f][c].removeActionListener(buttons[f][c].getActionListeners()[0]);
                turno = !turno;
                
                
                if(XO.equals(mensajes[3])){
                    JOptionPane.showMessageDialog(frame, "GANASTEEEEEE!");
                    new Vista().setVisible(true);
                    frame.dispose();
                }else  if("EMPATE".equals(mensajes[3])){
                    JOptionPane.showMessageDialog(frame, "EMPATE!");
                    new Vista().setVisible(true);
                    frame.dispose();
                }
                else  if(!"NADIE".equals(mensajes[3]) && !mensajes[3].equals(mensajes[0])){
                    JOptionPane.showMessageDialog(frame, "PERDISTE BUUUUU!");
                    new Vista().setVisible(true);
                    frame.dispose();
                }
                
                
              
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    public void enviarTurno(int f,int c){
        
        try {
            if(turno){
                String  datos = "";
                datos += f + ";";
                datos += c + ";";
                out.writeUTF(datos);
            }
            else{
                JOptionPane.showMessageDialog(frame, "Espera tu turno");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
}
