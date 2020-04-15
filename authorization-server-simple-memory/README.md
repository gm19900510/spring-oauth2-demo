# authorization-server-simple-memory
oauth2-授权服务-简单示例-内存存储

## 授权码模式

- 获取授权码

> http://localhost:8080/oauth/authorize?client_id=client&response_type=code
admin/admin

- 获取access_token

> http://localhost:8080/oauth/token?grant_type=authorization_code&code=KC2pLU&client_id=client&client_secret=secret

> 使用说明文档：https://blog.csdn.net/qq_33542154/article/details/89851150

## 客户端模式

- 获取access_token

> http://localhost:8080/oauth/token?grant_type=client_credentials&client_id=client&client_secret=secret