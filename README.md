# ExtendedTCSC
This repository contains the source code to compute time and cost information for composed services and its component services.

Setup:
- Java SDK 17
- Maven 3
- JUnit 5
- XML Input: JAXB
- XLSX Output: Apache POI

Input: XML-files validating against schema provided under src\main\resources\TCP.xsd
- 1 XML-file = 1 tree to compute
- put files to run under src\main\resources\input
- examples can be found here: src\main\resources

Run:
- BEFORE: create src\main\resources\output directory!
- after placing input files run src\main\java\CalculateOverallTCMap.java

Output:
- overall TCMap as XLSX-file for each file in the input directory under src\main\resources\output

Experiments:
- Setup:
    - Windows 10 machine
    - 16 GB RAM

- input files: src\main\resources
- run src\main\java\ExperimentsMain.java
- each directory in src\main\resources represents a class with several files to test
- output file: each directory gets own sheet, while the files in it represent the row names for each sheet
- output: SpeedTestResults.xlsx
