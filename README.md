<h1>DangerLab</h1>

Gebaseert op: [Portal 2](https://en.wikipedia.org/wiki/Portal_2).

**Doel Spel:** Alle puzzels oplossen met de tools die je hebt en uiteindelijk het lab te ontsnappen.

**Omgeving:** In een lab, met obstakels en een puzzle.

**Perspectief**: 2d, dus eigenlijk van de zijkant.

**Controls:**
* `WASD` Lopen, springen en mogelijk bukken.
* `Muis` Het richten van portals.
* `LinkerMuisKnop` Het plaatsen van rode portals.
* `RechterMuisKnop` Het plaatsen van blauwe portals.
* `T` Verwijderen van portals.
* `E` Het swappen tussen portals.
* `Q` Powerupp?
**Objecten:** muur, blokken die je kan oppaken, knoppen, knoppen waar je op kan staan, portals, lava, deuren.

**Begin:** Speler start in lab, wordt door text verteld een weg naar buiten te vinden.

**Eind:** speler is buiten, krijgt taart.

**Overige elementen:** Er staat welke portal equiped is en eventueel welke power ups aanwezig zijn.

**---------------------------------------------------------------------------------------------**

**Mogelijke powerups, actief voor heel level of tot doodgaan?

* Flight
* Armor
* Ghost
* Speed
* Teleport




**MoSCoW**

**Must Have:**
* Player movement
* Portals
* Werkend einde van level

**Should Have:**
* EdgeFollowingViewport
* Fancy portals

**Could Have:**
* PowerUpps
* MultiColor portals (2+ portals)

**Won't Have:**
* Level creator


**------------------------------------------------------------------------------------------------------**

## Tutorials & API
In de [wiki](https://github.com/HANICA/oopg/wiki) staan tutorials over alle belangrijke functionaliteiten uit de game engine.
De programmeurs API van deze game engine is hier beschikbaar: [API OOPG](http://hanica.github.io/oopg/).

## Processing 2 Reference
Verder is deze game engine gebaseerd op Processing 2, dus NIET versie 3 zoals in de course gebruikt. Van deze dependency merk je weinig, laat Eclipse of IntelliJ dit voor je oplossen via importeren op basis van Maven (de `pom.xml`). En gebruik dan vooral de OOPG functionaliteit voor je eigen game, niet direct processing.

Ga dus vooral NIET zelf Processing ook importeren zoals je tijdens de Course deed. Mocht je echter wel direct Processing functies willen gebruiken, om simpele zaken te tekenen (bv. `g.ellipse(...)` dan hier een link naar een (mirror van de oude) Processing reference:
https://cs.brynmawr.edu/Courses/cs110/fall2015dc/processing2.2.1Reference/

Voor grootste deel gelijk aan [Processing 3 reference](https://processing.org/reference/), die je kent maar er zijn enkele kleine verschillen in de API (verder verschil vooral performance).
