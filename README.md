
# GK961 Middleware Engineering
## User Management Webservice
**Author:** Stuppnig Markus, Wallpach Melissa

**Version:** 2024-10-10

## Einführung
Dieses Projekt implementiert einen einfachen Webservice zur Benutzerverwaltung. Die Webanwendung bietet die Möglichkeit zur Registrierung, Anmeldung und Verifizierung von Benutzern. Die Authentifizierung basiert auf JWT-Token und die Daten werden in einer PostgreSQL-Datenbank gespeichert.
Durch eine JSOn-Datei werden vordefinierte Benutzer bei der Initialisierung gespeichert.
## Technologien
- **Java 17**
- **Maven**: Build-Management-Tool
- **Spring Boot**: Framework für schnelle und effiziente Entwicklung
    - **Spring Boot Security**: Zur Implementierung der Authentifizierungs- und Autorisierungslogik
    - **Spring Boot Web**: Zur Bereitstellung der REST-API
    - **Spring Boot JWT Token**: Zur JWT-Token-Generierung und -Verifizierung
- **PostgreSQL**: Datenbank für die Benutzerdaten
- **Docker**: Containerisierung der Datenbank