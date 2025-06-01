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

**Volkov** est une bibliothèque Java conçue pour **chiffrer et exécuter des fichiers `.jar` de manière sécurisée, entièrement en mémoire**.

Vous commencez par **chiffrer votre `.jar` à l’aide de la CLI fournie**, avec votre propre clé AES. Ensuite, vous utilisez **la bibliothèque Volkov dans votre launcher Java** pour **décrypter et exécuter le JAR directement depuis la mémoire** – sans jamais l’écrire sur le disque.

Seul le `.jar` chiffré est stocké ; le contenu original reste protégé contre la rétro-ingénierie classique.

> 💡 Pour une protection maximale, il est recommandé d’**obfusquer à la fois le launcher et le `.jar` chiffré** avec un obfuscateur avancé.

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
