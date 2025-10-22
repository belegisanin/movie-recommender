# Intelligent Movie Recommender (Knowledge Engineering Project)

This project implements a **hybrid intelligent system** for managing and recommending movies.  
It combines **ontological reasoning**, **fuzzy logic**, and **case-based reasoning (CBR)** to provide movie suggestions, evaluate movie quality, and find movies most similar to a given one.

## Features

### 1. Ontology-based Movie Recommendation
- Movies are described with semantic metadata (title, genre, director, actors, year, etc.)
- Ontology modeled in **OWL 2 DL** using **Protégé**
- Separated into:
  - `movie-ontology-schema.owl` — classes and properties
  - `movie-ontology-instances.owl` — instances (movies, people, genres)
- Queried using **SPARQL** via the **Apache Jena** framework

### 2. Fuzzy Logic Quality Evaluation
- A fuzzy inference system estimates the quality of a movie (`bad`, `good`, `excellent`)
- Input criteria: direction, acting, scenario, visual effects, cultural value, etc.
- Implemented with **jFuzzyLogic**
- Includes at least **10 fuzzy rules**

### 3. Case-Based Reasoning (CBR) Similarity
- Finds movies similar to a user-provided one
- Implemented with **jColibri 3.2**
- Movie similarity is computed using normalized attribute comparison:
  - Genres (set overlap)
  - Director (exact match)
  - Actors (set overlap)
  - Year (interval distance)
- Supports user input for flexible querying

## Technologies Used

| Category | Tool / Library |
|-----------|----------------|
| Ontology | OWL 2 DL, Protégé |
| Reasoning | Apache Jena, HermiT Reasoner |
| CBR | jColibri 3.2 |
| Fuzzy Logic | jFuzzyLogic |
| Language | Java 24 |
| Logging | SLF4J / Log4j |
| Build System | Maven |

## Academic Context

This project was developed as part of the **Knowledge Engineering (Inženjering znanja)** course at the University of Novi Sad (2024/2025).  
It demonstrates integration of symbolic reasoning, fuzzy logic, and case-based reasoning techniques for intelligent decision support.
