package rmiServer;

import rmiService.ConversionImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ConversionServer {

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            ConversionImpl service = new ConversionImpl();
            Naming.rebind("rmi://localhost:1099/Conversion", service);
            System.out.println("Serveur de conversion démarré sur le port 1099");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
