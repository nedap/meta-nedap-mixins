SUMMARY = "Nedap mainline stable Linux kernel"

inherit kernel
require recipes-kernel/linux/linux-yocto.inc

# CVE exclusions
include recipes-kernel/linux/cve-exclusion.inc

BRANCH = "linux-6.18.y"
LINUX_VERSION = "6.18.52"
LINUX_VERSION_EXTENSION:append = "-nedap"

SRCREV_machine = "8f3741e6feb045da5b406df0a80b42a1adfb289b"
SRCREV_meta = "bf8faf1b184fcf6c555ee951f501e76f88eccf35"
KMETA = "kernel-meta"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"
PV = "${LINUX_VERSION}+git${SRCPV}"
PR = "r1"

SRC_URI = " \
  git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux.git;name=machine;protocol=https;branch=${BRANCH} \
  git://git.yoctoproject.org/yocto-kernel-cache;protocol=https;type=kmeta;name=meta;branch=yocto-6.18;destsuffix=${KMETA} \
"

COMPATIBLE_MACHINE = "^(ax8008|ax8010)$"
