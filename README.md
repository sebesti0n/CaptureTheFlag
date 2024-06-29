# Capture The Flag

## Overview

Capture The Flag is a full-stack mobile application developed for a treasure hunt game organized during a tech fest. The application uses Kotlin for the frontend and Node.js for the backend, with PostgreSQL as the database and Firebase Storage for storing media files. The game involves solving riddles to locate and scan QR codes placed around the campus.

## Features

- **QR Code Scanning**: Players scan QR codes to progress through the game.
- **Riddle Solving**: Each QR code is linked to a riddle that leads to the next QR location.
- **Cheat Prevention**: Unique paths are generated for each team using a graph algorithm.
- **Location Verification**: Ensures players are within 50 meters of the QR code using latitude and longitude.
- **Hint System**: Teams can access up to three hints, available every 30 minutes, each costing 50 points.
- **Points Deduction**: Deducts 10 points from a riddle's score for every 10 minutes it remains unsolved.
- **Balanced Travel Distance**: Ensures all teams have approximately equal travel distances.

## Tech Stack

- **Frontend**: Kotlin
- **Backend**: Node.js with Express
- **Database**: PostgreSQL
- **Storage**: Firebase Storage
- **Containerization**: Docker

## Installation and Setup

### Prerequisites

- Node.js
- PostgreSQL
- Docker
- Firebase account

### Backend Setup

1. **Clone the repository**:
    ```bash
    git clone https://github.com/sebesti0n/CaptureTheFlag.git
    cd CaptureTheFlag/Backend
    ```

2. **Create a `.env` file** in the `Backend` directory and add your PostgreSQL and Firebase credentials:
    ```env
    PORT=
    JWT_SECRET_KEY=
    TOKEN_HEADER_KEY=
    POSTGRES_USER=
    POSTGRES_PWD=
    POSTGRES_DB=
    DB_URL=<your_postgres_uri>
    ```

3. **Build and start the Docker containers**:
    ```bash
    docker-compose up --build
    ```

### Frontend Setup

1. **Open the project in Android Studio**:
    ```bash
    cd ../Frontend
    ```

2. **Configure Firebase**:
    - Add your Firebase configuration to the `google-services.json` file.

3. **Build and run the app** on an emulator or physical device.

## Game Logic

### Unique Paths Algorithm

To ensure each team has a unique path through the game, we implemented a graph algorithm. For a game requiring 8 riddles to be solved, the number of unique paths is \(4^8\).

### Location Verification

Each QR code is tagged with latitude and longitude coordinates. When a QR code is scanned, the app checks if the device is within 50 meters of the QR code's location. If the distance is within the threshold, the level is marked as solved.

### Hint System

If a team has difficulty finding the riddle, they can access up to three hints. A hint becomes available every 30 minutes after the riddle is opened. Each hint costs 50 points, which are deducted from the team's score.

### Points Deduction

The game includes an outstanding points system that deducts 10 points from the assigned points of a riddle for every 10 minutes the riddle remains unsolved.

### Balanced Travel Distance

The paths assigned to each team are designed to ensure that the total distance traveled by each team is approximately the same, providing a fair challenge for all participants.

## Contributions

Contributions are welcome! Please fork the repository and submit a pull request.

---

If you have any questions or need further assistance, please open an issue or contact the project maintainer.
