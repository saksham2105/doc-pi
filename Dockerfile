# Use an official OpenJDK runtime as a parent image
FROM --platform=linux/amd64 openjdk:17-jdk-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file into the container at /app
COPY build/libs/docpi-0.0.1-SNAPSHOT.jar /app/docpi.jar

# Expose the port your app runs on
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "docpi.jar"]
