# VulnEye

VulnEye is a modular and scalable Vulnerability Assessment and Penetration Testing (VAPT) platform built using Java and Spring Boot. The goal of the project is to automate security assessments by integrating multiple security tools into a unified platform.

> **Project Status:** 🚧 Under Active Development

---

## Current Features

### Authentication & Authorization
- JWT Authentication
- Spring Security Integration
- Role-Based Access Control (RBAC)

### User Management
- User CRUD APIs
- Role Management

### Asset Management
- Create Assets
- Update Assets
- Delete Assets
- Retrieve Assets

### Scan Management
- Create Scans
- Retrieve Scan Details
- Scan Status Tracking

### Scanner Execution Engine
- Asynchronous Scan Execution (`@Async`)
- Template Method Pattern
- Scanner Factory Pattern
- ProcessBuilder-based Command Execution
- Modular Scanner Architecture
- Initial Nmap Scanner Integration

---

## Architecture

```
                +----------------+
                |   REST API     |
                +-------+--------+
                        |
                        v
              +------------------+
              | Scan Service     |
              +--------+---------+
                       |
                       v
          +------------------------+
          | Scan Execution Service |
          +-----------+------------+
                      |
                      v
             +-------------------+
             | Scanner Factory   |
             +---------+---------+
                       |
        +--------------+--------------+
        |              |              |
        v              v              v
    NmapScanner   NucleiScanner   NiktoScanner
        |
        v
  Command Executor
        |
        v
 ProcessBuilder API
```

---

## Tech Stack

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Maven

---

## Design Patterns Used

- Factory Pattern
- Template Method Pattern
- Dependency Injection
- Service Layer Pattern
- Repository Pattern

---

## Completed Milestones

- ✅ Authentication & JWT
- ✅ Role-Based Access Control
- ✅ User Management
- ✅ Asset Management
- ✅ Scan Management
- ✅ Scanner Execution Engine
- ✅ Asynchronous Scan Processing

---

## Upcoming Features

- Real Nmap Scanning
- XML Report Generation
- XML Parsing
- Finding Management
- Nuclei Integration
- Nikto Integration
- Report Generation
- Scheduled Scanning
- Dashboard & Analytics

---

## Project Goal

Build an enterprise-grade VAPT platform capable of integrating multiple security tools into a unified, scalable, and extensible security assessment solution.

---

## Author

**Tejas Chaudhari**

B.Tech Computer Engineering

Cybersecurity Enthusiast