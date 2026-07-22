SUMMARY = "wolfSSL Lightweight Embedded SSL/TLS Library"
DESCRIPTION = "wolfSSL, formerly CyaSSL, is a lightweight SSL library written \
               in C and optimized for embedded and RTOS environments. It can \
               be up to 20 times smaller than OpenSSL while still supporting \
               a full TLS client and server, up to TLS 1.3"
HOMEPAGE = "https://www.wolfssl.com/products/wolfssl"
BUGTRACKER = "https://github.com/wolfssl/wolfssl/issues"
SECTION = "libs"

# Commercial/FIPS wolfSSL license
LICENSE = "LicenseRef-WolfSSL-LicenseAgmt-JAN-2024"
LICENSE_FLAGS = "commercial"
LIC_FILES_CHKSUM = "file://COPYING;md5=1ef7f03f8795f8e0c66f21a896ddb0f5"

PROVIDES += "cyassl"
RPROVIDES:${PN} = "cyassl"

SRC_URI = " \
    https://maven.pkg.github.com/nedap/nsm-aepu-build/com/nedap/nsm-aepu-build/${BPN}/${PV}/${BP}.tar.gz;subdir=${BP} \
    file://run-ptest \
"

SRC_URI[sha256sum] = "413da75248d14e4bd249ad82ad7523600f4bf44d67f7eced0e43e0d9220ef6c5"

FETCHCMD_wget = "/usr/bin/env wget -t 2 -T 100 --config=${BB_FETCH_GHRA_CONFFILE}"
do_fetch[vardepsexclude] += "BB_FETCH_GHRA_CONFFILE"

inherit autotools ptest

EXTRA_OECONF += "--enable-certreq --enable-dtls --enable-opensslextra --enable-certext --enable-certgen"

PACKAGECONFIG ?= "reproducible-build"

PACKAGECONFIG[reproducible-build] = "--enable-reproducible-build,--disable-reproducible-build,"
BBCLASSEXTEND += "native nativesdk"

CFLAGS += '-fPIC -DCERT_REL_PREFIX=\\"./\\"'

RDEPENDS:${PN}-ptest += " bash"

do_install_ptest() {
    # Prevent QA Error "package contains reference to TMPDIR [buildpaths]" for unit.test script
    # Replace the occurences of ${B}/src with '${PTEST_PATH}'
    sed -i 's|${B}/src|${PTEST_PATH}|g' ${B}/tests/unit.test

    install -d ${D}${PTEST_PATH}/test

    # create an empty folder examples, needed in wolfssl's tests/api.c to "Test loading path with no files"
    install -d ${D}${PTEST_PATH}/examples
    cp -rf ${B}/tests/. ${D}${PTEST_PATH}/test
    cp -rf ${S}/certs  ${D}${PTEST_PATH}
    cp -rf ${S}/tests  ${D}${PTEST_PATH}

    # Remove symlinks pointing to build directory (symlink-to-sysroot QA)
    find ${D}${PTEST_PATH} -type l -delete
}
