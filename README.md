```
██╗   ██╗ ██████╗ ██╗     ██╗  ██╗ ██████╗ ██╗   ██╗
██║   ██║██╔═══██╗██║     ██║ ██╔╝██╔═══██╗██║   ██║
██║   ██║██║   ██║██║     █████╔╝ ██║   ██║██║   ██║
╚██╗ ██╔╝██║   ██║██║     ██╔═██╗ ██║   ██║╚██╗ ██╔╝
 ╚████╔╝ ╚██████╔╝███████╗██║  ██╗╚██████╔╝ ╚████╔╝ 
  ╚═══╝   ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝   ╚═══╝  
     Encrypted Runtime Loader & Packer
              Project: VOLKOV
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

#### **1. Crypter un client**
```bash
java -jar volkov-packer.jar client.jar client.enc
```

#### **2. Intégration dans un projet**
```java
volkov.ClientLauncher.launchDecryptedClient("--arg1=value", "--arg2=value");
```

---

### **Avantages**

- Exécution **100% en mémoire** (pas de trace sur disque)
- Résistant aux outils comme **JD-GUI, Bytecode Viewer etc.**
- Lib légère, modulaire, facilement intégrable dans tout projet Java sécurisé

---

### **Note officieuse**

Volkov n’est pas un logiciel.  
C’est un homme quelque part à l’est, dans un bureau sans fenêtre,  
avec un clavier soviétique et un œil sur chaque byte que tu charges.  

> Si tu vois “client.enc”, c’est déjà trop tard.

---

**Codé pour Mineria**  
Par Clément Baggio — **alias CipheR_**
