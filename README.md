# 🤖 AI-Powered Selenium Test Agent

An autonomous test automation agent that leverages **OpenAI** to generate, execute, and self-heal **Selenium** test scripts. This project aims to bridge the gap between natural language requirements and robust web automation testing.

---

## ✨ Features

*   **Natural Language to Test Scripts:** Describe your test scenario in plain English, and let OpenAI generate the corresponding Selenium code.
*   **Automated Execution:** Seamlessly runs the generated scripts using Selenium WebDriver.
*   **Self-Healing Capabilities:** Detects broken locators or UI changes and attempts to fix them using AI reasoning.
*   **Detailed Reporting:** Outputs clear execution logs and test results.

---

## 🛠️ Tech Stack

*   **Language:** Java
*   **AI Integration:** OpenAI API (`gpt-4o` / `gpt-3.5-turbo`)
*   **Automation Framework:** Selenium WebDriver
*   **Testing Framework:** Cucumber/ TestNG *(update as needed)*

---

## 📁 Project Structure

```text
selenium-ai-agent/
│
├── agent/                # Core AI agent logic and prompt management
├── drivers/              # WebDriver binaries / configurations
├── tests/                # Generated and custom Selenium test scripts
├── .env.example          # Environment variables template
├── requirements.txt      # Python dependencies
└── README.md             # Project documentation
