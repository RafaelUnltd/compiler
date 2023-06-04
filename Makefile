create:
	rm -f Compiler.jar
	javac Compiler.java
	jar cf Compiler.jar *.class
	rm -rf *.class

list:
	jar tf Compiler.jar

run-test-1:
	java -cp Compiler.jar Compiler programs/test_1.txt

run-test-2:
	java -cp Compiler.jar Compiler programs/test_2.txt

run-test-3:
	java -cp Compiler.jar Compiler programs/test_3.txt

run-test-4:
	java -cp Compiler.jar Compiler programs/test_4.txt

run-test-5:
	java -cp Compiler.jar Compiler programs/test_5.txt

run-test-6:
	java -cp Compiler.jar Compiler programs/test_6.txt

run-test-7:
	java -cp Compiler.jar Compiler programs/test_7.txt

