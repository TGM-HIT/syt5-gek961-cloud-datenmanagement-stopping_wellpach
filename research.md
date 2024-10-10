# User Management Webservice

Dieses Projekt implementiert einen einfachen Webservice zur Benutzerverwaltung. Die Webanwendung bietet Endpunkte zur Registrierung, Anmeldung und Verifizierung von Benutzern. Die Authentifizierung basiert auf JWT-Token und die Daten werden in einer PostgreSQL-Datenbank gespeichert.

## Fragenstellungen

### Welche grundlegenden Elemente müssen bei einer REST-Schnittstelle zur Verfügung gestellt werden?

- **Ressourcen**: Ressourcen sind die zentralen Entitäten, auf die die Schnittstelle zugreift und die sie verwaltet. Jede Ressource wird durch eine eindeutige URI (Uniform Resource Identifier) identifiziert, die ihren Zugriff ermöglicht.

- **HTTP-Methoden**: REST nutzt standardisierte HTTP-Methoden, um Aktionen auf Ressourcen auszuführen. Die wichtigsten Methoden sind:
    - **GET**: Ruft Daten einer Ressource ab, ohne sie zu verändern. Diese Methode wird für Leseoperationen verwendet.
    - **POST**: Dient zum Erstellen einer neuen Ressource auf dem Server, indem Daten gesendet werden.
    - **PUT**: Aktualisiert eine bestehende Ressource oder erstellt sie, falls sie noch nicht vorhanden ist.
    - **DELETE**: Löscht eine Ressource vom Server.
    - **PATCH**: Nimmt partielle Änderungen an einer bestehenden Ressource vor.

- **Repräsentationen**: Ressourcen werden in spezifischen Formaten wie JSON oder XML dargestellt, die bei Anfragen und Antworten zwischen Server und Client ausgetauscht werden.

- **Stateless Interaktionen**: Jede Anfrage vom Client an den Server muss alle erforderlichen Informationen enthalten, um die Anfrage zu verarbeiten. Der Server speichert keinen Zustand zwischen den Anfragen.

- **Statuscodes**: Die Schnittstelle sollte HTTP-Statuscodes verwenden, um den Status der Anfrage zu kommunizieren, z. B. 200 OK, 404 Not Found oder 401 Unauthorized.

### Wie stehen diese mit den HTTP-Befehlen in Verbindung?

HTTP-Methoden sind essenziell für die Interaktion mit den Ressourcen einer REST-Schnittstelle. Jede Methode erfüllt eine spezifische Funktion, die sicherstellt, dass CRUD-Operationen (Create, Read, Update, Delete) auf die Ressourcen angewendet werden können.

### Welche Datenbasis bietet sich für einen solchen Use-Case an?

Für eine REST-API zur Benutzerverwaltung sind relationale Datenbanken wie **PostgreSQL** oder **MySQL** empfehlenswert. Diese bieten Transaktionssicherheit, Datenintegrität und eine einfache Verwaltung von relationalen Datenmodellen. In Entwicklungs- und Testumgebungen kann eine In-Memory-Datenbank wie **H2** eingesetzt werden. Für produktionsreife Anwendungen sollte jedoch eine skalierbare und robuste Datenbank wie PostgreSQL verwendet werden, um eine sichere und leistungsfähige Datenhaltung zu gewährleisten.

### Welche Erfordernisse bezüglich der Datenbasis sollten hier bedacht werden?

- **Datenintegrität**: Gewährleistung, dass die gespeicherten Daten korrekt und konsistent sind. Dies schließt die Anwendung von Constraints und Transaktionen ein.

- **Sicherheit**: Passwörter sollten niemals im Klartext gespeichert werden. Stattdessen sollte ein sicheres Hashing-Verfahren wie **BCrypt** verwendet werden.

- **Skalierbarkeit**: Die Datenbank sollte skalierbar sein, um auch bei steigender Benutzerzahl eine gute Performance zu gewährleisten. Dies kann durch horizontale oder vertikale Skalierung erreicht werden.

- **Backups und Wiederherstellung**: Regelmäßige Backups und eine durchdachte Wiederherstellungsstrategie sind notwendig, um Datenverluste zu vermeiden.

### Welche Eckpunkte müssen bei einer öffentlichen Bereitstellung (Production) von solchen Services beachtet werden?

- **Sicherheit**: Die Verwendung von HTTPS für die Verschlüsselung der Datenübertragung ist unerlässlich. API-Schlüssel oder JWT-Authentifizierung sollten implementiert werden, um unbefugten Zugriff auf die Schnittstelle zu verhindern.

- **Skalierbarkeit**: Die Anwendung sollte so konzipiert sein, dass sie bei steigender Last durch horizontale Skalierung (z. B. Load Balancing) oder vertikale Skalierung (mehr Ressourcen) erweiterbar ist.

- **Monitoring und Logging**: Eine kontinuierliche Überwachung der Server-Performance, API-Anfragen und Fehler ist notwendig, um Probleme frühzeitig zu erkennen und zu beheben.

- **Rate Limiting**: Um Missbrauch der API zu verhindern, sollte eine Begrenzung der Anzahl von Anfragen pro Benutzer/IP-Adresse implementiert werden.

- **Fehlertoleranz**: Mechanismen sollten vorhanden sein, die sicherstellen, dass die Anwendung auch bei Ausfällen einzelner Komponenten oder hoher Last funktionsfähig bleibt.

- **Datenbank-Optimierung**: Abfragen sollten optimiert sein, beispielsweise durch Indexierung, um eine schnelle und effiziente Datenverarbeitung zu gewährleisten.

## Technologien

- **Java 17**
- **Maven**: Build-Management-Tool
- **Spring Boot**: Framework für schnelle und effiziente Entwicklung
    - **Spring Boot Security**: Zur Implementierung der Authentifizierungs- und Autorisierungslogik
    - **Spring Boot Web**: Zur Bereitstellung der REST-API
    - **Spring Boot JWT Token**: Zur JWT-Token-Generierung und -Verifizierung
- **PostgreSQL**: Datenbank für die Benutzerdaten
- **Docker**: Containerisierung der Datenbank


# Arbeitsprotokoll für das Cloud-Datenmanagement-Projekt

## 1. Docker-Setup

### Docker-Compose-Datei

Wir haben eine `docker-compose.yml`-Datei erstellt, um die erforderlichen Dienste zu konfigurieren. Diese Datei enthält zwei Hauptdienste: `db` für die PostgreSQL-Datenbank und `java-build` für die Java-Anwendung.

```yaml
services:
  db:
    image: postgres:latest
    container_name: postgres-db
    environment:
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: db
    volumes:
      - ./init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "5432:5432"
    networks:
      - mynetwork

  java-build:
    image: jelastic/maven:3.9.9-temurinjdk-21.0.2-almalinux-9
    container_name: java-build-container
    command: >
      sh -c "git clone https://github.com/TGM-HIT/syt5-gek961-cloud-datenmanagement-stopping_wellpach.git && cd syt5-gek961-cloud-datenmanagement-stopping_wellpach && mvn clean install && mv file.json target/file.json && cd target && java -jar Cloud_Datenmanagement-1.0-SNAPSHOT.jar"
    depends_on:
      - db
    ports:
      - "8080:8080"
    networks:
      - mynetwork

networks:
  mynetwork:
    driver: bridge
```

**Erklärungen:**
- Der `db`-Service nutzt das `postgres`-Image, um eine PostgreSQL-Datenbank zu betreiben. Die Umgebungsvariablen setzen das Passwort und den Namen der Datenbank.
- Der `java-build`-Service verwendet ein Maven-Image, um die Java-Anwendung zu bauen und auszuführen. Es werden die erforderlichen Ports freigegeben und die Abhängigkeit zur Datenbank definiert.

### SQL-Datenbankinitialisierung

Eine SQL-Datei (`init.sql`) wurde erstellt, um die Benutzertabelle zu initialisieren:

```sql
CREATE TABLE IF NOT EXISTS Users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    userid VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(100) NOT NULL
);
```

## 2. Java-Anwendung

### Hauptanwendungsklasse

Die Hauptanwendungsklasse (`Main.java`) wurde erstellt, um die Spring Boot-Anwendung zu starten:

```java
package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
```

### JDBC-Konfiguration

Die JDBC-Konfiguration (`JdbcConfiguration.java`) stellt die Verbindung zur PostgreSQL-Datenbank her und konfiguriert das Entity-Manager-Factory:

```java
package org.example;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class JdbcConfiguration {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://db:5432/db");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.show_sql", "true");

        em.setJpaProperties(properties);
        em.setPackagesToScan("org.example.user.entities");  // Anpassung an die Projektstruktur

        return em;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
```

**Erklärungen:**
- Die `dataSource()`-Methode konfiguriert die Verbindung zur PostgreSQL-Datenbank.
- Die `entityManagerFactory()`-Methode konfiguriert Hibernate für die Nutzung von JPA.
- Es werden die erforderlichen Hibernate-Eigenschaften gesetzt.

### JSON-Hilfsklasse

Die `JsonHelper`-Klasse stellt eine Methode zur Verfügung, um HashMaps in JSON-Strings umzuwandeln:

```java
package org.example;

import org.json.JSONObject;

import java.util.HashMap;

public class JsonHelper {
    public static <K, V> String toJSON(HashMap<K,V> map) {
        return new JSONObject(map).toString();
    }
}
```

**Erklärungen:**
- Die Methode `toJSON` verwendet die `JSONObject`-Klasse aus der JSON-Bibliothek, um eine HashMap in einen JSON-String zu konvertieren.

### Benutzerdaten

Die Benutzerinformationen werden als JSON-Daten gespeichert:

```json
[
  {
    "name": "Melissa Wallpach",
    "userid": "melissa",
    "password": "test1",
    "role": "admin"
  },
  {
    "name": "Markus Stuppnig",
    "userid": "markus",
    "password": "test2",
    "role": "reader"
  }
]
```

## 3. Maven-Konfiguration

Die `pom.xml`-Datei definiert die Abhängigkeiten und Plugins für das Projekt:

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.2.0</version>
        <relativePath/>
    </parent>
    <groupId>org.example</groupId>
    <artifactId>Cloud_Datenmanagement</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <!-- Abhängigkeiten für Spring Boot und PostgreSQL -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
        </dependency>
        <!-- Weitere Abhängigkeiten -->
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

**Erklärungen:**
- Die Datei definiert die Versionen von Spring Boot und Java.
- Es werden Abhängigkeiten für Spring Data JPA, PostgreSQL und weitere Bibliotheken hinzugefügt.

