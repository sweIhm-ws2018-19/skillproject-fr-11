
# Hypershop - die smarte Einkaufsliste
Diese Alexa Skill Applikation erlaubt es Zutaten für Ihre Gerichte einer Einkaufsliste hinzuzufügen.
Um Ihnen die Pflege zu erleichtern, können Sie ihre Lieblingsrezepte abspeichern und diese mit den benötigten Zutaten direkt in den Warenkorb legen.
Während dem Einkauf können Sie sich Ihre Einkaufsliste ausgeben lassen und die Zutaten abhaken.

Eine Übersicht der Funktionen finden Sie hier:
https://github.com/sweIhm-ws2018-19/skillproject-fr-11/wiki/Anwendungsfallbeschreibungen

## Travis CI Setup
Die `pom.xml` Datei wurde so konfiguriert, dass das Testverzeichnis und das Quellcodeverzeichnis gefunden wird und alle Tests automatisch mit dem goal `test` ausgeführt werden können. Es wurde eine neue Datei `.travis.yml` angelegt, welche einen Befehl enthält um das gesamte Projekt zu testen und zu kompilieren. Dies wird durch das Maven goal `assembly` erreicht. Außerdem wurde die Integration für Sonarqube in der `.travis.yml` eingerichtet, wodurch der Code automatisch auf "Code Smells" und häufige Fehler überprüft wird.

### Continuous Delivery
Continuous Delivery konnte leider in der Kombination von TravisCI `dpl` in Kombination mit dem AWS Starter Accounts nicht umgesetzt werden. Ein AWS Starter Account erfordert eine Authentifizierung mit `aws_access_key_id`, `aws_secret_access_key` und `aws_session_token`, [was durch das TravisCI dpl Plugin nicht umgesetzt werden kann](https://github.com/travis-ci/dpl/issues/731). Die Alternative wäre die Verwendung der `aws-cli`, deren Automatisierung durch das 60-minütige Wechseln der AWS Starter Account Credentials schwer möglich wäre. Der Skill soll also bis auf weiteres manuell deployed werden.


