package sql;

import java.sql.Connection;
import java.sql.SQLException;

public class PoolManager {

    private Pool pool = Pool.getInstance();
// Añadir nuevas conexiones, siempre que aun no se haya superado el numero maximo de conexiones
    public synchronized void addConn() {
        if(pool.getAvailableConnections().size() < pool.getMaxPoolSize()) {
            for (int i = 0; i < pool.getGrow(); i ++) {
                pool.createConn();
            }
        }
        else {
            System.out.println("Maximas conexiones alcanzadas");
        }
    }

    // Modificar el estado de una conexion de disponible a en uso. 
    public synchronized Connection getConn() {
        Connection connection = null;
        do {
            if (!pool.getAvailableConnections().isEmpty()) {
                connection = pool.getAvailableConnections().get(0);
                pool.addUsedConn(pool.getAvailableConnections().get(0));
                pool.removeAvailableConn(pool.getAvailableConnections().get(0));
                return connection;
            } else {
                try {
                    System.out.println(Thread.currentThread() + " esperando conecciones disponibles.");
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(pool.getAvailableConnections().size() < pool.getMaxPoolSize()) {
                    addConn();
                }
            }
        }while(connection == null);
        return connection;
    }
// Modificar el estado de una conexion de en uso a disponible 
    public void returnConn(Connection connection) {
        pool.removeUsedConn(connection);
        pool.addAvailableConn(connection);
    }

    public int getAvailConn() { return pool.getAvailableConnections().size(); }
    public int getTotalConn() { return pool.getAvailableConnections().size() + pool.getUsedConnections().size(); }
}