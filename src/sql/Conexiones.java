package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Conexiones extends Thread {

    public Conexiones (PoolManager poolManager) {
        this.poolManager = poolManager;
    }

    private PoolManager poolManager;
    private Connection connection;
    private PreparedStatement std;
    
    public void run() {
        try {
        	long inicio = System.currentTimeMillis(); 
            connection = poolManager.getConn(); // Conectarse a la pool
            std = connection.prepareStatement("SELECT * FROM tabla");   // Query que se desea consultar
            std.executeQuery(); //Ejecucion de la query
            long fin = System.currentTimeMillis();
            long transcurrido = fin - inicio; // Tiempo en realiza la query
            System.out.println("\n La query fue realizada correctamente. \n"
            		+ "El tiempo de inicio = " + inicio + "ms \n"
            		+ "El tiempo del fin =  " + fin + "ms \n"
           			+ "El tiempo transcurrido ="+ transcurrido + "ms");
            poolManager.returnConn(connection); //Retornar la conexion para su reutilizacion
        }
        catch (Exception e){
            e.printStackTrace(); // Mensaje en caso de error
        }
    }
}