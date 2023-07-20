# famark-cloud-api-java-example
This is a sample code showing how to call Famark Cloud API to store and retrieve data through Java programming language.

## Compiling the Java Code
Open the integrated terminal in Visual Studio Code (View > Terminal or press Ctrl+~).
Use the cd command to navigate to your project's root directory.

### Command to Compile:

javac -cp .\lib\json-simple\json-simple-1.1.1.jar .\src\App.java

#### Explanation of command options:

-cp: Specifies the classpath, i.e., the location of external libraries or dependencies.
.\lib\json-simple\json-simple-1.1.1.jar: Path to the json-simple-1.1.1.jar library file.
.\src\App.java: Path to the main class file (App.java).

## Running the Java Code
After successfully compiling the code, you can run the Java application using the following command:

### Command to Run:

java -cp .\lib\json-simple\json-simple-1.1.1.jar;.\src App

#### Explanation of command options:

-cp: Specifies the classpath, i.e., the location of external libraries or dependencies. Here, we include both the json-simple library and the src directory where the compiled .class files are located. Separate the classpath entries with a semicolon (;) on Windows, or a colon (:) on macOS/Linux.

App: The name of the main class (without the .java extension) you want to run. Make sure it matches the actual class name used in your App.java file.

### Additional Notes <br>
Make sure to save your changes in the Java source file before compiling or running the code.
Double-check that you have the correct file paths and package structure (if applicable) in your Java source code.
With these steps, you should be able to compile and run your Java code in Visual Studio Code using the specified json-simple library and the main class file, App.java. Happy coding!
