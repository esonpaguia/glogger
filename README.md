# GLogger Utility

Logs you in GLogger.


## Repository

```
git clone https://github.com/esonpaguia/glogger.git
```


## Requisites

  * [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [Firefox](https://www.mozilla.org/en-US/firefox/new/)



## Build

```
mvn assembly:assembly
```


## Usage

Syntax:
```
java -jar glogger-jar-with-dependencies.jar [CONFIG_FILE_PATH]
```

Example: 
```
java -jar glogger-jar-with-dependencies.jar config.properties
```


## Configuration

  * toCloseWindow - to close or not to close the initialized browser window. 


## Task Scheduler

  * Program: 
  <pre>
  C:\Program Files (x86)\Java\jdk1.8.0_60\bin\java.exe
  </pre>
  * Add arguments: 
  <pre>
  -jar "C:\Users\eson\workspace\glogger\target\glogger\glogger.jar" "C:\Users\eson\workspace\glogger\target\glogger\config.properties"
  </pre>
  * Start in: 
  <pre>
  C:\Users\eson\workspace\glogger\target\glogger
  </pre>


## To do

- [ ] Figure out check out, meal in and meal out
- [ ] Set proxy IP address
