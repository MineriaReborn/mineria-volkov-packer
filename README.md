```
██╗   ██╗ ██████╗ ██╗     ██╗  ██╗ ██████╗ ██╗   ██╗
██║   ██║██╔═══██╗██║     ██║ ██╔╝██╔═══██╗██║   ██║
██║   ██║██║   ██║██║     █████╔╝ ██║   ██║██║   ██║
╚██╗ ██╔╝██║   ██║██║     ██╔═██╗ ██║   ██║╚██╗ ██╔╝
 ╚████╔╝ ╚██████╔╝███████╗██║  ██╗╚██████╔╝ ╚████╔╝ 
  ╚═══╝   ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝   ╚═══╝  
     Encrypted Runtime Loader & Packer
```

---

### **Présentation**

**Volkov** est une solution Java destinée à la **protection et à l’exécution discrète de fichiers `.jar`**.

Elle se compose de deux outils :
- un **packer** qui chiffre un fichier `.jar` à l’aide d’AES,
- une **lib de chargement mémoire** qui permet d'exécuter ce `.jar` directement depuis la RAM, sans l’écrire sur le disque.

Conçu pour les environnements sensibles, Volkov permet de **réduire la surface d’attaque**, d’empêcher l’analyse statique classique et d’éviter la décompilation facile des clients Java.

---

### **Composants**

#### **1. Volkov Packer**
> Un outil en ligne de commande qui :
- Prend un fichier `.jar`
- Le chiffre avec AES (clé configurable)
- Génère un fichier `client.enc` lisible uniquement par le loader

#### **2. Volkov Loader Lib**
> Une bibliothèque Java qui :
- Lit un fichier `.enc` chiffré
- Le déchiffre en mémoire
- Charge le contenu via un `ClassLoader` personnalisé
- Lance automatiquement la méthode `main()` de la classe cible

---

### **Exemple d’utilisation**

#### **1. Crypter/décrypter un client**
```bash
java -jar volkov.jar --pack --input=client.jar --output=client.enc --key=SecretKey
java -jar volkov.jar --unpack --input=client.enc --output=client.jar --key=SecretKey
```

#### **2. Intégration dans un projet**
```java
    public static void main(String[] args) {
        try {
            // Exemple de config
            String key = "SuperSecretKey42"; // 16 ou 32 caractères (AES)
            String pathToEnc = "client.enc"; // chemin vers le .enc
            String mainClass = "fr.project.client.Main"; // la classe contenant le main()
            String clientArgs = "--accessToken=1234abc --uuid=uuid-XYZ --username=Dev";

            // Lancement via Volkov
            VolkovLauncher launcher = new VolkovLauncher(key, pathToEnc, mainClass, clientArgs);
            launcher.launch();

        } catch (Exception e) {
            System.err.println("[VOLKOV] >> Échec du lancement : " + e.getMessage());
            e.printStackTrace();
        }
    }
```

---

### **Avantages**

- Exécution **100% en mémoire** (pas de trace sur disque)
- Résistant aux outils comme **JD-GUI, Bytecode Viewer etc.**
- Lib légère, modulaire, facilement intégrable dans tout projet Java sécurisé


---

**Codé pour Mineria**  
Par **CipheR_**
