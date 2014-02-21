ATTENTION: This will probably only build (or rather: publish) properly on a Linux system!

Here's how:

umask 002
newgrp statismo
sbt publish
sbt publish-fixup
sh /export/contrib/statismo/push-repo.sh
