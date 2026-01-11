# 🏦 TP 10 : Client REST Android Retrofit (XML & JSON)




Créer une application Android permettant de consommer un service REST pour gérer des comptes bancaires avec des fonctionnalités CRUD (Create, Read, Update, Delete). L'application utilisera Retrofit pour les appels API et RecyclerView pour l'affichage des données.

## 🌟 Fonctionnalités
## Fonctionnalités
- Liste des comptes bancaires
- Ajout de nouveaux comptes
- Modification des comptes existants
- Suppression de comptes
- Support des formats JSON et XML
## Prérequis
- Android Studio
- SDK Android 24+
- Serveur REST accessible
## Installation
1. Cloner le dépôt
2. Ouvrir dans Android Studio
3. Configurer l'URL de l'API dans `RetrofitClient.java`
4. Exécuter l'application
## Structure du projet
- Client-REST-Android-Retrofit-XML-JSON/
├── app/
│   ├── src/main/
│   │   ├── java/ma/projet/restclient/
│   │   │   ├── MainActivity.java            # Activité principale
│   │   │   ├── adapter/
│   │   │   │   └── CompteAdapter.java       # Adaptateur pour RecyclerView
│   │   │   ├── api/                         # Interface Retrofit
│   │   │   ├── config/                      # Configuration
│   │   │   ├── entities/                    # Entités
│   │   │   │   ├── Compte.java
│   │   │   │   └── CompteList.java
│   │   │   └── repository/
│   │   │       └── CompteRepository.java    # Gestion des appels API
│   │   └── res/
│   │       ├── layout/
│   │       │   ├── activity_main.xml        # Layout principal
│   │       │   ├── item_compte.xml          # Item de liste
│   │       │   └── dialog_add_compte.xml    # Boîte de dialogue
│   │       └── xml/
│   │           └── network_security_config.xml
│   └── build.gradle
└── README.md
## Technologies utilisées
- Android
- Retrofit 2
- Gson
- XML
- Material Design

#

---



