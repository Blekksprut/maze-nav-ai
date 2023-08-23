:: Batch file for running Robot Maze Program

@echo OFF
del out\*.class
javac -d out\ src\*.java
java -cp out\ Main %1 %2