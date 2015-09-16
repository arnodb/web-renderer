# web-renderer

A fairly simple tool that uses the web browser (tested with Firefox) rendering power to create
complex and potentially big pictures. It is implemented in java and uses the
[Selenium](http://www.seleniumhq.org/) web driver to load any URL and convert the page to a PNG
file.

This tool coupled with [ImageMagick](http://www.imagemagick.org/) can mass generate tons of cards
very fast, see the scripts in [character-cards](character-cards) and a sample of what it can do:

![Arno.db](character-cards/samples/arnodb.png)

