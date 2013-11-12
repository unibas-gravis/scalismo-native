#!/usr/bin/perl
use strict;
use warnings;

my $dir = $ARGV[0];

die "No such directory: $dir" unless -d $dir;
foreach my $f (`ls $dir|sort`) {
	chomp $f;
	next unless $f =~ /\.so$/;
	$f =~ s/^lib//;
	$f =~ s/.so$//;
	print "list.add(\"$f\");\n";
}
