1. run the cmd 
   ```shell
   ./gradlew clean assemble
   ```
   
2. run the cmd 
    ```shell
     java -jar build/libs/starter-1.0.0-SNAPSHOT-fat.jar
   ```
   
3. check the process identifier
    ```shell
   lsof -nP -iTCP:8888 -sTCP:LISTEN
   ```