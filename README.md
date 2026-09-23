# Catalog Sync API

Pulls products from a VTEX store's **public** Catalog API, stores them locally, and
exposes them through a small REST API. Built as a portfolio piece to show a real
VTEX integration end to end: outbound HTTP client, persistence, REST layer, tests.

## Stack

- Java 17, Spring Boot 3.3 (Web, Data JPA)
- H2 (file-based) for local runs — no install required
- PostgreSQL for production use (`postgres` profile)

## Run locally

No setup needed, H2 is the default profile:

```bash
mvn spring-boot:run
```

App starts on `http://localhost:8080`.

## Run against PostgreSQL

```bash
export DB_URL=jdbc:postgresql://localhost:5432/catalogsync
export DB_USER=postgres
export DB_PASSWORD=postgres
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## Endpoints

| Method | Path                | Description                                  |
|--------|---------------------|-----------------------------------------------|
| POST   | `/api/sync`         | Fetch products from VTEX and store them       |
| GET    | `/api/products`     | List locally synced products                  |
| GET    | `/api/products/{id}`| Get one product by VTEX `productId`           |

### Example

```bash
curl -X POST "http://localhost:8080/api/sync?account=tokstok&query=cadeira"
curl "http://localhost:8080/api/products"
```

`account` is any live VTEX store's account name (the subdomain in
`https://{account}.vtexcommercestable.com.br`). No API key needed — it hits the
same public search endpoint the store's own site uses.

## Tests

```bash
mvn test
```

`SyncServiceTest` covers the VTEX → local mapping logic with a mocked client (no
network calls). `CatalogSyncApplicationTests` is a context-load smoke test that
catches datasource/JPA/RestClient wiring breakage.

## Run with Docker

```bash
docker compose up --build
```

Builds the image (multi-stage: Maven+JDK to compile, slim JRE to run) and starts
it alongside a real Postgres container. Same app, same `postgres` profile used in
production — this is what actually gets deployed.

## Deploy (Render free tier)

```
┌──────────────┐   push    ┌────────────────┐   JDBC    ┌──────────────────┐
│ GitHub repo   │ ────────▶ │ Render Web      │ ────────▶ │ Render Postgres   │
│ (Dockerfile)  │  builds   │ Service (free)  │           │ (free tier)       │
└──────────────┘   image   └────────────────┘           └──────────────────┘
```

1. Push this repo to GitHub (see the portfolio series root for the `gh repo create` command).
2. On [render.com](https://render.com): **New +** → **PostgreSQL** → free plan → name it
   `catalog-sync-db` → create. Copy **Host**, **Port**, **Database**, **User**, **Password**
   from its Info tab.
3. **New +** → **Web Service** → connect the GitHub repo → Environment: **Docker** → plan: **Free**.
4. Add environment variables:
   - `SPRING_PROFILES_ACTIVE=postgres`
   - `DB_URL=jdbc:postgresql://<host>:<port>/<database>`
   - `DB_USER=<user>`
   - `DB_PASSWORD=<password>`
5. Deploy. Render builds the `Dockerfile` and starts the service. Free web services
   sleep after 15 min idle and take ~30s to wake on the next request — expected
   behavior for a portfolio demo link, not a bug.

## Next steps (portfolio series)

1. **Catalog Sync API** (this repo)
2. Order Webhook Receiver — simulated order webhooks, HMAC signature validation
3. **Dockerize + deploy to a cloud free tier** (this repo's `Dockerfile` / `docker-compose.yml`)
