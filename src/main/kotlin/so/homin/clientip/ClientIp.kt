package so.homin.clientip

import java.net.InetAddress
import java.net.UnknownHostException

object ClientIp {

    private val HEADERS = arrayOf(
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR",
        "Z-Forwarded-For",
        "Source-IP",
        "X-Real-IP",
        "X-RealIP"
    )

    fun resolve(httpHeaders: Iterator<String>, remoteHostAddress: String?): String? {
        val locals = mutableListOf<String>()
        while(httpHeaders.hasNext()) {
            val header = httpHeaders.next()
            if (header.isNullOrEmpty()) {
                continue
            }
            val addresses = header.split(",").toTypedArray()
            for (address in addresses) {
                if (address.equals("unknown", ignoreCase = true)) {
                    continue
                }
                try {
                    InetAddress.getByName(address).let {
                        if (!it.isAnyLocalAddress && !it.isSiteLocalAddress && !it.isLoopbackAddress) {
                            return address
                        } else {
                            locals.add(address)
                        }
                    }
                } catch (e: UnknownHostException) {
                    // ignore
                }
            }
        }
        try {
            InetAddress.getByName(remoteHostAddress).let {
                if (!it.isAnyLocalAddress && !it.isSiteLocalAddress && !it.isLoopbackAddress) {
                    return remoteHostAddress
                }
            }
            if (locals.isNotEmpty()) {
                return locals[0]
            }
        } catch (e: UnknownHostException) {
            // ignore
        }
        return null
    }
}
