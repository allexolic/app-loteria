# Lottery — Results & Favorite Games

Full stack application (Backend + Frontend) that allows users to:

- Upload **XLSX files** containing Lotofácil draw results  
- Store imported contests and drawn numbers  
- Register **favorite games** (15 numbers each)  
- Recalculate game metrics such as:
  - `drawnGame` → indicates whether the exact game has already been drawn (15 matches)
  - `maxMatchedNumbers` → highest number of matches achieved in any contest

---

## Repository Structure

- web-frontend/ # React + Vite + MUI
- app-backend/ # Java 17 + Spring Boot + JPA + PostgreSQL


---

## Requirements

### Backend
- Java 17
- Maven 3.8+
- PostgreSQL 13+ (or Docker)

### Frontend
- Node.js 18+ (recommended)
- npm 9+

---

## Configuration

### 1) Database (PostgreSQL)
Create a database, for example:
- `lottery_db`

You can configure database access using environment variables (recommended):

**Example variables:**
- `DB_HOST`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`

Create an example file and keep the real one out of version control:
- `web-loteria/.env.example` (committed)
- `web-loteria/.env` (not committed)

---

## Running the Backend

Navigate to the backend folder:

cd app-backend

Run the application:

mvn clean spring-boot:run

Default backend URL: http://localhost:8080

## Running the Frontend

Navigate to the frontend folder:

cd web-frontend

Run the application:

npm run dev

Default frontend URL: http://localhost:5173

## Main API Endpoints

### Upload Draw Results (XLSX)

- POST `/api/loteria/upload` (multipart/form-data, fieldName: `file`)

### Imported Files

- GET `/api/loteria/arquivos`

### Favorite Games

- POST `/api/jogos/favoritos`

  { "balls": [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15] }

- GET `/api/jogos/favoritos`
- POST `/api/jogos-favoritos/recalcular`

  { "gameId": 1 }
