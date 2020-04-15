# authorization-server-simple-jdbc

oauth2-授权服务-简单示例-JDBC Token存储

## 功能项

- 简单的管理页面
- 自定义登录页面
- 自定义授权页面
- 支持多授权模式
- Token JDBC存储
- Token 可执行刷新token
- 权限控制

## 获取授权码模式
- 获取授权码

> http://localhost:8080/oauth/authorize?client_id=client_one&response_type=code
admin/admin

- 获取access_token

> http://localhost:8080/oauth/token?grant_type=authorization_code&code=gsNDpJ&client_id=client_one&client_secret=secret

- 刷新token

> http://localhost:8080/oauth/token?grant_type=refresh_token&refresh_token=2fb73a99-def8-49c4-a51f-8d962491aa01&client_id=client_one&client_secret=secret

- 使用说明文档

> https://blog.csdn.net/qq_33542154/article/details/89851150

## 客户端模式
- 获取access_token

> http://localhost:8080/oauth/token?grant_type=client_credentials&client_id=client_one&client_secret=secret







 
