# Étape 1 : Diagnostic du code historique

## Rôle et dépendances de chaque classe

| Classe | Ce qu'elle fait | Dépend de |
|---|---|---|
| `Main` | démo : crée la flotte, des clients, des expéditions, affiche les factures | `Flotte`, `ServiceClient`, `ServiceExpedition`, `Colis`, `Expedition` |
| `ServiceClient` | valide les données d'un client **et** l'enregistre **et** envoie l'email de bienvenue | `Client`, `Database` (singleton), `EmailService` (statique) |
| `ServiceExpedition` | valide une demande, choisit le véhicule, calcule prix et délai, génère l'identifiant, persiste, gère le cycle de vie, notifie par email et SMS | `Database`, `CalculateurPrix`, `AffectationVehicule`, `EmailService`, `SmsService`, `Utils`, `Vehicule`, `Colis`, `Client` |
| `CalculateurPrix` | calcule le prix HT : tarif par mode, suppléments, urgence, remise, assurance, arrondi | `Colis` |
| `AffectationVehicule` | parcourt la flotte et retourne le premier véhicule compatible | `Flotte` (statique), `Vehicule` et ses sous-classes (`instanceof`) |
| `Flotte` | liste **statique** et mutable des véhicules | `Camion`, `CamionFrigorifique`, `Train`, `Bateau`, `Avion` |
| `IVehicule` | interface « fourre-tout » de tous les véhicules | `Colis` |
| `Vehicule` | état commun (capacité, charge, disponibilité), chargement | `IVehicule`, `Colis` |
| `Camion`, `CamionFrigorifique`, `Train`, `Bateau`, `Avion` | véhicules concrets, la plupart des méthodes lèvent `UnsupportedOperationException` | `Vehicule` |
| `Client` | données d'un client, type en `String` | — |
| `Colis` | données d'un colis, type en `int`, libellé par `switch` | — |
| `Expedition` | données d'une expédition, statut et mode en `String`, **génère sa facture** | `Client`, `Colis`, `Vehicule`, `Utils` |
| `Database` | singleton, maps **statiques publiques**, faux SQL | `Client`, `Expedition` |
| `EmailService` | envoi d'email par méthode statique, journal statique, exception avalée | — |
| `SmsService` | envoi de SMS, journal statique, clé d'API en dur | — |
| `Utils` | arrondi, dates (`SimpleDateFormat` statique et mutable), email, table des distances codée en `if/else` | — |

## Défauts → emplacement → principe violé

| Défaut | Emplacement | Principe violé / odeur |
|---|---|---|
| `ServiceExpedition` fait tout : validation, affectation, prix, délai, identifiant, persistance, cycle de vie, notifications | `ServiceExpedition` | **S** : responsabilité unique (God class) |
| L'entité `Expedition` met en forme sa propre facture | `Expedition.genererFacture()` | **S** |
| `ServiceClient` valide **et** enregistre **et** notifie | `ServiceClient.creerClient` | **S** |
| `Utils` mélange dates, prix, emails, distances | `Utils` | **S** (classe « fourre-tout ») |
| Ajouter un mode impose de modifier les `if/else` sur `"ROUTE"`, `"RAIL"`… | `CalculateurPrix.calculerPrixHT`, `ServiceExpedition.calculerDelai`, `ServiceExpedition.creerExpedition`, `ServiceExpedition.demarrerExpedition`, `AffectationVehicule` | **O** : ouvert/fermé |
| Ajouter un type de client impose de modifier la remise, la validation, les notifications, la facture | `CalculateurPrix`, `ServiceClient`, `ServiceExpedition.notifier`, `Expedition.genererFacture` | **O** |
| Ajouter un supplément = un `if` de plus au milieu du calcul | `CalculateurPrix` | **O** |
| `CamionFrigorifique.charger` refuse ce que `Camion.charger` accepte : il n'est pas substituable | `CamionFrigorifique`, contournement dans `AffectationVehicule` (`continue` « sinon ça plante ») | **L** : substitution de Liskov |
| `Train.faireLePlein()` lève une exception là où `Vehicule` réussit, masquée par un `catch` vide | `Train`, `ServiceExpedition.demarrerExpedition` | **L** |
| `IVehicule` impose `rouler`, `naviguer`, `voler`, `activerRefrigeration` à tous : `UnsupportedOperationException` partout | `IVehicule`, `Camion`, `Train`, `Bateau`, `Avion` | **I** : ségrégation des interfaces |
| Singleton `Database.getInstance()`, maps statiques publiques | `Database`, `ServiceExpedition`, `ServiceClient` | **D** : inversion des dépendances, état global |
| `EmailService.envoyer` statique, `new SmsService()` dans `notifier` | `ServiceExpedition`, `ServiceClient` | **D** |
| `new CalculateurPrix()`, `new AffectationVehicule()` en dur dans le service | `ServiceExpedition` | **D** |
| `Flotte.vehicules` et `ServiceExpedition.compteur` statiques et mutables (l'adaptateur doit les vider) | `Flotte`, `ServiceExpedition`, `LegacyAdapter.reinitialiser` | **D** / état global |
| Nombres magiques : `1.2`, `0.10`, `0.8`, `150`, `500`, `167`, `4.5`, `1.15`, `1.30`, `5000`, `0.85`, `0.02`, `15`, `70`, `9`, `18`, `1440`, `720`, `3000`, `1.852`, `35000`, `160`… | `CalculateurPrix`, `ServiceExpedition`, `SmsService` | lisibilité (*Magic Number*) |
| Codes de type en `String` (mode, statut, type client) et en `int` (type de colis) | `Client`, `Colis`, `Expedition`, partout | *Primitive Obsession* |
| `getLibelleType()` est un `switch` sur un entier | `Colis` | *Switch Statement* |
| `Math.round(x * 100.0) / 100.0` dupliqué alors que `Utils.arrondir` existe | `CalculateurPrix`, `ServiceExpedition` | *Duplicated Code* |
| Volume calculé deux fois (`Colis.getVolume` et `Expedition.getVolumeTotal`) | `Colis`, `Expedition` | *Duplicated Code* |
| Exceptions avalées (`catch` vide, `catch (Exception)`) | `EmailService`, `ServiceExpedition.demarrerExpedition` | robustesse |
| Codes d'erreur (`null`, `-1`) au lieu d'`Optional` | `ServiceClient`, `ServiceExpedition`, `Utils.getDistance` | lisibilité |
| Secret en dur (`apiKey`), SQL concaténé | `SmsService`, `Database` | sécurité |

## Endroits à modifier pour ajouter le mode `FLUVIAL` (code historique)

1. `CalculateurPrix.calculerPrixHT` : nouveau `else if` pour le tarif et le minimum de 300 €.
2. `ServiceExpedition.calculerDelai` : nouveau `case`.
3. `ServiceExpedition.creerExpedition` : règle « pas d'urgence » (`mode.equals("MER")` → ajouter `FLUVIAL`) ; la règle
   « réfrigéré uniquement route ou mer » fonctionne par hasard.
4. `ServiceExpedition.demarrerExpedition` : nouvelle branche pour faire partir la péniche.
5. `AffectationVehicule.trouverVehicule` : nouvelle branche `instanceof Peniche`.
6. `IVehicule` : une méthode de plus (`naviguerSurFleuve` ?) → **tous** les véhicules à modifier (`Camion`, `Train`,
   `Bateau`, `Avion`) pour lever `UnsupportedOperationException`.
7. Nouvelle classe `Peniche`.
8. `Flotte.initialiser` : ajouter la péniche.
9. `Utils.getDistance` : nouveau `else if` pour Paris → Le Havre.
10. `LegacyAdapter` / tout code qui convertit le mode (chaînes de caractères non vérifiées).

→ **9 fichiers existants modifiés** + 1 nouvelle classe.

## Endroits à modifier pour ajouter le supplément « hors gabarit »

1. `CalculateurPrix.calculerPrixHT` : détecter un colis de plus de 200 cm dans la boucle, puis un `if` de plus entre
   les suppléments et l'urgence.
2. Éventuellement `Colis` pour une méthode « dimension maximale ».

→ **1 à 2 fichiers existants modifiés**, au cœur d'une méthode qui fait déjà tout.
