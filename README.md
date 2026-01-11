# 🏦 Application Android - Gestion de Comptes Bancaires

[![Kotlin](https://img.shields.io/badge/Kotlin-1.8.0-blue.svg)](https://kotlinlang.org/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://developer.android.com/about/versions/nougat)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

Application Android complète pour la gestion de comptes bancaires, développée en Kotlin avec une architecture moderne et des bonnes pratiques de développement. L'application communique avec une API REST et prend en charge les formats JSON et XML.

## 🌟 Fonctionnalités

### Fonctionnalités principales

- **Gestion des comptes** : CRUD complet (Créer, Lire, Mettre à jour, Supprimer)
- **Support multi-format** : Affichage en JSON ou XML selon la préférence
- **Interface utilisateur intuitive** : Conçue avec Material Design 3
- **Gestion hors ligne** : Cache des données pour une meilleure expérience utilisateur
- **Sécurité** : Gestion sécurisée des données sensibles

### Détails techniques

- **Architecture** : MVVM (Model-View-ViewModel)
- **Appels réseau** : Retrofit avec convertisseurs GSON et SimpleXML
- **Base de données locale** : Room pour le cache des données
- **Injection de dépendances** : Hilt pour une gestion propre des dépendances
- **Tests unitaires** : JUnit, Mockito et Espresso

## 🛠️ Prérequis techniques

- Android Studio Flamingo (2022.2.1) ou version ultérieure
- SDK Android 24 (Nougat) ou supérieur
- JDK 17
- Accès à un serveur REST fonctionnel
- Accès à Internet pour les dépendances

## 🚀 Installation



2. **Configurer l'environnement** :
   - Ouvrir le projet dans Android Studio
   - Synchroniser le projet avec les fichiers Gradle
   - Configurer le SDK Android dans `File > Project Structure`

3. **Configurer l'API** :
   - Modifier `local.properties` pour définir l'URL de base de l'API :
     ```properties
     API_BASE_URL="http://10.0.2.2:8082/"  # Pour émulateur
     # API_BASE_URL="http://votre-ip:8082/"  # Pour appareil physique
     ```

### Structure du projet

```
app/
├── src/
│   ├── main/
│   │   ├── java/ma/projet/restclient/
│   │   │   ├── data/
│   │   │   │   ├── local/             # Couche d'accès aux données locales
│   │   │   │   ├── remote/            # Couche d'accès aux données distantes
│   │   │   │   └── repository/        # Implémentation des repositories
│   │   │   ├── di/                    # Configuration de l'injection de dépendances
│   │   │   ├── domain/                # Modèles de domaine et cas d'utilisation
│   │   │   ├── ui/                    # Couche d'interface utilisateur
│   │   │   │   ├── main/              # Écran principal
│   │   │   │   └── viewmodel/         # ViewModels
│   │   │   └── utils/                 # Utilitaires et extensions
│   │   └── res/                       # Ressources Android
│   └── test/                          # Tests unitaires
└── build.gradle                       # Configuration du module
```

## 🔧 Configuration avancée

### Variables d'environnement

Créez un fichier `secrets.properties` à la racine du projet :

```properties
# Configuration de l'API
API_BASE_URL="http://10.0.2.2:8082/"
API_TIMEOUT=30

# Configuration du cache
CACHE_DURATION=3600  # en secondes
```

### Configuration de Retrofit

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }
}
```

## 📚 Documentation de l'API

### Endpoints

| Méthode | Endpoint | Description | Format |
|---------|----------|-------------|--------|
| `GET`   | `/banque/comptes` | Liste des comptes | JSON/XML |
| `GET`   | `/banque/comptes/{id}` | Détails d'un compte | JSON/XML |
| `POST`  | `/banque/comptes` | Créer un compte | JSON |
| `PUT`   | `/banque/comptes/{id}` | Mettre à jour un compte | JSON |
| `DELETE`| `/banque/comptes/{id}` | Supprimer un compte | - |

### Modèle de données

**Compte**
```json
{
  "id": 1,
  "solde": 1500.75,
  "type": "COURANT",
  "dateCreation": "2025-01-11"
}
```

## 🧪 Tests

### Exécuter les tests

```bash
# Tous les tests
./gradlew test

# Tests unitaires uniquement
./gradlew testDebugUnitTest

# Tests d'interface utilisateur
./gradlew connectedAndroidTest
```

### Couverture de code

Générer un rapport de couverture :
```bash
./gradlew jacocoTestReport
```

## 📦 Dépendances

### Principales dépendances

- **AndroidX** : `androidx.core:core-ktx:1.10.0`
- **Material Design** : `com.google.android.material:material:1.9.0`
- **Retrofit** : `com.squareup.retrofit2:retrofit:2.9.0`
- **Room** : `androidx.room:room-runtime:2.5.0`
- **Hilt** : `com.google.dagger:hilt-android:2.45`
- **Coroutines** : `org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4`
- **Navigation** : `androidx.navigation:navigation-fragment-ktx:2.5.3`

### Dépendances de développement

- **LeakCanary** : `com.squareup.leakcanary:leakcanary-android:2.10`
- **Chucker** : `com.github.chuckerteam.chucker:library:3.5.2`
- **Timber** : `com.jakewharton.timber:timber:5.0.1`

## 📝 Licence

```
MIT License

Copyright (c) 2025 Votre Nom

Permission is hereby granted...
```

## 🤝 Contribution

Les contributions sont les bienvenues ! Voici comment contribuer :

1. Forkez le projet
2. Créez votre branche (`git checkout -b feature/AmazingFeature`)
3. Committez vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Poussez vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📞 Support

Pour toute question ou problème, veuillez ouvrir une issue sur GitHub.


## 🔄 Workflow CI/CD

Le projet utilise GitHub Actions pour :
- Exécution des tests à chaque push
- Vérification du format de code avec ktlint
- Déploiement automatique sur Firebase App Distribution

## 📚 Ressources

- [Documentation Android](https://developer.android.com/docs)
- [Guide Retrofit](https://square.github.io/retrofit/)
- [Documentation Room](https://developer.android.com/training/data-storage/room)
- [Guide Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

---

<div align="center">
  Fait avec ❤️ par Votre Nom - 2025
</div>

