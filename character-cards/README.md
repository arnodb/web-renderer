# Fast and automatic word cards generator

This bunch of scripts generates cards based on input files. It is very fast and does not require
interaction once the main script is launched.

![Arno.db](samples/arnodb.png)

## Prerequisite

In order to generate cards you need to build the web-renderer package. The source code is in `../web-renderer-java`:

```
$ cd ../web-renderer-java
$ mvn package
$ cd -
```

## Run it

On all input files:

```
$ for f in input/*; do ./gen.sh $f; done
```

On a single file:

```
$ ./gen.sh input/I-am-chinese_UTF-8.txt
```

The input files can be somewhere else but the output always goes to the `output` directory.

The input file name format must be of the form `<name>_<charset>.<ext>`.

The magic is supposed to generate file names in the charset of your filesystem.

This is a very simple use of web-renderer and ImageMagick, a more advanced script may generate more
advanced cards!

