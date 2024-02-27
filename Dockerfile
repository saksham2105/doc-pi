# Use an official OpenJDK runtime as a parent image
FROM --platform=linux/amd64 amazoncorretto:17.0.7-alpine

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file into the container at /app
COPY target/docpi-0.0.1-SNAPSHOT.jar /app/docpi.jar

# Expose the port your app runs on

# Specify the command to run on container start
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]