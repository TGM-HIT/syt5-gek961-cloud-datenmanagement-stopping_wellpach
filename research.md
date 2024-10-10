# User Management Webservice

Dieses Projekt implementiert einen einfachen Webservice zur Benutzerverwaltung. Die Webanwendung bietet Endpunkte zur Registrierung, Anmeldung und Verifizierung von Benutzern. Die Authentifizierung basiert auf JWT-Token und die Daten werden in einer PostgreSQL-Datenbank gespeichert.


## Fragestellungen

### Grundlegenden Elemente für eine REST-Schnittstelle

- **Ressourcen**: Diese sind die zentralen Objekte, auf die die Schnittstelle zugreift und die sie verwaltet. Ressourcen werden durch eindeutige URIs identifiziert.
- **HTTP-Methoden**: REST verwendet standardisierte HTTP-Methoden, um Aktionen auf Ressourcen auszuführen, wie z. B. GET, POST, PUT, DELETE.
- **Repräsentationen**: Ressourcen werden in bestimmten Formaten wie JSON oder XML repräsentiert, die bei Anfragen und Antworten zwischen Server und Client übertragen werden.
- **Stateless Interaktionen**: Jede Anfrage vom Client an den Server muss alle notwendigen Informationen enthalten, um die Anfrage zu verarbeiten, da der Server keinen Zustand zwischen den Anfragen speichert.
- **Statuscodes**: Die Schnittstelle muss HTTP-Statuscodes nutzen, um den Status der Anfrage zu kommunizieren (z. B. 200 OK, 404 Not Found, 401 Unauthorized).

###  Verbindung zu den HTTP-Befehlen

HTTP-Methoden spielen eine zentrale Rolle in der Interaktion mit den Ressourcen einer REST-Schnittstelle. Jede Methode hat eine bestimmte Funktion:

- **GET**: Ruft Daten einer Ressource ab, ohne diese zu verändern. Es wird für Leseoperationen verwendet.
- **POST**: Dient zum Erstellen einer neuen Ressource auf dem Server. Es wird verwendet, um Daten zu senden und neue Objekte zu erstellen.
- **PUT**: Aktualisiert eine vorhandene Ressource oder erstellt sie, falls sie noch nicht existiert.
- **DELETE**: Löscht eine Ressource vom Server.
- **PATCH**: Aktualisiert teilweise eine Ressource.

Diese Befehle stellen sicher, dass CRUD-Operationen (Create, Read, Update, Delete) auf die Ressourcen anwendbar sind.

### Welche Datenbasis für einen solchen Use-Case?

Für eine REST-API zur Benutzerverwaltung eignet sich eine relationale Datenbank wie **PostgreSQL** oder **MySQL**, da sie Transaktionssicherheit, Datenintegrität und einfache Verwaltung von relationalen Datenmodellen bietet. In Entwicklungs- und Testumgebungen kann eine In-Memory-Datenbank wie **H2** verwendet werden.

Bei einer produktionsreifen Anwendung sollte jedoch eine skalierbare und robuste Datenbank wie PostgreSQL verwendet werden, um eine sichere und performante Datenhaltung zu gewährleisten.

### Erfordernisse bezüglich der Datenbasis?

- **Datenintegrität**: Sicherstellen, dass die gespeicherten Daten korrekt und konsistent sind. Dies schließt die Nutzung von Constraints und Transaktionen ein.
- **Sicherheit**: Passwörter sollten nie im Klartext gespeichert werden. Es sollte ein sicheres Hashing-Verfahren wie **BCrypt** verwendet werden.
- **Skalierbarkeit**: Die Datenbank sollte skalierbar sein, um bei zunehmender Benutzerzahl performant zu bleiben. Dies kann durch horizontale oder vertikale Skalierung erreicht werden.
- **Backups und Wiederherstellung**: Regelmäßige Backups und eine robuste Wiederherstellungsstrategie sind notwendig, um Datenverlust vorzubeugen.

### Eckpunkte bei einer öffentlichen Bereitstellung (Production)

- **Sicherheit**: HTTPS sollte für die Verschlüsselung der Datenübertragung verwendet werden. Außerdem sollten API-Schlüssel oder JWT-Authentifizierung zum Schutz der Schnittstelle vor unbefugtem Zugriff genutzt werden.
- **Skalierbarkeit**: Die Anwendung sollte so gestaltet werden, dass sie bei steigender Last durch horizontale Skalierung (z. B. Load-Balancing) oder vertikale Skalierung (mehr Ressourcen) erweitert werden kann.
- **Monitoring und Logging**: Die Server-Performance, API-Anfragen und Fehler sollten kontinuierlich überwacht werden, um Probleme schnell zu erkennen und zu beheben.
- **Rate-Limiting**: Um Missbrauch der API zu verhindern, sollte eine Begrenzung der Anzahl von Anfragen pro Benutzer/IP implementiert werden.
- **Fehlertoleranz**: Es sollten Mechanismen implementiert werden, die gewährleisten, dass die Anwendung auch bei Ausfällen einzelner Komponenten oder hoher Last funktionsfähig bleibt.
- **Datenbank-Optimierung**: Abfragen sollten optimiert sein (z. B. durch Indexierung), um eine schnelle und effiziente Datenverarbeitung zu gewährleisten.

## Technologien

- **Java 17**
- **Maven**: Build-Management-Tool
- **Spring Boot**: Framework für schnelle und effiziente Entwicklung
    - **Spring Boot Security**: Zur Implementierung der Authentifizierungs- und Autorisierungslogik
    - **Spring Boot Web**: Zur Bereitstellung der REST-API
    - **Spring Boot JWT Token**: Zur JWT-Token-Generierung und -Verifizierung
- **PostgreSQL**: Datenbank für die Benutzerdaten
- **Docker**: Containerisierung der Datenbank

## Endpunkte

### Registrierung
- **URL**: `/auth/admin/register`
- **Methode**: `POST`
- **Beschreibung**: Registriert einen neuen Benutzer. Diese Aktion kann nur von einem Administrator ausgeführt werden.
- **Daten**:
  ```json
  {
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "roles": ["ADMIN", "MODERATOR"]
  }
  ```

### Anmeldung
- **URL**: `/auth/signin`
- **Methode**: `POST`
- **Beschreibung**: Authentifiziert einen Benutzer und liefert einen JWT-Token zurück.
- **Daten**:
  ```json
  {
    "email": "john.doe@example.com",
    "password": "password123"
  }
  ```

### Verifizierung
- **URL**: `/auth/verify`
- **Methode**: `POST`
- **Beschreibung**: Überprüft die Gültigkeit eines JWT-Tokens und gibt die Benutzerrolle zurück.
- **Header**: `Authorization: Bearer <JWT_TOKEN>`

## Datenbank

Die Benutzerdaten werden in einer PostgreSQL-Datenbank gespeichert. Beim Starten der Anwendung wird eine Datenbanktabelle `Users` automatisch erstellt.

### Datenbankkonfiguration

Die Datenbank wird in einem Docker-Container bereitgestellt. Die Konfiguration erfolgt über das `docker-compose.yml`-File.

## Docker

### Voraussetzungen

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/)

### Docker Compose Setup

Um die PostgreSQL-Datenbank in einem Docker-Container zu starten, führe den folgenden Befehl im Wurzelverzeichnis des Projekts aus:

```bash
docker-compose up -d
```

Die Datenbank ist unter `localhost:5432` mit folgenden Standard-Credentials erreichbar:

- **Datenbankname**: `myappdb`
- **Benutzer**: `postgres`
- **Passwort**: `postgrespassword`

### Initial SQL-Skript

Beim Starten des Containers wird automatisch ein SQL-Skript ausgeführt, das die Tabelle `Users` erstellt.

```sql
CREATE TABLE IF NOT EXISTS Users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    roles VARCHAR(100) NOT NULL
);
```

## Installation und Ausführung

### Voraussetzungen

- Java 17
- Maven
- Docker

### Schritte

1. Klone das Repository:
   ```bash
   git clone https://github.com/dein-username/dein-repository.git
   cd dein-repository
   ```

2. Starte die PostgreSQL-Datenbank im Docker-Container:
   ```bash
   docker-compose up -d
   ```

3. Erstelle das Projekt und führe es aus:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Das Backend ist jetzt unter `http://localhost:8080` erreichbar.

## Tests

Um die Funktionalität zu testen, können Curl-Befehle verwendet werden. Hier einige Beispiele:

### Registrierung eines Benutzers

```bash
curl -X POST http://localhost:8080/auth/admin/register \
   -H "Content-Type: application/json" \
   -d '{"name":"John Doe", "email":"john.doe@example.com", "password":"password123", "roles":["ADMIN"]}'
```

### Anmeldung

```bash
curl -X POST http://localhost:8080/auth/signin \
   -H "Content-Type: application/json" \
   -d '{"email":"john.doe@example.com", "password":"password123"}'
```

### Token-Verifizierung

```bash
curl -X POST http://localhost:8080/auth/verify \
   -H "Authorization: Bearer <JWT_TOKEN>"
```

## Erklärung der Abschnitte:

- **Technologien**: Auflistung der verwendeten Technologien.
- **Endpunkte**: Beschreibt die wichtigsten API-Endpunkte mit Beispielen.
- **Datenbank**: Konfigurationsinformationen zur PostgreSQL-Datenbank.
- **Docker**: Setup-Anweisungen für Docker und Docker Compose.
- **Installation und Ausführung**: Schritt-für-Schritt-Anweisungen zur Einrichtung und Ausführung der Anwendung.
- **Tests**: Beispiel-Curl-Befehle zur einfachen Überprüfung der Funktionalität.

