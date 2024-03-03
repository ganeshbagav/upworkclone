# Upwork Clone 🚀

Welcome to the Upwork Clone project! This repository contains the backend source code for a web application built using Spring Boot, REST API, and MySQL. The frontend part is located in a separate Git repository (link provided below) using React. Get ready to explore the exciting world of freelancing and online job marketplaces! 💼💻

## Project Overview

📝 The Upwork Clone project aims to replicate the functionalities of Upwork, providing a platform for clients and freelancers to connect and collaborate. It includes features such as messaging, notifications, job posting, and hiring freelancers.

## Features

✨ The Upwork Clone offers the following features:

1. **Messaging**: Clients and freelancers can communicate in real-time, discussing project details and exchanging messages to ensure smooth collaboration.

2. **Notifications**: Users receive notifications for important events, including new messages, proposal updates, and project milestones.

3. **Job Posting**: Clients can post job requirements, including project descriptions, skills required, and budgets.

4. **Hiring Freelancers**: Clients can browse freelancer profiles, review proposals, and hire the most suitable candidates for their projects.

5. **Payment Integration**: Integration with a secure payment gateway to facilitate financial transactions between clients and freelancers.

6. **User Authentication**: Secure sign-up and login functionality, ensuring the privacy and security of user accounts.

7. **Dashboard**: Provides an intuitive dashboard for users to manage their projects, proposals, and payments.

## Technologies Used

⚙️ The Upwork Clone project utilizes the following technologies:

- **Backend**: Spring Boot, WebSocket, REST API, MySQL
- **Frontend**: React (located in a separate repository: [Upwork Clone Frontend](https://github.com/your-username/upwork-clone-frontend))
- **Database**: MySQL for data persistence

## Getting Started

🚀 To get started with the Upwork Clone project, follow these steps:

## System Requirements
Before cloning and running the project, please ensure your system meets the following requirements:
- Java 17
- MySQL Server installed
- Database named "upwork" created in MySQL

## Cloning and Running the Project
To clone and run the backend project on your local machine, follow these steps:

1. Clone the repository to your local machine using the following command:
   ```
   git clone https://github.com/your-username/upwork-clone-backend.git
   ```

2. Navigate into the cloned repository:
   ```
   cd upwork-clone-backend
   ```

3. Edit the `application.properties` file located at `src/main/resources/application.properties`:
   - Replace `username` and `password` with your MySQL username and password to connect to the database.

4. Build the project using Maven:
   ```
   mvn clean install
   ```

5. Run the backend:
   ```
   mvn spring-boot:run
   ```

6. Access the application by visiting `http://localhost:8080` in your web browser.

## Frontend Repository
The frontend code for this project is located in a separate repository. You can clone and set up the frontend by following the instructions in its README file.

## Additional Notes
- Make sure your MySQL server is running before starting the backend.
- If you encounter any issues during the setup or running of the project, feel free to reach out to us for support.


Let's build the future of freelancing together! 💪💼💻✨