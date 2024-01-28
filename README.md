# Brainfuck JVM Complier
This is a compiler that using Gradle compile-time tasks to compile BrainFuck code into equivalent Kotlin code. This allows you to compile and run BrainFuck code on the JVM platform

# Usage
### 1.Preparation
Clone this repository to your local environment, then
modify the gradle.properties file by changing the "brainfuckFileName" entry to the name of your BrainFuck code file.

### 2. Compile
Put your BrainFuck code file in the resources dictionary, The following is an example BrainFuck code that will print "Hello World!" in console:
```
++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>++.>+.+++++++..+++.<<++.>+++++++++++++++.>.+++.------.--------.<<+.<.
```
The code file will also be included in the project by default.

Then, Execute the following gradle task to get the compiled Jar file:

```
gradle build
```

After that, you will find the Jar file in "build/lib" file.
