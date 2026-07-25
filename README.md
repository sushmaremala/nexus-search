# 🚀 NexusSearch – High-Performance Search & Indexing Engine

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.1-green.svg)](https://spring.io/projects/spring-boot)
[![Redis](https://img.shields.io/badge/Redis-Upstash-red.svg)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Async-blue.svg)](https://www.rabbitmq.com/)

> **NexusSearch** is a high-throughput, sub-15ms search and indexing engine engineered in Java and Spring Boot. It features an in-memory **Trie** data structure for instant autocomplete suggestions, an **Inverted Index** with TF-IDF relevance ranking, and **RabbitMQ** background message queuing.

---

## 🏗️ System Architecture

```
                                  ┌───────────────────────────────┐
                                  │      React.js Frontend        │
                                  └───────────────┬───────────────┘
                                                  │
                                          HTTP / REST APIs
                                                  │
                                                  ▼
                                  ┌───────────────────────────────┐
                                  │  Spring Boot Search Gateway   │
                                  └───────┬───────────────┬───────┘
                                          │               │
                 ┌────────────────────────┘               └────────────────────────┐
                 ▼                                                                 ▼
   ┌───────────────────────────┐                                     ┌───────────────────────────┐
   │    Trie Autocomplete      │                                     │   Redis Query Cache       │
   │  (Sub-15ms Suggestions)   │                                     │ (In-Memory Fast Lookup)   │
   └───────────────────────────┘                                     └───────────────────────────┘
                 │                                                                 │
                 └────────────────────────┬────────────────────────────────────────┘
                                          │
                                          ▼
                         ┌─────────────────────────────────┐
                         │   Inverted Index & TF-IDF Engine│
                         └────────────────┬────────────────┘
                                          │
                                          ▼
                         ┌─────────────────────────────────┐
                         │    RabbitMQ Async Indexer       │
                         └─────────────────────────────────┘
```

---

## ✨ Key Features

1. **Sub-15ms Autocomplete Search (Trie Data Structure)**:
   - Uses a custom **Trie (Prefix Tree)** stored in RAM to return top search suggestions instantly as the user types.

2. **Inverted Index & Relevance Ranking**:
   - Maps keywords directly to document IDs using an **Inverted Index**, evaluating document relevance via **TF-IDF scoring**.

3. **Redis Query Caching**:
   - Caches top-searched query responses in **Redis**, lowering latency to **< 5ms** for frequent search terms.

4. **Asynchronous RabbitMQ Document Ingestion**:
   - Offloads new document indexing to **RabbitMQ queues**, ensuring zero live query blocking for active users.

---

## ⚡ API Endpoints

### 1. Instant Autocomplete
- **Endpoint**: `GET /api/v1/search/autocomplete?prefix={term}`
- **Sample Response**:
```json
{
  "prefix": "jav",
  "suggestions": [
    "java spring boot",
    "javascript",
    "java microservices"
  ],
  "latencyMs": 12
}
```

### 2. Full-Text Document Search
- **Endpoint**: `GET /api/v1/search/query?q={query}`
- **Sample Response**:
```json
{
  "query": "java spring boot",
  "totalResults": 2,
  "results": [
    {
      "id": 101,
      "title": "Spring Boot Microservices Architecture",
      "score": 4.85
    }
  ]
}
```

---

## 🛠️ Getting Started Locally

```bash
# 1. Clone the repository
git clone https://github.com/sushmaremala/nexus-search.git
cd nexus-search

# 2. Start Redis & RabbitMQ via Docker
docker-compose up -d

# 3. Build & Run the Spring Boot Server
./mvnw spring-boot:run
```
