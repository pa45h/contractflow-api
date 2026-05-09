# ContractFlow — Contract Management Backend

This repository contains a lightweight Java backend for storing contracts (PDF), extracting basic text from uploaded documents, tracking contract status, and answering simple questions about contract content.

Key features
- Upload and store contract files (PDF)
- Persist contract metadata and extracted text
- Status lifecycle: DRAFT → REVIEW → APPROVED (enforced)
- Basic question answering via keyword/paragraph search in the extracted text
- OpenAPI (Swagger) UI for interactive API exploration

Checklist
- [x] Build & run the service
- [x] Upload a contract (PDF)
- [x] Fetch contract metadata and extracted text
- [x] Update contract status with validated transitions
- [x] Ask simple questions about a stored contract

Technology stack
- Java 17
- Spring Boot (Web, Data JPA, Validation)
- Apache PDFBox for PDF text extraction
- SpringDoc OpenAPI for API docs
- MySQL by default (configurable)
- Maven (wrapper provided)

Project layout (important files)
- `contractflow/pom.xml` — Maven configuration and dependencies
- `contractflow/src/main/java/.../controller/ContractController.java` — REST endpoints
- `contractflow/src/main/java/.../service/ContractService.java` — business logic
- `contractflow/src/main/java/.../util/DocumentParserUtil.java` — PDF parsing
- `contractflow/src/main/resources/application.properties` — default configuration

Configuration
1. Edit `contractflow/src/main/resources/application.properties` to set your database credentials and file upload directory. Defaults:

- spring.datasource.url=jdbc:mysql://localhost:3306/contract_management
- file.upload-dir=D:/ContractFlow/uploads/

2. To run quickly without configuring MySQL, you can switch to an embedded H2 database by updating the datasource settings or creating a Spring profile that uses H2.

Build & run
From the project root (`D:\ContractFlow`) you can use the included Maven wrapper.

Windows (PowerShell):
```
cd D:\ContractFlow\contractflow
.\mvnw.cmd clean package
.\mvnw.cmd spring-boot:run
```

Or run the built jar:
```
java -jar target/contractflow-0.0.1-SNAPSHOT.jar
```

API reference
Base path: http://localhost:8080

Interactive docs (Swagger UI):
- http://localhost:8080/swagger-ui.html
  (or http://localhost:8080/swagger-ui/index.html)

Endpoints

1) Upload a contract
- POST /contracts
- Content-Type: multipart/form-data
- Form fields:
  - title: string (required)
  - file: file (required) — only PDF files are accepted
- Example (curl):
```
curl -X POST "http://localhost:8080/contracts" \
  -F "title=My Contract" \
  -F "file=@/path/to/contract.pdf"
```

Response: JSON representation of the saved contract

2) Fetch contract details
- GET /contracts/{id}
- Example:
```
curl http://localhost:8080/contracts/1
```

Response fields (example):
```
{
  "id": 1,
  "title": "My Contract",
  "fileName": "168..._contract.pdf",
  "filePath": "D:\\ContractFlow\\uploads\\168..._contract.pdf",
  "extractedText": "...full text extracted from PDF...",
  "status": "DRAFT",
  "createdAt": "2026-05-08T12:34:56"
}
```

3) Update contract status
- PUT /contracts/{id}/status
- Request body (JSON):
```
{
  "status": "REVIEW"
}
```
- Allowed transitions:
  - DRAFT -> REVIEW
  - REVIEW -> APPROVED
  - No-op when setting the same status
- Example (curl):
```
curl -X PUT "http://localhost:8080/contracts/1/status" \
  -H "Content-Type: application/json" \
  -d '{"status":"REVIEW"}'
```

4) Ask a question about a contract
- POST /contracts/{id}/ask
- Request body (JSON):
```
{
  "question": "What is the termination clause?"
}
```
- The service performs a simple keyword-based paragraph lookup on the extracted text and returns the first matching paragraph or a "No relevant information found" message.
- Example (curl):
```
curl -X POST "http://localhost:8080/contracts/1/ask" \
  -H "Content-Type: application/json" \
  -d '{"question":"termination"}'
```

Notes & considerations
- Only PDF parsing is implemented (PDFBox). Attempting to upload non-PDF files will return an error.
- File storage: uploaded files are saved to the path configured by `file.upload-dir`.
- Security: there is no authentication implemented. Do not expose this service to untrusted networks without adding authentication and authorization.
- Error handling: the service returns JSON error responses. Validation and custom exceptions are handled centrally.
- Tests: a basic test class exists under `src/test` — extend with more tests as needed.

Extending the project
- Add DOCX support by integrating Apache POI (extract text from Word files) and updating `DocumentParserUtil`.
- Replace the simple keyword QA with an NLP/semantic search solution (embedding-based search or an LLM integration) for better QA results.
- Add authentication (e.g., JWT) and role-based access control.

License
Add a license file to this repository (e.g., MIT, Apache 2.0) if you plan to publish the code publicly.

Contact / Contribution
If you plan to extend this project, feel free to open issues or PRs. Include clear descriptions for bugs or feature requests.

