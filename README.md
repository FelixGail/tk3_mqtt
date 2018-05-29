### Informationen zum Projekt und Maven

- Sourcecode sollte im Package `com.github.felixgail.tk3.mqtt` liegen
- Wird die Hauptklasse _(im Moment ExampleClass)_ ersetzt, muss **Zeile 14**
  der pom.xml angepasst werden `<main.class>com.github.felixgail.tk3.mqtt.ExampleClass</main.class>`
- Kompiliert wird das Projekt mit dem Befehl `mvn package`
- Die ausführbare .jar-Datei ist unter `target/tk3_mqtt-1.0-SNAPSHOT-jar-with-dependencies.jar` 
  zu finden
