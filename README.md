# Qubership Testing Platform Common Auditing Library

## Purpose
Common Auditing Library is designed to perform Mongo Databases activities auditing.
It is used by Qubership Testing Platform RAM Service.

## Local build

In IntelliJ IDEA, one can select 'github' Profile in Maven Settings menu on the right, then expand Lifecycle dropdown of qubership-atp-common-auditing module, then select 'clean' and 'install' options and click 'Run Maven Build' green arrow button on the top.

Or, one can execute the command:
```bash
mvn -P github clean install
```

## How to add dependency into a service
```xml
    <!-- Change version number if necessary -->
    <dependency>
        <groupId>org.qubership.atp.common.auditing</groupId>
        <artifactId>qubership-atp-common-auditing</artifactId>
        <version>0.0.7-SNAPSHOT</version>
    </dependency>
```

In RAM Service, Common Auditing Library is used in Main.java, the following way:
1. Annotations are imported 
```java
import org.qubership.atp.common.auditing.annotation.EnableAtpAuditing;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
```

2. Path-to-library-root is added to @SpringBootApplication annotation
```text
@SpringBootApplication(exclude = MongoAutoConfiguration.class, scanBasePackages = {
        "org.qubership.atp.ram",
        "org.qubership.atp.common.probes.controllers",
        "org.qubership.atp.common.auditing"
})
```

3. Annotations are added
```text
@EnableMongoAuditing
@EnableAtpAuditing
```

This auditing is needed in RAM Service, because it stores and retrieves log records to/into Mongo Database,
so there is a task to track user activity against Mongo Database, the same way as it is tracked in other Qubership 
Testing Platform Services (for example, Environments Service, ITF-Executor Service, ITF-Lite Service) against 
PostgreSQL databases they use. 