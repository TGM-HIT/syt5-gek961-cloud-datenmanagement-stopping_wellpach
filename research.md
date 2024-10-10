# User Management Webservice

Dieses Projekt implementiert einen einfachen Webservice zur Benutzerverwaltung. Die Webanwendung bietet Endpunkte zur Registrierung, Anmeldung und Verifizierung von Benutzern. Die Authentifizierung basiert auf JWT-Token und die Daten werden in einer PostgreSQL-Datenbank gespeichert.

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

