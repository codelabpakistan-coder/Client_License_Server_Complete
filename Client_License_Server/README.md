# Clinic License Server

Manual Maven + Spring Boot license server for the Clinic desktop application.

## Requirements

- Java 21 or newer
- IntelliJ IDEA Community or another Java IDE
- Maven (the included Maven wrapper can also be used if added by your IDE)

## Run in IntelliJ

1. Open this folder.
2. Open `pom.xml` as a Maven project.
3. Wait for Maven dependencies to download.
4. Run:

`org.example.clinic.licenseserver.LicenseServerApplication`

The server starts on:

`http://localhost:8080`

The SQLite database is automatically created as:

`licenses.db`

## Test the server

Open:

`http://localhost:8080/api/health`

Expected response:

```json
{
  "success": true,
  "message": "Clinic License Server is running"
}
```

## Generate a license

Use Postman, curl, or another HTTP client.

POST:

`http://localhost:8080/api/licenses/generate`

JSON:

```json
{
  "customerName": "ABC Medical Store"
}
```

Example response contains a generated key similar to:

`AB7K2-QP9LM-7X2RT-MN8CY`

Keep this key private until you give it to your client.

## Activate a computer

POST:

`http://localhost:8080/api/licenses/activate`

JSON:

```json
{
  "licenseKey": "YOUR-LICENSE-KEY",
  "computerId": "YOUR-COMPUTER-ID"
}
```

The first computer activates the license.

A second computer using the same license gets:

`This license is already activated on another computer.`

## Validate a license

POST:

`http://localhost:8080/api/licenses/validate`

JSON:

```json
{
  "licenseKey": "YOUR-LICENSE-KEY",
  "computerId": "YOUR-COMPUTER-ID"
}
```

The Clinic desktop application will use this endpoint at startup.

## Important production note

Do not expose the `/api/licenses/generate`, `/api/licenses`, `/deactivate`, or `/reactivate` administration endpoints publicly without authentication.

For the first local test this is intentionally simple. After activation works, the next step is to add an admin secret/API authentication and then connect the JavaFX Clinic application to `/activate` and `/validate`.
