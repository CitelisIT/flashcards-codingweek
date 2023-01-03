# Fonctionnalités à implémenter

## Fonctionnalités obligatoires

- Mode création 
  - Création d'une carte (Question/Réponse en chaine de caractères)
  - Edition d'une carte
  - Suppresion d'une carte
  - Création d'une pile de carte (Nom + description)
  - Modification d'une pile (Changement de nom/description, ajout/suppression de cartes)
  - Exporter et importer une pile (Format choisi JSON)
- Mode apprentissage
  - Présenter une Présenter une carte face question pendant une durée choisie par l'utilisateur. A la fin du chrono, présenter la réponse et laisser l'utilisateur s'autoévaluer (Vrai / Faux) *Evalutation pouvant être amenée à évoluer dans le futur*
  - Définir un algorithme de répétition des cartes (initialement aléatoire mais *pouvant être amené à évoluer dans le futur*)
    - Idée : 3 stratégies : 
      - Combler ses lacunes (Cartes mal maitrisées)
      - Confirmer ses acquis (Cartes déjà bien maitrisées)
      - Mixte (Cartes mal maitrisées + cartes bien maitrisées)
  - Statistiques d'apprentissages (% de bonnes/mauvaises réponses, meilleure pile, meilleure carte, *etc.*)

## Fonctionnalités supplémentaires

- Mode création 
  - Contenu multimédia (Images, sons, vidéos) dans une carte