# Fleckbot (TCC)

Legacy Java/Spring WAR application for trading strategy experiments and dashboards.

## Current stack
- Java 8
- Maven (WAR packaging)
- Spring MVC + Spring Security + Hibernate/JPA
- JSP views
- XML-based runtime config (`src/main/resources/xml/*.xml`)

## Quick start (local)

### 1) Prerequisites
- JDK 8 (recommended for current codebase)
- Maven 3.8+
- PostgreSQL

### 2) Configure application
Update:
- `src/main/resources/application.properties`
  - DB URL
  - DB username/password

Update exchange credentials:
- `src/main/resources/xml/exchange.xml`

### 3) Build
```bash
mvn -q -DskipTests package
```

WAR output:
- `target/fleckbot-1.0.war`

### 4) Run options
- Deploy WAR into Tomcat/Jetty, **or**
- Use your existing app server workflow.

## Default credentials (development only)
- `admin / ADMIN`
- `user / USER`

> Change these for any non-local environment.

## First modernization pass (already applied)
- Added CI workflow for Maven build + test on push/PR
- Added `.editorconfig` baseline for consistent formatting
- Expanded setup documentation (this README)

## Next modernization phases

### Phase 2 (safe upgrades)
- Dependency refresh for known vulnerable/old libs
- Add Maven Enforcer + plugin version pinning
- Replace legacy frontend libs (jQuery/bootstrap v3 static assets)

### Phase 3 (structural)
- Spring upgrade path (toward Boot)
- Move XML configs to typed properties where feasible
- Add integration tests and test fixtures

## Notes
This project is your TCC project. Modernization should prioritize:
1) build stability,
2) security updates,
3) incremental refactors without breaking behavior.
