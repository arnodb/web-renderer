#!/usr/bin/perl

use strict;

use File::Basename;

my $input = @ARGV[0] or die 'give me an input file';
my $charset = basename($input);
$charset =~ s/^[^_]*_([^\.]+)\.txt$/$1/;

my $columns = 5;

print <<EOF
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=$charset" />
        <style type="text/css">

EOF
;

system("cat cards-static.css");

print <<EOF
        </style>
    </head>
    <body>
EOF
;

print "        <div class=\"card_line\">\n";
my $count = 0;
while(my $word = <>) {
    chomp $word;
    if($count > 0 && $count % $columns == 0) {
        print "        </div><div class=\"card_line\">\n";
    }
    print <<EOF
        <div class="card">
            <table class="word"><tr><td>$word</td></tr></table>
            <div class="by_arnodb">by arnodb.web-renderer</div>
        </div>
EOF
    ;
    ++$count;
}
print "        </div>\n";

my $geometry = $columns.'x'.int(($count + $columns - 1) / $columns);

print <<EOF
    <!-- Geometry: $geometry -->
    </body>
</html>
EOF
;

