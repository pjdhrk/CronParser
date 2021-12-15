
## Building

Please use `./mvnw package` to compile and test application

## Running
To run application you need to install Java 16.

OpenJDK is enough.

Execute it with:

`java -jar Deliveroo-1.0.jar "minutes hours daysOfMonth months daysOfWeek command"`

where:

- `minutes, hours, daysOfMonth, months, daysOfWeek` are expressions like `*`, `0-5`, `1,2,3`, `5/10` 

- `command` is any string value