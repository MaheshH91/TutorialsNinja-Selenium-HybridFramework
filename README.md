# TutorialsNinja Enterprise Selenium Hybrid Automation Framework

[![Java 25](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Selenium](https://img.shields.io/badge/Selenium%20WebDriver-4.28.1-43B02A?style=for-the-badge&logo=selenium&logoColor=white)](https://www.selenium.dev/)
[![TestNG](https://img.shields.io/badge/TestNG-7.10.2-FFB703?style=for-the-badge&logo=testng&logoColor=white)](https://testng.org/)
[![Maven](https://img.shields.io/badge/Apache%20Maven-3.9+-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Apache POI](https://img.shields.io/badge/Apache%20POI-5.2.5-D22128?style=for-the-badge&logo=apache&logoColor=white)](https://poi.apache.org/)
[![ExtentReports](https://img.shields.io/badge/ExtentReports-5.1.1-6C5CE7?style=for-the-badge&logo=codewars&logoColor=white)](https://extentreports.com/)
[![Log4j2](https://img.shields.io/badge/Log4j2-2.24.3-E53935?style=for-the-badge&logo=apache&logoColor=white)](https://logging.apache.org/log4j/2.x/)
[![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-CI%2FCD-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)](https://github.com/features/actions)
[![Jenkins](https://img.shields.io/badge/Jenkins-Pipeline-D24939?style=for-the-badge&logo=jenkins&logoColor=white)](https://www.jenkins.io/)
[![Browsers](https://img.shields.io/badge/Browsers-Chrome%20%7C%20Firefox%20%7C%20Edge-4285F4?style=for-the-badge&logo=googlechrome&logoColor=white)](https://www.google.com/chrome/)

[![GitHub](https://img.shields.io/badge/GitHub-Profile-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/MaheshH91)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-Mahesh%20Holkar-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/maheshholkarqaengineer/)

---

A modular, thread-safe, enterprise-level test automation solution built on the **Page Object Model (POM)** design pattern. Engineered to validate end-to-end functionality across web applications with full CI/CD orchestration, dynamic reporting, cross-browser compatibility, and flaky test auto-healing.

## 📑 Table of Contents
* [Architecture & Tech Stack](#-architecture--tech-stack)
* [Framework Key Features](#-framework-key-features)
* [Project Directory Structure](#-project-directory-structure)
* [Prerequisites & Installation](#-prerequisites--installation)
* [Configuration & Data Management](#-configuration--data-management)
* [Test Execution Guide](#-test-execution-guide)
* [Reporting & Diagnostics](#-reporting--diagnostics)
* [CI/CD Integration](#-cicd-integration)
* [Author & Connect](#-author--connect)

---

## 🏗 Architecture & Tech Stack

| Component | Technology | Version | Purpose |
| :--- | :--- | :--- | :--- |
| **Language** | Java OpenJDK | `25` | Modern language features, strict native access handling |
| **Automation Tool** | Selenium WebDriver | `4.28.1` | Cross-browser DOM driver & browser interaction |
| **Test Runner** | TestNG | `7.10.2` | Test sequencing, DataProviders, parallel execution |
| **Build Automation** | Apache Maven | `3.9+` | Build lifecycle, dependency resolution, Surefire runner |
| **Test Data Engine** | Apache POI | `5.2.5` | Dynamic Excel parsing (`DataFormatter`) |
| **Reporting** | ExtentReports Spark | `5.1.1` | Rich interactive HTML dashboard with Base64 screenshots |
| **Logging** | Log4j2 + SLF4J | `2.24.3` | Structured runtime console and rolling file logging |
| **CI/CD** | GitHub Actions & Jenkins | - | Continuous regression runs and automated report distribution |

---

## ⚡ Framework Key Features

* **Thread-Safe Architecture**: Uses `ThreadLocal<WebDriver>` within a dedicated `DriverManager` to prevent race conditions during multi-threaded parallel execution.
* **Robust Page Object Model (POM)**: Decoupled page elements and actions via `BasePage` incorporating explicit, configurable `WebDriverWait` synchronization.
* **Flaky Test Resilience**: Automatic test retries implemented via custom `RetryAnalyzer` and dynamic `AnnotationTransformer`.
* **Zero-Loss Base64 Screenshots**: Captures full-page Base64-encoded screenshots upon failure, embedding them directly into HTML reports without external file path dependency issues.
* **Data-Driven Testing (DDT)**: Integrated with Apache POI to parse matrix-based login and registration permutations directly from `TutorialsNinjaTestData.xlsx`.
* **Headless Execution Support**: Optimized for CI agents with native headless Chrome, Edge, and Firefox options.

---

## 📂 Project Directory Structure

```text
TutorialsNinja-Selenium-HybridFramework/
├── .github/
│   └── workflows/
│       └── test-execution.yml        # GitHub Actions matrix regression runner
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/tutorialsninja/qa/
│   │           ├── constants/
│   │           │   └── FrameworkConstants.java    # Centralized timeouts & configurations
│   │           ├── driver/
│   │           │   ├── DriverFactory.java         # Browser instantiation logic
│   │           │   └── DriverManager.java         # ThreadLocal driver lifecycle manager
│   │           ├── pages/
│   │           │   ├── BasePage.java              # Fluent wait abstractions & action wrappers
│   │           │   ├── HomePage.java
│   │           │   ├── LoginPage.java
│   │           │   ├── RegisterPage.java
│   │           │   ├── AccountPage.java
│   │           │   ├── AccountSuccessPage.java
│   │           │   └── SearchPage.java
│   │           └── utils/
│   │               ├── ExtentManager.java         # ThreadLocal ExtentTest manager
│   │               ├── ExtentReporterNg.java      # Dashboard and theme configuration
│   │               └── UtilitiesCLS.java          # Excel loader, screenshot utilities
│   └── test/
│       ├── java/
│       │   └── com/tutorialsninja/qa/
│       │       ├── base/
│       │       │   └── Base.java                  # Suite setup, properties initialization
│       │       ├── listeners/
│       │       │   ├── AnnotationTransformer.java # Dynamic retry transformer
│       │       │   ├── MyListeners.java           # TestNG lifecycle events & report hook
│       │       │   └── RetryAnalyzer.java         # Automatic failure retry logic
│       │       └── testcases/
│       │           ├── LoginTest.java             # Login functional test permutations
│       │           ├── RegisterTest.java          # Account creation edge cases
│       │           └── SearchTest.java            # Product query & catalog validation
│       └── resources/
│           ├── config.properties                  # Environment URLs & timeout keys
│           ├── testdata.properties                # Expected message assertion values
│           ├── TutorialsNinjaTestData.xlsx        # Data-driven test sheets
│           ├── log4j2.xml                         # Logging patterns and appenders
│           └── testng.xml                         # TestNG suite definition
├── Jenkinsfile                                    # Declarative pipeline for Jenkins CI
├── pom.xml                                        # Maven project object model dependencies
└── README.md
```

---

## 🛠 Prerequisites & Installation

### 1. Requirements
* **JDK**: Java Development Kit 25 (or JDK 17/21 LTS compatible)
* **Maven**: Version 3.9.0 or higher
* **Browsers**: Google Chrome, Mozilla Firefox, or Microsoft Edge installed locally

### 2. Setup
Clone the repository and verify dependency resolution:

```bash
git clone https://github.com/MaheshH91/TutorialsNinja-Selenium-HybridFramework.git
cd TutorialsNinja-Selenium-HybridFramework
mvn clean compile test-compile
```

---

## ⚙ Configuration & Data Management

### 1. Global Application Settings (`src/test/resources/config.properties`)
```properties
url=https://tutorialsninja.com/demo/
browserName=chrome
implicitWait=10
pageLoadTime=40
validEmail=maheshholkar14@gmail.com
validPassword=12345
```

### 2. Static Assertions (`src/test/resources/testdata.properties`)
Maintains dynamic UI alert messages, error text, and catalog search parameters.

---

## 🚀 Test Execution Guide

### Run via TestNG XML
```bash
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

### Run Cross-Browser and Headless Modes
```bash
# Chrome Headless (Default for CI)
mvn clean test -Dbrowser=chrome -Dheadless=true

# Mozilla Firefox
mvn clean test -Dbrowser=firefox

# Microsoft Edge
mvn clean test -Dbrowser=edge
```

### Parallel Thread Configuration
Adjust the thread count and parallel execution mode directly in `src/test/resources/testng.xml`:
```xml
<suite name="TutorialsNinja Automation Suite" parallel="tests" thread-count="2">
```

---

## 📊 Reporting & Diagnostics

### Interactive ExtentReport
Following test completion, an interactive HTML report is generated at:
```text
test-output/ExtentReports/extentReport.html
```

#### Report Capabilities:
* **Interactive Dashboard**: Aggregated pass/fail/skip breakdown metrics.
* **Environment Context**: Java version, operating system, target browser, and execution timestamps.
* **Embedded Screenshots**: Inline Base64 images embedded on test failure.
* **Log Integration**: Detailed stack traces attached to failed step nodes.

---

## 🔄 CI/CD Integration

### 1. GitHub Actions (`.github/workflows/test-execution.yml`)
* Triggered on pushes/pull requests to the `main` branch or manual dispatch.
* Downloads OpenJDK 25, provisions headless Chrome, and runs the regression suite.
* Uploads the Extent HTML report as a persistent workflow artifact.
* Sends an automated email summary containing the execution status and report attachment via SMTP.

#### Required Repository Secrets:
* `MAIL_USERNAME`: SMTP email address
* `MAIL_PASSWORD`: SMTP app password
* `MAIL_TO`: Destination report recipient

### 2. Jenkins Declarative Pipeline (`Jenkinsfile`)
* Automatically picks up agent nodes with `JDK-25` and `Maven-3.9`.
* Archives Surefire and Extent reports.
* Publishes execution results via the Jenkins `TestNG` and `Email-Extension` plugins.

---

## 👤 Author & Connect

**Mahesh Holkar**
*Senior QA Automation Engineer SDET*

* **LinkedIn:** [maheshholkarqaengineer](https://www.linkedin.com/in/maheshholkarqaengineer/)
* **GitHub:** [MaheshH91](https://github.com/MaheshH91)
* **Repository:** [TutorialsNinja-Selenium-HybridFramework](https://github.com/MaheshH91/TutorialsNinja-Selenium-HybridFramework)
