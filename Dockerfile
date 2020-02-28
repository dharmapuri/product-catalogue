FROM openjdk:8
ADD /target/product.jar product.jar
ADD /infrastructure/startup.sh /apps/startup/startup.sh
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "product.jar"]
# ENTRYPOINT ["/apps/startup/startup.sh"]