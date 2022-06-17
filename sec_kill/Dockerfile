FROM java:8

COPY *.jar /lrl_app.jar

CMD ["--server.port=8080"]

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/seckill_app.jar"]