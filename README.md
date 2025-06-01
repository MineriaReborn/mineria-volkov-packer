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

> 💡 Pour une protection maximale, il est recommandé d’**obfusquer à la fois le launcher et le `.jar` avant chiffrage** avec un obfuscateur avancé.

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
- En particulier, la clé de chiffrement utilisée pour protéger le `.jar` **doit être protégée par vos soins**. Puisqu’elle doit être présente côté client pour permettre le déchiffrement en mémoire, elle pourra toujours être récupérée par un attaquant déterminé.
- Il est donc important de mettre en place des mesures complémentaires pour limiter la récupération de cette clé, par exemple :  
  - Fragmenter la clé en plusieurs morceaux dans le code et la reconstruire dynamiquement au runtime.  
  - Chiffrer partiellement les fragments de clé avec des transformations simples (XOR, rotation de bits).  
  - Obfusquer fortement le launcher et la logique de déchiffrement avec des outils spécialisés.  
  - Implémenter des protections anti-debug et anti-tampering pour compliquer l’analyse dynamique.  
  - Lorsque c’est possible, déporter la gestion ou la fourniture de la clé vers un serveur sécurisé, afin que la clé ne soit pas stockée directement dans le client.

### **Est-ce difficile à intégrer ?**
- Pas du tout. Volkov est conçu pour être simple et rapide à utiliser.

### **Quels modes AES sont supportés en Java 22 ?**
| Mode     | Support natif dans JDK 22 | Padding recommandé       |
|----------|---------------------------|-------------------------|
| AES/ECB  | Oui                       | PKCS5Padding            |
| AES/CBC  | Oui                       | PKCS5Padding            |
| AES/CFB8 | Oui                       | NoPadding               |
| AES/CFB  | Oui                       | NoPadding               |
| AES/OFB  | Oui                       | NoPadding               |
| AES/CTR  | Oui                       | NoPadding               |
| AES/GCM  | Oui                       | NoPadding (authentifié) |

---

## Licence

Distribué sous licence MIT. Consultez le fichier `LICENSE` pour plus de détails.

---

**Développé** avec ❤️ par **CipheR_** pour **Mineria**
