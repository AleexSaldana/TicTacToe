package servidor;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Servidor {

    //Inicializamos el puerto
    private final int puerto = 2027;
    //Numero maximo de conexiones (el tictactoe es un juego para 2)
    private final int noConexiones = 2;
    //Creamos una lista de sockets para guardar el socket de cada jugador
    private LinkedList<Socket> usuarios = new LinkedList<Socket>();
    //Variable para controlar el turno de cada jugador
    private Boolean turno = true;
    //Matriz donde se guardan los movimientos 
    private int G[][] = new int[3][3];
    //Numero de veces que se juega...para controlar las X y O
    private int turnos = 1;

    //Funcion para que el servidor empieze a recibir conexiones de clientes
    public void escuchar() {
        try {
            //Inicializamos la matriz del juego con -1
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    G[i][j] = -1;
                }
            }
            //Creacion del socket servidor
            ServerSocket servidor = new ServerSocket(puerto, noConexiones);
            //Ciclo infinito para esperar nuevos jugadores

            while (true) {
                //Cuando un jugador se conecte guardamos el socket en la lista
                System.out.println("Esperando jugadores....");
                Socket cliente = servidor.accept();
                System.out.println("Se ha conectado un nuevo usuario");
                //Agrega Socket
                usuarios.add(cliente);
                //Generación de hilos
                int xo = turnos % 2 == 0 ? 1 : 0;
                turnos++;
                //Instancia de un hilo
                Runnable run = new HiloServidor(cliente, usuarios, xo, G);
                Thread hilo = new Thread(run);
                hilo.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Correr Servidor
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.escuchar();
    }
}
