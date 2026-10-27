# Choix de conception

## Étape 5 : Strategy : qui choisit la stratégie ?

Trois familles de stratégies ont été isolées :

| Interface | Implémentations | Sélection |
|---|---|---|
| `TarifTransport` (tarif de base) | `TarifRoute`, `TarifRail`, `TarifMer`, `TarifAir` (base commune `TarifAuKilometre`) | `Map<ModeTransport, TarifTransport>` |
| `PolitiqueRemise` | `AucuneRemise`, `RemiseEntreprise`, `RemisePremium` | `Map<TypeClient, PolitiqueRemise>` |
| `DelaiTransport` (bonus) | `DelaiRoute`, `DelaiRail`, `DelaiMer`, `DelaiAir` | `Map<ModeTransport, DelaiTransport>` |
| `RegleAcceptation` | `MarchandiseInterdite`, `MarchandiseReservee`, `UrgenceInterdite` | `List<RegleAcceptation>` |

Les canaux de notification suivent le même principe : `Map<TypeClient, List<CanalNotification>>`.

### Option 1 : l'enum porte sa stratégie

```java
enum ModeTransport {
    ROUTE(new TarifRoute(), new DelaiRoute()), ...
}
```

- ✅ impossible d'oublier une stratégie pour un mode (le compilateur l'impose) ;
- ❌ l'enum du **modèle** dépend des classes de tarification, de délai… : il grossit à chaque nouvelle
  préoccupation et mélange les responsabilités (S) ;
- ❌ les stratégies deviennent des singletons de fait : impossible d'injecter un autre tarif (tests, tarif
  promotionnel, tarifs différents par agence) sans modifier l'enum (D).

### Option 2 : une `Map` injectée (retenue)

- ✅ le modèle ne connaît pas les règles de calcul ; chaque service ne reçoit que les stratégies qui le
  concernent ;
- ✅ la configuration (quel tarif pour quel mode) est faite à **un seul endroit**, la racine de composition
  `TransFret` ;
- ✅ les tests unitaires injectent des stratégies factices (`EstimateurDelaiTest`) ;
- ❌ un mode sans stratégie n'est détecté qu'à l'exécution : `CalculateurPrix` et `EstimateurDelai` lèvent
  alors une `IllegalArgumentException` explicite.

L'enum ne garde que des **données** intrinsèques (le libellé d'un type de marchandise, le fait qu'un type de
client soit identifié par un SIRET), pas de comportement.

## Étape 6 : Decorator : la chaîne de prix

```
Assurance( Remise( SupplementUrgence( SupplementRefrigere( SupplementDangereux( SupplementFragile( TarifParMode ) ) ) ) ) )
```

- `ComposantPrix` : interface commune (`double calculer(DemandeTarif)`) ;
- `TarifParMode` : composant de base, délègue à la stratégie `TarifTransport` du mode ;
- `DecorateurPrix` : décorateur abstrait (méthode `calculer` finale = calcul enveloppé puis `ajuster`) ;
- `SupplementMarchandise` factorise les suppléments « au moins un colis de tel type » : `SupplementFragile`,
  `SupplementDangereux`, `SupplementRefrigere` ;
- `SupplementUrgence`, `Assurance` ;
- `Remise` est un décorateur qui délègue à la stratégie `PolitiqueRemise` de l'étape 5 ;
- `AssemblageTarif` est le **seul** endroit qui connaît l'ordre des couches ; `CalculateurPrix` ne fait plus
  qu'arrondir le résultat de la chaîne.

Chaque décorateur décide lui-même s'il s'applique (`demande.contient(type)`, `demande.urgent()`…) : la chaîne est
construite une seule fois au démarrage et l'assemblage reste une simple liste ordonnée, sans `if`.
`AssemblageTarifTest` vérifie l'ordre (remise après urgence, assurance après remise).
