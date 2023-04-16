run:
	rm -rf ./build
	mkdir -p build
	javac -d ./build src/Compiler.java
	jar cvf ./build/Compiler.jar ./build/*
	rm -rf ./build/src
