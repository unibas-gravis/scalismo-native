#!/usr/bin/perl

# *** MUST SEE ***
# https://blogs.oracle.com/dipol/entry/dynamic_libraries_rpath_and_mac
# http://www.dribin.org/dave/blog/archives/2009/11/15/rpath/
# http://stackoverflow.com/questions/14656657/linking-a-dynamic-library-libjvm-dylib-in-mac-os-x-rpath-issue


# NOTE!
# This script isn't terribly robust (it won't cope with spaces in paths etc.)
# and the "trigger" is hard-coded. Moreover, return codes aren't checked,
# In other words: this is only an ad-hoc helper tool that was needed at
# some point during development. It may or may not be needed again.


# ANOTHER NOTE!
# This only runs correctly on Mac OS. And even then, you may have to
# manually install or update some of the required tools. At the time
# of writing, an installation of the "Command Line Tools (OS X Lion)"
# from https://developer.apple.com/downloads/index.action
# (which, of course, requires a registration with Apple)
# seems to be enough.


use strict;
use warnings;

my $TRIGGER = "/Users/statismo/";

my $OTOOL = "otool";
my $RENAME = "install_name_tool";

if ($#ARGV < 1) {
	print "Usage: $0 fromdir todir\n";
	exit 1;
}

my ($FROM, $TO) = @ARGV;

my @files = `ls $FROM/*.dylib|sort`;

foreach my $of (@files) {
	chomp $of;
	my $f = $of;
	$f =~ s|${FROM}|${TO}|;
	system("cp $of $f");
	

	my $self = `$OTOOL -D $f | tail -n 1`;
	chomp $self;
	print("\nWorking on: $f\n");
	if ($self =~ /^\s*$TRIGGER/) {
		my $rel = &relativate($self);
		print ("SELF: $self => $rel\n");
		system("$RENAME -id $rel $f");
	}

	my @others = `$OTOOL -L $f`;
	foreach my $dep (@others) {
		chomp($dep);
		$dep =~ s/^\s*(\S*)\s+.*$/$1/;
		if ($dep =~ /^\s*$TRIGGER/) {
			my $rdep = &relativate($dep);
			print "DEP:  $dep -> $rdep\n";
			system("$RENAME -change $dep $rdep $f");
		}
	}
	
}

exit;

sub relativate {
	my ($abs) = @_;
	if ($abs =~ /^$TRIGGER/) {
		my $rel = $abs;
		$rel =~ s|.*/||;
		$rel = '@rpath/'.$rel;
		return $rel;
	}
	return $abs;
}
