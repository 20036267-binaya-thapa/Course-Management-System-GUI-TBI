# Course-Management-System-GUI-TBI

A Java-based **Course Management System** developed using both **GUI (Swing)** and **Text-Based Interface (TBI)** approaches.  
The system allows administrators to manage students, staff, courses, and performance evaluations efficiently using in-memory data structures.

This project demonstrates **object-oriented design**, **layered architecture**, and **basic algorithm and data structure usage**.

---

## Features

- Manage **Students**, **Staff**, and **Courses**
- Dual interface support:
  - 🖥️ GUI using Java Swing
  - 💻 Text-Based Interface (Console)
- Student performance evaluation system:
  - Immediate reward or penalty
  - Scheduled evaluations using a **Queue (FIFO)**
- In-memory data storage using Java Collections
- Clear separation of concerns using layered architecture

---

## System Architecture

The system follows a **three-layer architecture**:

- **Presentation Layer**
  - GUI (Swing): MainGUI, StudentPanel, StaffPanel, CoursePanel, EvaluationPanel
  - TBI (Console): Main (CLI)
- **Logic Layer**
  - CourseManager
  - Evaluation handling
- **Data Layer**
  - In-memory collections using `ArrayList` and `Queue`

This design improves maintainability, scalability, and clarity.

---

## UML Class Diagram

The UML Class Diagram models the core entities and relationships in the system, including:

- Abstract `Person` class
- `Student` and `Staff` subclasses
- `Course` entity
- Manager classes responsible for business logic

The diagram demonstrates encapsulation, inheritance, and abstraction.

---

## Algorithms & Data Structures Used

- **Linear Search** – O(n)  
  Used to locate students and courses in in-memory lists.
- **Sorting (TimSort via `Collections.sort`)** – O(n log n)  
  Used for ordered data presentation.
- **Queue (FIFO)**  
  Used to manage scheduled student evaluations.
- **ArrayList**  
  Used for dynamic storage of students, staff, and courses.

These choices provide sufficient performance for the expected dataset size.

---

## Technologies Used

- Java
- Java Swing (GUI)
- Java Collections Framework
- Object-Oriented Programming (OOP)
- UML (Class & Architecture Diagrams)

---

## How to Run

1. Clone the repository:
   ```bash
   git clone [https://github.com/20036267-binaya-thapa/Course-Management-System-GUI-TBI.git]
