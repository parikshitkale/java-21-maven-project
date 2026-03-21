# -------- Base Image  --------
FROM eclipse-temurin:21-jdk-alpine


# -------- Create non-root user --------
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# -------- Set working directory --------
WORKDIR /app

# -------- Copy artifact --------
COPY target/java*.jar app.jar

# -------- Set permissions --------
RUN chown -R appuser:appgroup /app

# -------- Switch to non-root user --------
USER appuser

# -------- Expose port --------
EXPOSE 8080


# -------- Entry Point --------
ENTRYPOINT ["java", "-jar", "app.jar"]

