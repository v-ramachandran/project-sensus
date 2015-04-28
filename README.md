# project-sensus

__TODO__: Add more detail and make the README more clear.

### Running the Code

##### POS Tagger for data to generate sentences with superwords

The library is located under src/tagger, is written in Java, and is built with Gradle. The library offers the four following options:

+ __Psource__: The path (relative or absolute) to the source file which needs to be tagged. _Default:_ ```./data/english_test.source```
+ __Poutput__: The path (relative or absolute) to the output file which will store the results of the tagging. _Default:_ ```./data/english_test.output```
+ __Planguage__: The actual language to use when considering the __source__ file. ```ENGLISH``` and ```SPANISH``` are the only two currently supported parameters. _Default:_ ```ENGLISH```
+ __PuseUniversalTags__: A flag indicating whether to use universal POS tags or language-specific POS tags. Specify ```true``` to use universal POS tags and ```false``` otherwise. _Default:_ ```true```

The following command executes the library.

```
./gradlew executeTaggerMain
```

For example, to use the tagger on a data set located at ./some/where/data.set, output the results at ./some/other/place/results.output, use the SPANISH language, and disable universal POS tags, execute the following command:

```
./gradlew executeTaggerMain -Psource=./some/where/data.set -Poutput=./some/other/place/results.output -Planguage=SPANISH -PuseUniversalTags=false
```

The order of the options does not matter. For another example, to use the tagger on a data set located at ./some/where/data.set, output the results at ./some/other/place/results.output but with the ENGLISH language and universal POS tags, execute the following command:

```
./gradlew executeTaggerMain -Psource=./some/where/data.set -Poutput=./some/other/place/results.output -Planguage=ENGLISH -PuseUniversalTags=true
```

or the following, since using universal POS tags and the choice of ENGLISH as language are defaults:

```
./gradlew executeTaggerMain -Psource=./some/where/data.set -Poutput=./some/other/place/results.output ```
