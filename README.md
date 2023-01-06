# **Coding Week PCD - Projet de Conception et de Développement 2022-2023**


# Informations générales
## Membres de l'équipe projet
- Quentin LENFANT *(Chef de Projet)* <<Quentin.Lenfant@telecomnancy.eu>>
- Guillaume RICARD <<Guillaume.Ricard@telecomnancy.eu>>
- Damien SIMON <<Damien.Simon@telecomnancy.eu>>
- Camille MOUSSU <<Camille.Noiray-Moussu@telecomnancy.eu>>

## Encadrant de l'équipe projet
- Martine GAUTIER <<martine.gautier@univ-lorraine.fr>>

## Description

Ce projet est réalisé dans le cadre de la semaine bloquée "Coding Week" du module PCD (Projet de Conception et de Développement) de deuxième année à TELECOM Nancy.

Il consiste en la création d'une application JavaFX de flashcard.

## Sujet complet
[Sujet complet](./gdp/sujet.pdf)

## Vidéo de démonstration
[Vidéo de démonstration]()


# Mode d'emploi
## Exécution
### Exécutable jar
L'application est exécutable à l'aide d'une archive jar dans le fichier `app/build/libs/`. Pour cela, taper les commandes suivantes dans un terminal depuis la racine du projet:
```sh
cd app/build/libs/
java -jar app-all.jar
```

### Gradle
La construction de l'application JavaFX s'est faite avec l'outil Gradle.
Ainsi, pour compiler et exécuter l'application, il suffit de taper la commande suivante dans un terminal depuis la racine du projet:
```sh
./gradlew run
```

## Sauvegarde des piles et JSON
Pour sauvegarder l'ensemble des piles qui ont été créées, il faut cliquer sur `Fichier > Sauvegarder et quitter`. Les piles sont alors exportées dans un fichier `decks.json` qui est créé s'il n'existe pas déjà.
Pour sauvegarder une pile en particulier, il faut cliquer sur la pile voulue en mode création puis sur `Exporter`.

ATTENTION : Ne pas modifier `decks.json` lorsque l'application est fermée.