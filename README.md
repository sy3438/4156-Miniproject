# Tax API Service

A REST API for managing items and calculating sales tax.

## Setup

Requirements:
- Java 17
- Maven

Clone the repository and go into the project directory.

Run the application:

```bash
cd IndividualProject
mvn spring-boot:run
```

The application runs on port `8080`.

## Testing

Run the tests:

```bash
mvn test
```

Run Checkstyle:

```bash
mvn checkstyle:check
```

Generate the JaCoCo coverage report:

```bash
mvn clean test
```

The coverage report can be found at:

```text
target/site/jacoco/index.html
```

## API Endpoints

### POST /v1/clients

Creates a new client and API key.

### POST /v1/items

Creates a new item.

Requires an API key.

### GET /v1/items

Returns all items. Items can also be filtered by category or a search query.

Requires an API key.

### GET /v1/items/{id}

Returns an item by its ID.

Requires an API key.

### PATCH /v1/items/{id}

Updates the price of an existing item.

Requires an API key.

### DELETE /v1/items/{id}

Deletes an item.

Requires an API key.

### POST /v1/tax/quote

Calculates the tax rate, tax amount, and total price for a given state, price, and item category.

Requires an API key.

### GET /v1/supported

Returns the supported states and item categories.

Requires an API key.

## API Demo

API demo video:

https://drive.google.com/file/d/1bLxJXC51wviSXSUq5E4NGexbbFebexf0/view?usp=sharing
## Client App

The client application is a State Tax Comparison tool. It uses the `/v1/tax/quote` endpoint to compare the tax on an item between two states.

ChatGPT was used to help build the client application.

Client app demo video:

https://drive.google.com/file/d/1GBhVEMCeH9ExIk2vQD0YlG0cw4EIlIsI/view?usp=sharing
