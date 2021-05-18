<h1>Basic Video Player Project</h1>

This is a basic video player. This uses the following tech stack:
- Java 11
- JavaFX SDK 11
- Spring Boot: This doesn't use Spring Boot per se, rather it uses the Spring Boot library but it's usage is more like the regular Spring MVC 

When coding in intellij:
1. For the main class, open Run > Edit Configurations
2. In VM Options add: --module-path=/<PATH TO JAVAFX>/javafx-sdk-11.0.2/lib --add-modules=javafx.base,javafx.graphics,javafx.controls,javafx.fxml,javafx.swing,javafx.web
3. Then you should be able to run the JAVAFX application.
4. Test it out
