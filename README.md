# 🎟️ Event Registration & Ticket Booking System
A complete Spring Boot backend for managing events, users, tickets, and payments with external payment gateway integration.  
The system follows clean architecture, secure JWT roles, and production-ready design patterns.

---

## 🚀 Features Overview

### 👥 User Management
- Registration & Login
- Role-based access control (Admin, Organizer, User)
- JWT authentication
- Profile & account handling

### 📅 Event Management
- Create & manage events
- Search & filter events
- Track seat availability
- Categorized events

### 🎫 Ticket Booking
- Ticket creation per user
- Mapping User → Event → Ticket
- Ticket status update
- QR Code ready structure

### 💳 Payment Handling (External API)

- Integration with external payment gateway
- Create payment requests
- Store payment response, status, transactionId
- Payment verification callback
- Map **Payment → Ticket** correctly

#### 🔗 Third-Party Payment Integration
- For demonstration/testing, the system integrates with a **fake third-party payment service**.
- The payment logic is abstracted using an interface so you can easily replace it with a real payment provider (Razorpay, Stripe, CashFree, Paytm) without rewriting code.
- Ticket booking triggers the payment workflow:
    - On **success** → payment is recorded & ticket is confirmed
    - On **failure** → booking/ticket creation is rolled back
- Mimics real-world payment gateway behavior for development purposes.

### ⚡ Additional Capabilities
- Clean REST API
- Global exception handling
- DTO + ModelMapper
- Validation using annotations
- PostgreSQL + Hibernate
- Optional Redis caching

---

## 🧩 Technology Stack

| Component | Tech |
|----------|------|
| Language | Java 17+ |
| Framework | Spring Boot |
| Security | JWT + Spring Security |
| Database | PostgreSQL |
| ORM | Hibernate/JPA |
| Cache (Optional) | Redis |
| Gateway | External Payment API (Razorpay / Stripe / Custom) |
| Build Tool | Gradle |
| Documentation | Swagger |
