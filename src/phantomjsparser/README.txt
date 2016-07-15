Скачайте и разархивируйте PhantomJS с сайта http://phantomjs.org/download.html
Для настройки phantomJS и selenium  можно использовать maven:
    создайте в директории проекта файл pom.xml:
    
    <?xml version="1.0" encoding="UTF-8"?>
    <project xmlns="http://maven.apache.org/POM/4.0.0"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
                     http://maven.apache.org/xsd/maven-4.0.0.xsd">
        <modelVersion>4.0.0</modelVersion>
        <groupId>MySel20Proj</groupId>
        <artifactId>MySel20Proj</artifactId>
        <version>1.0</version>
        <dependencies>
            <dependency>
                <groupId>org.seleniumhq.selenium</groupId>
                <artifactId>selenium-support</artifactId>
                <version>2.53.1</version>
            </dependency>
            <dependency>
                <groupId>com.codeborne</groupId>
                <artifactId>phantomjsdriver</artifactId>
                <version>1.2.1</version>
            </dependency>
        </dependencies>
    </project>
    
    запустите maven командой mvn clean install в директории проекта

    Для импорта проекта в eclipse выполните mvn eclipse:eclipse

    Импортируйте проект в eclipse

    Задайте путь до bin phantomjs на вашем компьютере в переменной phantombinary

