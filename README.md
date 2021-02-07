# ClientIP
Resolves client's IP address from proxy headers

## How to use

```
val clientIp = ClientIp.resolve(serverHttpRequest.headers, serverHttpRequest.remoteAddress?.address?.hostAddress) 
```
