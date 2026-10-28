# Caso 1 — Reserva concurrente de entradas

Proyecto JavaFX para simular la reserva concurrente de 20 entradas mediante
tres agentes. Cada agente realiza 10 solicitudes.

## Tecnologias

- JDK 21
- JavaFX 21.0.6
- Maven Wrapper
- JUnit 5

## Ejecucion

Desde la carpeta `Caso1`:

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12'
.\mvnw.cmd clean javafx:run
```

## Pruebas

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.12'
.\mvnw.cmd clean test
```
