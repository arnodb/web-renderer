#!/bin/sh

set -e
set -x

FILE=$1
if [ -n "$FILE" ]
then
    BASE_NAME=$(echo $(basename "$FILE") | sed 's/^\([^\.]\+\)_\([^\.]\+\)\..*$/\1/')
    CHARSET=$(echo $(basename "$FILE") | sed 's/^\([^\.]\+\)_\([^\.]\+\)\..*$/\2/')
else
    exit 1
fi

if [ -z "$BASE_NAME" -o -z "$CHARSET" ]
then
    exit 2
fi

COMPOSITE_NAME="${BASE_NAME}_${CHARSET}"

WEB_RENDERER_JAR="../web-renderer-java/target/web-renderer-1.0.0-SNAPSHOT-jar-with-dependencies.jar"

mkdir -p wip
./gen.pl "$FILE" >| "wip/${COMPOSITE_NAME}.html"

rm -f "wip/${COMPOSITE_NAME}.png"
java -jar "$WEB_RENDERER_JAR" "file://$(pwd)/wip/${COMPOSITE_NAME}.html" -o "wip/${COMPOSITE_NAME}.png"

rm -Rf "wip/_${COMPOSITE_NAME}"
mkdir "wip/_${COMPOSITE_NAME}"
GEOMETRY_1=$(cat "wip/${COMPOSITE_NAME}.html" | sed -n 's/^.*<!-- Geometry: \([^ ]\+\) -->.*$/\1/p')
GEOMETRY_2="$(expr 300 \* $(echo $GEOMETRY_1 | sed 's/x[0-9]*$//'))x$(expr 200 \* $(echo $GEOMETRY_1 | sed 's/^[0-9]*x//'))"

convert -monitor -limit memory 2GB -limit map 2GB -limit area 2GB -crop "${GEOMETRY_2}+0+0" +repage -crop 300x200 "wip/${COMPOSITE_NAME}.png" "wip/_${COMPOSITE_NAME}/%d.png"

mkdir -p output
rm -Rf "output/${COMPOSITE_NAME}"
mkdir "output/${COMPOSITE_NAME}"
./rename.pl "wip/_${COMPOSITE_NAME}" "output/${COMPOSITE_NAME}" "$CHARSET" '' "$FILE"

