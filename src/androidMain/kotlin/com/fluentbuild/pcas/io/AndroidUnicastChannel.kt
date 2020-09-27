package com.fluentbuild.pcas.io

import com.fluentbuild.pcas.async.ThreadExecutor

class AndroidUnicastChannel(
    cipher: Cipher,
    executor: ThreadExecutor
): JvmUnicastChannel(cipher, executor)