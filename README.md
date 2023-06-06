# Run the tests

```shell
mvn clean verify
```

# Generate the report

```shell
mvn surefire-report:report-only
```

After running the Surefire Report plugin, you can find the generated HTML report in the target/site/surefire-report.html file. Open this file in a web browser to view the test execution status.
