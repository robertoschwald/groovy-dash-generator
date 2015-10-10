# Dash Documentation Generation

This project allows for you to generate documentation from project source. This project uses a modified groovydoc page to remove the navigation and keep the 'look and feel' the same across pages.

To build the tool, you will need to run `./gradlew shadowJar`. 

## Usage

    java -jar dash-<version>-all.jar <options> <src-directories>
    
### Options

* --output-dir \<dir\>   : Directory to create the docset in
* --name \<name\>        : Name of the docset
* --index \<index\>      : Index to start with
* src-directories        : Directories to search for source files

Simple example:

    java -jar build/dash/libs/dash-0.0.2-SNAPSHOT-all.jar --output-dir build/generated-docs --name foo dash/src/main/java

Running this will generate a file `build/generated-docs/foo.docset`, you can simply add this to Dash. 

Detailed example:

    java -jar build/dash/libs/dash-0.0.2-SNAPSHOT-all.jar --output-dir build/generated-docs/gradle3 --name gradle  --index ~/Downloads/gradle-2.5/docs/dsl  `find ~/Downloads/gradle-2.5/src -type d -maxdepth 1 -mindepth 1 -exec echo -n "{} " \;`

This will take about 8 min to run. The find command will get the source root of all the folders, making the dsl links work.

More to come...
