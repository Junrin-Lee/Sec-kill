## seckill

#### 介绍

基于springboot的并发秒杀订单系统

#### 项目简介

1. 前后端多次MD5与盐值加密保护数据
2. cookies和session校验实现登录验证
3. 统一异常处理+自定义异常
4. 页面静态化实现前后端分离
5. MQ异步下单
6. 统一日志管理
7. 根据IP实现接口限流
8. 随机秒杀路径保护秒杀接口
9. redis缓存实现秒杀预热
10. 并发下单

#### 环境搭建

1. IDE：IDEA 2022.1.3
2. JDK: 1.8.0_333
3. maven: 3.8.5
4. WebServer：Tomcat 9.56
5. DataBase：Mariadb 10.3
6. redis: 6.2.6
7. RabbitMQ: 3.9.11
8. docker
9. LAYUI MINI V2单页面