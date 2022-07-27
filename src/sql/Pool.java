package sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pool {
	//Creando pool
    private static class PoolSingletonHelper {
        private static final Pool INSTANCE = new Pool();
    }
    public static Pool getInstance() { return PoolSingletonHelper.INSTANCE; }
// Establecer propiedades de la pool
    private List<Connection> availableConnections = new ArrayList<>();
    private List<Connection> usedConnections = new ArrayList<>();
    private static int maxPoolSize = 50;
    private static int grow = 10;
    private static int initialPoolSize = 10;
    private String url = "jdbc:postgresql://localhost:5432/data";
    private String user = "postgres";
    private String pwd = "postgres";
    private Connection connection;

    //Creando conecciones iniciales
    public Pool() {
        for (int i = 0; i < initialPoolSize; i++) {
            availableConnections.add(createConnection(url, user, pwd));
        }
    }
  // Funcion para crear conexiones
    private Connection createConnection(String url, String user, String pwd) {
        try {
            connection = DriverManager.getConnection(url, user, pwd);
            System.out.println("Created connection " + (availableConnections.size() + 1));
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 // Funcion para cerrar conexiones
    public void shutdown() {
        for(int i = 0; i < availableConnections.size(); i++) {
            try {
                System.out.println("Closed connection " + (i + 1));
                availableConnections.get(i).close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Connection> getAvailableConnections() { return availableConnections; }
    public List<Connection> getUsedConnections() { return usedConnections; }
    public int getMaxPoolSize() { return maxPoolSize; }
    public int getGrow() { return grow; }
    public int getInitialPoolSize() { return initialPoolSize; }

    public void addAvailableConn(Connection connection) { availableConnections.add(connection); }
    public void addUsedConn(Connection connection) { usedConnections.add(connection); }
    public void removeAvailableConn(Connection connection) { availableConnections.remove(connection); }
    public void removeUsedConn(Connection connection) { usedConnections.remove(connection); }
    public void createConn() { availableConnections.add(createConnection(url, user, pwd)); }
}