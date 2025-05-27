# Jimmy Overdrive
**Jimmy Overdrive** is fast-paced platformer inspired by the popular Flash game *Johnny Upgrade*. Start underpowered, collect coins, buy upgrades, and push your limits to reach the final exit.

## How to Play
- Use **A** and **D** to move; space to jump.
- Press **W** to open doors.
- Collect coins to use on upgrades.
- Use the upgrades menu to improve your character's stats like speed, jump height, time limit, and more.
- Each attempt lets you go further.
- Reach the exit to win!

## Try it out (Prebuilt JAR)
### 1. Install [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or later
### 2. Go to the GitHub repo's [Releases](https://github.com/drew18moore/platformer-java/releases) tab
### 3. Download the latest JAR file (e.g. 'platformer-java-1.0.jar')
Note: The JAR file's name may vary. There may be two JAR files. Use the one **not** labeled `original-` 
### 4. Run the game (replace filename with actual filename if it differs):
```bash
java -jar platformer-java-1.0.jar
```

## Build and run locally
### 1. Install [Java JDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
### 2. Install [Maven](https://maven.apache.org/download.cgi)
### 3. Verify installations:
```bash
java -version
mvn -version
```
### 4. Clone the Repository
```bash
git clone https://github.com/drew18moore/platformer-java.git
```
### 5. Build and run the project
Note: The JAR file's name may vary. Maven may generate two JAR files. Use the one **not** labeled `original-`
```bash
cd platformer-java
mvn clean package
java -jar target/platformer-java-1.0-SNAPSHOT.jar
```

