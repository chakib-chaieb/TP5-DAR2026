package rmiServer;

import rmiService.BanqueImpl;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.registry.LocateRegistry;

public class BanqueServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            BanqueImpl service = new BanqueImpl();
            Context ctx = new InitialContext();
            ctx.rebind("BanqueService", service);
            System.out.println("Serveur de banque démarré (JNDI) sur le port 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
