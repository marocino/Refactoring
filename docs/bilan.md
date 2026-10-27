# Étape 8 : Preuve par l'extension, et bilan

## Ce qui a été ajouté

| Besoin | Nouvelles classes | Assemblage (`TransFret`) |
|---|---|---|
| Tarif fluvial : 0,40 €/km + 0,02 €/kg, minimum 300 € | `TarifFluvial` (hérite de `TarifAuKilometre`) | `FLUVIAL → new TarifFluvial()` |
| Délai : distance / 360 arrondi au supérieur + 1 jour | `DelaiFluvial` | `FLUVIAL → new DelaiFluvial()` |
| Réfrigéré interdit | — (la règle `MarchandiseReservee(REFRIGERE, {ROUTE, MER})` l'interdit déjà) | — |
| Pas d'urgence | — | `UrgenceInterdite({MER, FLUVIAL})` |
| Péniche `PEN-0001` de 2 500 t | `Peniche` | ajoutée à la flotte |
| Trajet Paris → Le Havre, 365 km | — | `.ajouter("Paris", "Le Havre", 365)` |
| Supplément hors gabarit ×1,10 (> 200 cm) | `SupplementHorsGabarit` (décorateur) | ajouté à la liste des suppléments |

## Combien de fichiers existants ont été modifiés ?

| Fichier existant | Pourquoi |
|---|---|
| `modele/ModeTransport.java` | une constante `FLUVIAL` (inévitable : c'est une nouvelle valeur du domaine) |
| `TransFret.java` | l'assemblage : c'est son rôle |
| `tarif/AssemblageTarif.java` | reçoit désormais la liste des suppléments au lieu de la coder en dur (modification faite **une fois** : le prochain supplément ne touchera plus que `TransFret`) |
| `NouveauSystemeAdapter.java` (test) | choisit la grille tarifaire décrite par le contrat (voir ci-dessous) |
| `AssemblageTarifTest.java` (test) | suit la nouvelle signature d'`AssemblageTarif` |

**Comparaison avec l'étape 1** (voir [diagnostic](diagnostic.md)) :

- `FLUVIAL` : **2** fichiers de production modifiés (`ModeTransport`, `TransFret`) contre **9** dans le code
  historique. Aucun service, aucun calculateur, aucun véhicule existant n'a été touché.
- Hors gabarit : `TransFret` + la mise en paramètre unique d'`AssemblageTarif`, contre une modification au cœur
  de `CalculateurPrix.calculerPrixHT` dans le code historique. Aucun autre décorateur n'a été modifié.

## Les 68 tests passent-ils toujours ?

- **`FLUVIAL` : oui**, sans rien faire de particulier : c'est uniquement du code nouveau.
- **Hors gabarit : non, pas avec la grille en vigueur, et c'est normal.** Le supplément change le prix de
  toutes les expéditions dont un colis dépasse 2 m, or plusieurs colis du jeu d'essai du contrat les dépassent
  (fûts de solvant 240 cm, produits laitiers 240 cm, bobines d'acier et conteneur 1 200 cm). Activé sur le contrat, il fait échouer
  **6 tests** :

  | Test | Attendu (ancienne grille) | Obtenu (nouvelle grille) |
  |---|---|---|
  | `route_entreprise_dangereux_urgent_remise_10_pourcent` | 2 509,65 € | 2 760,62 € |
  | `route_premium_refrigere_avec_assurance` | 727,20 € | 781,92 € |
  | `rail_entreprise_au_dela_de_5000_euros_remise_15_pourcent` | 12 216,20 € | 13 437,82 € |
  | `mer_premium_avec_assurance_proportionnelle` | 7 700,00 € | 7 970,00 € |
  | `annulation_en_cours_facturee_50_pourcent_du_ttc` | 1 505,79 € | 1 656,37 € |
  | `facture_texte_entreprise_annulee_en_cours` | frais 1 505,79 € | frais 1 656,37 € |

  Le contrat décrit le comportement d'**avant** l'extension et ne doit pas être modifié. La racine de composition
  propose donc les deux grilles :

  - `new TransFret()` : grille en vigueur, avec hors gabarit (utilisée par `Main`, testée par `ExtensionTest`) ;
  - `TransFret.avecGrilleHistorique()` : grille sans hors gabarit, utilisée par l'adaptateur des tests de contrat.

  Les deux ne diffèrent que par la liste des suppléments passée à `AssemblageTarif`. **Les 68 tests de contrat
  restent verts**, et la nouvelle règle est couverte par `SupplementHorsGabaritTest` et `ExtensionTest`.
  Dans un vrai projet, ce changement de prix serait validé avec le métier et le contrat mis à jour en conséquence.

## Tests des nouvelles classes

`TarifFluvialTest`, `DelaiFluvialTest`, `PenicheTest`, `SupplementHorsGabaritTest`, et `ExtensionTest` (de bout en
bout : prix, minimum, délai, véhicule, refus du réfrigéré et de l'urgence, hors gabarit avant urgence et remise).

`mvn install` : `transport-legacy` 68 tests, `transport-refactored` 148 tests (68 de contrat + 80 unitaires et
d'intégration), tous verts.

## Grille d'auto-évaluation

| Critère | ✔ | Où |
|---|---|---|
| 68 tests de contrat verts | ✔ | `NouveauSystemeContratTest` |
| Aucun nombre magique | ✔ | constantes nommées dans chaque classe (tarifs, délais, véhicules, SMS…) |
| Aucun `if` / `switch` sur un mode, un type de client ou un type de marchandise | ✔ | stratégies et règles paramétrées, sélection par `Map` |
| Tarifs et remises en Strategy, suppléments en Decorator, chacun avec son test unitaire | ✔ | packages `tarif`, `tarif.remise`, `tarif.supplement`, `delai` |
| Aucun `instanceof`, aucune `UnsupportedOperationException` | ✔ | `Vehicule.peutPrendreEnCharge`, `Vehicule.partir` |
| Aucun `static` modifiable, aucun singleton | ✔ | repositories, journaux, flotte, compteur : des instances créées par `TransFret` |
| L'adaptateur ne contient que de la traduction | ✔ | `NouveauSystemeAdapter` : conversion des types du contrat, `reinitialiser()` = nouvelle application |
| Ajouter `FLUVIAL` et « hors gabarit » n'a demandé que de nouvelles classes et l'assemblage | ✔ | voir le tableau ci-dessus (+ la constante d'enum) |

## Architecture finale (`transport-refactored`)

```
fr.efrei.transfret
├── Main, TransFret            démo et racine de composition
├── modele                     Client, Colis, Expedition, enums
├── client                     ServiceClient, ValidateurClient, ClientRepository
├── expedition                 ServiceExpedition, GenerateurFacture, GenerateurIdentifiant, TableDistances, ExpeditionRepository
├── acceptation                ValidateurExpedition + règles (Strategy)
├── tarif                      CalculateurPrix, ComposantPrix, DecorateurPrix, AssemblageTarif, tarifs par mode (Strategy)
│   ├── remise                 PolitiqueRemise (Strategy) + décorateur Remise
│   └── supplement             suppléments, urgence, assurance (Decorator)
├── delai                      EstimateurDelai + délais par mode (Strategy)
├── vehicule                   Vehicule et sous-classes, Ravitaillable, Refrigerant, Flotte, AffectationVehicule
├── notification               Notificateur, CanalNotification, EmailConsole, SmsConsole
├── persistance                repositories en mémoire
└── commun                     Montants, Dates
```
