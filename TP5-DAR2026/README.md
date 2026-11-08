# TP5 : Invocation des méthodes sur des objets distants avec Java RMI

- **Matière** : Développement d'applications réparties
- **Faculté** : Faculté des Sciences de Sfax, Département d'informatique et des Communications
- **Auditoire** : LSI 3, AU 2026 – 2027
- **Enseignante des TPs** : Zeineb DAOUD
- **Étudiant** : Chaieb Chakib
- **IDE** : IntelliJ IDEA, JDK 8 ou supérieur

## Structure du dépôt

```
ChaiebChakib_LSI3_DevAppReparties/
├── RMIConversion_Server/    (Activité 5-1 : serveur)
├── RMIConversion_Client/    (Activité 5-1 : client + lib/conversionClient.jar)
├── RMIBanque_Server/        (Activité 5-2 : serveur + jndi.properties)
├── RMIBanque_Client/        (Activité 5-2 : client + jndi.properties + lib/banqueClient.jar)
└── README.md
```

---

## Activité 5-1 : Service de conversion de devise

### Étape 1 : Préparation de l'espace de travail

Création du répertoire `ChaiebChakib_LSI3_DevAppReparties`.

![Étape 1](screenshots/5-1_etape1.png)

### Étape 2 : Développement du serveur

Projet `RMIConversion_Server` :

- `rmiService.IConversion` : interface distante avec `double convertirMontant(double mt) throws RemoteException`.
- `rmiService.ConversionImpl` : implémentation (étend `UnicastRemoteObject`), taux utilisé : 1 EUR = 3.4 TND.
- `rmiServer.ConversionServer` : crée le registre RMI sur le port 1099, instancie `ConversionImpl` et la publie sous le nom `Conversion`.

![Projet serveur](screenshots/5-1_etape2.png)

### Étape 3 : Développement du client

- Export de l'interface `IConversion` seule (sans `ConversionImpl`) vers `conversionClient.jar`.
- Ajout du JAR au classpath de `RMIConversion_Client`.
- `rmiClient.ConversionClient` : recherche l'objet distant via `Naming.lookup` puis invoque `convertirMontant(500)`.

![Export du JAR](screenshots/5-1_etape3_jar.png)
![Ajout au classpath](screenshots/5-1_etape3_classpath.png)

### Étape 4 : Exécution

Ordre : `ConversionServer` puis `ConversionClient`.

![Exécution serveur](screenshots/5-1_etape4_serveur.png)
![Exécution client](screenshots/5-1_etape4_client.png)

Résultat attendu : `500 EUR = 1700.0 TND`

### Synthèse 5-1

Le cycle de vie d'un service RMI a été mis en œuvre : définition de l'interface distante, implémentation, enregistrement dans le RMI Registry puis invocation depuis le client via le stub. Le client ne dépend que de l'interface, l'implémentation reste côté serveur.

---

## Activité 5-2 : Service de banque avec RMI et JNDI

### Étape 1 : Préparation de l'espace de travail

![Étape 1](screenshots/5-2_etape1.png)

### Étape 2 : Développement du serveur

Projet `RMIBanque_Server` :

- `metier.Compte` : classe sérialisable (code `int`, solde `double`, dateCreation `Date`).
- `rmiService.IBanque` : `String creerCompte(Compte c)` et `String getInfoCompte(int code)`, chacune avec `RemoteException`.
- `rmiService.BanqueImpl` : stocke les comptes dans une `HashMap`, refuse la création si le code existe déjà.
- `rmiServer.BanqueServer` : serveur de l'application.

![Projet serveur](screenshots/5-2_etape2.png)

### Étape 3 : Développement du client

- Export de `IBanque` et `Compte` (sans `BanqueImpl`) vers `banqueClient.jar`.
- Ajout du JAR au classpath de `RMIBanque_Client`.
- `rmiClient.BanqueClient` : invoque les méthodes distantes.

![Export du JAR](screenshots/5-2_etape3_jar.png)
![Classpath](screenshots/5-2_etape3_classpath.png)

### Étape 4 : Exécution

![Serveur](screenshots/5-2_etape4_serveur.png)
![Client](screenshots/5-2_etape4_client.png)

### Étape 5 : Utilisation de JNDI

Fichier `jndi.properties` placé dans `src` côté serveur et côté client :

```
java.naming.factory.initial=com.sun.jndi.rmi.registry.RegistryContextFactory
java.naming.provider.url=rmi://localhost:1099
```

- Serveur : `new InitialContext()` puis `ctx.rebind("BanqueService", service)` à la place de `Naming.rebind`. Le registre est toujours démarré par `LocateRegistry.createRegistry(1099)`, car JNDI a besoin d'un registre actif pour y enregistrer l'objet.
- Client : `ctx.lookup("BanqueService")` à la place de `Naming.lookup`.

![JNDI serveur](screenshots/5-2_etape5_serveur.png)
![JNDI client](screenshots/5-2_etape5_client.png)

Résultat attendu côté client :

```
Compte 1 créé avec succès
Compte 2 créé avec succès
Erreur : le compte 1 existe déjà
Compte [code=1, solde=5000.0, dateCreation=...]
Compte [code=2, solde=1200.5, dateCreation=...]
Erreur : le compte 99 n'existe pas
```

### Synthèse 5-2

L'application fonctionne de la même façon avec JNDI qu'avec l'API RMI directe. JNDI ajoute une couche d'abstraction : le code serveur et client n'utilise plus d'URL RMI, la configuration est externalisée dans `jndi.properties`, ce qui permet de changer d'annuaire sans modifier le code. Les objets `Compte` sont passés par valeur (sérialisation), tandis que `BanqueImpl` est passé par référence (stub).

---

## Exécution rapide (IntelliJ)

1. Ouvrir chaque dossier comme projet IntelliJ (ou un module par dossier).
2. Pour chaque client : File → Project Structure → Modules → Dependencies → `+` → JARs → sélectionner le JAR de `lib/`.
3. Lancer d'abord `ConversionServer` / `BanqueServer`, puis `ConversionClient` / `BanqueClient`.

## Dépôt Git

```
git init
git add .
git commit -m "TP5 DAR : RMI conversion et banque avec JNDI"
git branch -M main
git remote add origin <url-du-repository>
git push -u origin main
```
