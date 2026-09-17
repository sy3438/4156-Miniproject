Run the application:
- cd IndividualProject
- mvn spring-boot:run

Run tests:
- mvn test

Install and run PMD:
- brew install pmd
- pmd check -d src/main/java -R rulesets/java/quickstart.xml -f text
- results are displayed in the terminal with the issues found and the locations.
