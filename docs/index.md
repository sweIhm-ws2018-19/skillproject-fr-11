# Hypershop - die smarte Einkaufsliste
[![Build Status](https://travis-ci.org/sweIhm-ws2018-19/skillproject-fr-11.svg?branch=master)](https://travis-ci.org/sweIhm-ws2018-19/skillproject-fr-11)
[![Sonarqube](https://sonarcloud.io/api/project_badges/measure?project=edu.hm.cs.seng%3Ahypershop&metric=alert_status)](https://sonarcloud.io/dashboard?id=edu.hm.cs.seng%3Ahypershop)

Dieser Alexa Skill Applikation ermöglicht, Zutaten für Ihre Gerichte einer Einkaufsliste hinzuzufügen.
Um Ihnen die Pflege zu erleichtern, können Sie ihre Lieblingsrezepte abspeichern und diese mit den benötigten Zutaten direkt in den Warenkorb legen.
Während des Einkaufs können Sie Ihre Einkaufsliste ausgeben lassen und die Zutaten abhaken.

Eine Übersicht der Funktionen finden Sie [hier](https://github.com/sweIhm-ws2018-19/skillproject-fr-11/wiki/Anwendungsfallbeschreibungen)

## Systemidee
Der Alexa Skill "Hypershop" stellt eine Einkaufsliste speziell für Lebensmittel bereit. In diese Einkaufsliste können Einträge mit Namen und Mengenangabe eingegeben werden. Außerdem können Rezepte hinzugefügt werden, die wiederum mehrere Zutaten mit Mengenangaben enthalten. Es ist möglich, Rezepte zu speicher und mit dem Rezeptnamen wieder auf die Einkaufsliste zu setzen. Bei einem Einkauf kann die Einkaufsliste mit den Einträgen (Name + Menge) ausgegben werden.

## Beispiel Interaktion
![image](https://user-images.githubusercontent.com/43847839/49254042-7692c700-f428-11e8-9b97-908eef5ab7da.png)

## Anwendungsfalldiagramm
![swe sprint 3](https://user-images.githubusercontent.com/43847839/50737379-c6537380-11c8-11e9-911f-cd98f8698407.png)

## Fachklassendiagramm
### Überblick
![class-overview](https://raw.githubusercontent.com/sweIhm-ws2018-19/skillproject-fr-11/master/05-Sprint3/class-overview.png)
### Detailiert
![classes](https://raw.githubusercontent.com/sweIhm-ws2018-19/skillproject-fr-11/master/05-Sprint3/class-diagram.png)
### ShoppingListService - Klasse
![shoppinglist](https://raw.githubusercontent.com/sweIhm-ws2018-19/skillproject-fr-11/master/05-Sprint3/class-shoppinglist.png)

## Sequenzdiagramm Add Ingredient to ShoppingList
![addingredient2shoppinglist-2-4](https://user-images.githubusercontent.com/43875688/49247707-33305c80-f418-11e8-83f5-902d41aaae08.png)

## Highlight Userstory- Rezept bearbeiten

- Über Befehl "bearbeite Rezept {Recipe}" wird der Name des Rezepts in den Session Context geladen
- Implementierung des Session Context über Stack (pushContext)
- Abhängig von Inhalt im Stack reagieren Handler unterschiedlich z.B.: AddIngredientIntentHandler fügt Zutaten der Einkaufsliste hinzu, wenn Stack leer, ansonsten dem Rezept hinzu das sich im Stack befindet
- Über Befehl "Zurück" wird der Stack geleert und damit die Ebene "Rezept bearbeiten" verlassen (popContext)

## Lessons Learned
- Verwendung von GitHub Issues und des Project Boards in Verbindung mit Scrum
- Relevanz von automatisierten Tests bei Pull Requests
- Relevanz von (Unit-)Tests für die Softwareentwicklung
- Vorteil von Mocks bei der Entwicklung von Tests
- Gruppenplanung und Teamarbeit
- Steuerung einer Applikation mit Spracheingabe
- Arbeiten mit Microservices
- Arbeiten mit maven 
