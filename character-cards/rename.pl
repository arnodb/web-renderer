#!/usr/bin/perl

use strict;

use Encode qw(decode);

my $input = shift or die;
my $output = shift or die;
my $charset = shift or die;
my $prefix = shift;
die if(!defined $prefix);

my $count = 0;
while(my $word = <>) {
    chomp $word;
    my $decoded_word = decode($charset, $word);
    system("cp \"${input}/${prefix}${count}.png\" \"${output}/${prefix}${decoded_word}.png\"");
    ++$count;
}

