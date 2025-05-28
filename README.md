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

## Introduction

**Volkov** est une petite bibliothèque Java conçue pour **protéger et exécuter des fichiers `.jar` de manière sécurisée**. Elle chiffre vos `.jar` avec AES et les exécute directement depuis la mémoire, sans jamais les écrire sur disque.

Le but principal de Volkov est de **réduire la surface d'attaque** et d'empêcher la rétro-ingénierie de vos fichiers `.jar`.

---

## Fonctionnalités

### **Pourquoi utiliser Volkov ?**
- **Exécution en mémoire uniquement** : Pas de trace sur disque.
- **Protection contre la rétro-ingénierie** : Résistant aux outils comme JD-GUI ou Bytecode Viewer.
- **Chiffrement AES** : Utilisation de clés AES (16 ou 32 caractères).
- **Simple à intégrer** : Une interface intuitive et légère.

---

## Utilisation via CLI

Volkov inclut un outil CLI simple pour chiffrer ou déchiffrer vos fichiers `.jar`.

### **Commandes**
#### Chiffrer (`pack`)
```bash
java -jar volkov.jar pack --input client.jar --output client.enc --key SuperSecretKey42
```

#### Déchiffrer (`unpack`)
```bash
java -jar volkov.jar unpack --input client.enc --output client.jar --key SuperSecretKey42
```

### **Options**
| Option         | Description                                  |
|----------------|----------------------------------------------|
| `--input`      | Chemin du fichier source à traiter.          |
| `--output`     | Chemin de sortie pour le fichier généré.     |
| `--key`        | Clé AES (16 ou 32 caractères).               |

---

## Exemple d'intégration

Voici un exemple simple pour exécuter un `.jar` chiffré dans votre projet :

```java
import fr.mineria.volkov.VolkovLauncher;

public class Main {
    public static void main(String[] args) {
        try {
            new VolkovLauncher()
                .withKey("SuperSecretKey42")
                .withEncryptedPath("client.enc")
                .withMainClass("com.example.Main")
                .launch();
        } catch (Exception e) {
            System.out.println("[VOLKOV] >> Échec du lancement : " + e.getMessage());
        }
    }
}
```

---

## FAQ

### **Quelles sont les limites de Volkov ?**
- Volkov protège vos `.jar` contre les analyses statiques et la décompilation classique, mais aucune solution n'est 100% infaillible face à un attaquant très motivé.

### **Est-ce difficile à intégrer ?**
- Pas du tout. Volkov est conçu pour être simple et rapide à utiliser.

---

## Licence

Distribué sous licence MIT. Consultez le fichier `LICENSE` pour plus de détails.

---

**Développé** avec ❤️ par **CipheR_** pour **Mineria**