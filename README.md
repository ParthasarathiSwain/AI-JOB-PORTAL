# AI JOB PORTAL 🌐🤖✨

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.1.2-green)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue)](https://www.mysql.com/)
[![JWT](https://img.shields.io/badge/JWT-Authentication-yellow)](https://jwt.io/)
[![OpenAI](https://img.shields.io/badge/OpenAI-Integration-orange)](https://openai.com/)
[![Resume Parser](https://img.shields.io/badge/Resume_Parser-Enabled-red)]()

---

## **Project Overview** 🌟

AI Job Portal is a **modern backend application** built with **Spring Boot and MySQL**.  
It connects **job seekers and recruiters** while providing **AI-based job recommendations** using OpenAI.  

The platform ensures **secure role-based login**, **JWT authentication**, and a **feature-rich experience** for users, recruiters, and admins.  
Additionally, it includes a **resume parser** feature to extract and analyze candidate resumes automatically. 📄🤖

---

## **Technology Stack** 💻

* **Backend:** Java 17, Spring Boot, Maven
* **Database:** MySQL
* **Authentication:** JWT (JSON Web Token)
* **AI Integration:** OpenAI API
* **Resume Parser:** Apache POI / OpenAI NLP
* **Other:** RESTful APIs, Lombok, Spring Data JPA

---

## **Project Structure** 📂

```
AI-JOB-PORTAL
│
├─ src/main/java/com/otz
│  ├─ config        # Security & JWT configuration
│  ├─ controller    # REST controllers (User, Job, Application, Resume)
│  ├─ dto           # Request/Response DTOs
│  ├─ model         # Entity classes (User, Job, Application)
│  ├─ repository    # Spring Data JPA repositories
│  └─ service       # Business logic, OpenAI & Resume Parser integration
│
├─ src/main/resources
│  ├─ application.yml   # DB, JWT & API key configs
│  └─ data.sql          # Optional initial data
│
├─ pom.xml              # Maven dependencies
└─ README.md            # Project documentation
```

---

## **Key Features** 🚀

### **User Management** 👤
* Registration & login
* Role-based access
* Profile update
* Password reset via email 📧
* Email verification on signup ✅

### **Job Management** 💼
* Add / update / delete jobs
* View all jobs
* Search & filter jobs by category, location, and skills
* AI-based job recommendation for users 🤖
* Pagination and sorting support

### **Application Management** 📝
* Apply for jobs
* View applied jobs
* Recruiters can view all applications
* Application status updates (Pending, Shortlisted, Rejected)
* Download applicant resumes
* **Resume Parser** to extract candidate information automatically 📄✨

### **Security** 🔒
* JWT authentication
* Role-based access control
* Environment variable for sensitive keys
* Input validation & sanitization
* HTTPS support ready

---

## **API Endpoints** 📡

| Module      | Endpoint                  | Method | Role       |
| ----------- | ------------------------- | ------ | ---------- |
| User        | `/api/auth/register`      | POST   | Public     |
| User        | `/api/auth/login`         | POST   | Public     |
| User        | `/api/user/profile`       | GET    | User/Admin |
| User        | `/api/user/profile`       | PUT    | User/Admin |
| Job         | `/api/jobs`               | GET    | All        |
| Job         | `/api/jobs`               | POST   | Recruiter  |
| Job         | `/api/jobs/{id}`          | PUT    | Recruiter  |
| Job         | `/api/jobs/{id}`          | DELETE | Recruiter  |
| Application | `/api/applications`       | POST   | Job Seeker |
| Application | `/api/applications/user`  | GET    | Job Seeker |
| Application | `/api/applications/{id}`  | PUT    | Recruiter  |
| AI          | `/api/ai/recommendations` | GET    | Job Seeker |
| Resume      | `/api/resume/parse`       | POST   | Recruiter  |

> **Note:** All endpoints are secured with JWT; role-based access control is implemented.

---

## **Setup Instructions** 🛠️

1. **Clone repository**
```bash
git clone https://github.com/ParthasarathiSwain/AI-JOB-PORTAL.git
cd AI-JOB-PORTAL
```

2. **Database Setup**
* Create MySQL database: `AI_JOB_PORTAL`
* Update `application.yml` or environment variables

3. **Environment Variables**
```bash
export OPENAI_API_KEY="your_api_key_here"
```

4. **Build & Run**
```bash
mvn clean install
mvn spring-boot:run
```

5. **Test API**
* Use **Swagger UI** or **Postman**

---

## **Security Note** ⚠️
* Never commit API keys
* Use environment variables for secrets
* HTTPS recommended in production

---

## **Screenshots / Demo** 📸
*(Add Swagger UI, Postman, or project screenshots here with colorful highlights)*

---

## **License** 📝
MIT License

---

## **Author** 👨‍💻
**Parthasarathi Swain – Full Stack Java Developer**
📧 **Email**: rajaswain6969@gmail.com
**LinkedIn**: [https://www.linkedin.com/in/parthasarathiswain](https://www.linkedin.com/in/parthasarathiswain)

