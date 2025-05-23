# 🃏 DeckBuilder - Application Web de dépôt de decks

> Une application Java/Spring Boot MVC pour créer, gérer et partager des decks de cartes de jeu. Intégration d'une API externe pour récupérer les cartes (ex : pokemon TGC, Marvel Snap etc.).

---

## 🔧 Stack technique

- **JDK 24**
- **Spring Boot 3**
- **Spring MVC**
- **Spring Security**
- **Thymeleaf** pour le front-end
- **Spring Data JPA + PostgreSQL**
- **Daisy** pour une UI propre
- **API externe** pour les cartes (ex : [pokemon TGC](https://pokemontcg.io/))

---

## 🚀 Fonctionnalités

### Partie publique
- Voir les decks déposés par tous les utilisateurs
- Affichage propre avec image des cartes
- Possibilité faire un export de deck (optionnel)

### Partie privée (authentification requise)
- Connexion/inscription via formulaire sécurisé
- Déposer un nouveau deck (nom + cartes)
- Modifier ou supprimer ses propres decks
- Intégration d’une API pour chercher et valider les cartes

---

## 🔐 Sécurité

- Gestion des utilisateurs avec Spring Security
- Accès privé via login
- Chiffrement des mots de passe avec BCrypt
- Protection CSRF activée
- URLs privées sécurisées par rôle `USER`

---

## 📁 Structure du projet

```text
com.monsite.deckbuilder
├── controller       # Contrôleurs MVC (Deck, Auth)
├── model            # Entités JPA : Deck, Card, User
├── repository       # Repositories Spring Data
├── service          # Logique métier (DeckService, CardService)
├── security         # Configuration Spring Security
└── resources/
    ├── templates/   # Pages Thymeleaf : login.html, deck-form.html, etc.
    ├── static/      # CSS / JS / Images
    └── application.yml
