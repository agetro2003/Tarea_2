package sql;

public class Main {

	// Funcion principal donde se simula la cantidad de conexiones deseada
	public static void main (String[] args) {
		PoolManager poolManager = new PoolManager();
		for (int i=0; i<100000; i++) {
		
Conexiones mythread = new Conexiones(poolManager);
mythread.start();

	}
		Pool.getInstance().shutdown();
	}
	
}