# message-notification-demo
> 消息通知是一个企业内部常用的应用功能，本例是钉钉企业内部H5微应用。架构形态是一个Java单体应用，用户在页面上点击**上传媒体文件**按钮上传一张图片到钉钉demo中把图片放在了服务端，前端只需要调用接口就可以了；之后用户在页面点击**发送群消息**发送一条消息到钉钉客户端，点击**查看已读人员列表**可以查询已经读取该条消息的人员
>
> 包含功能：

- 上传媒体文件：上传一个图片到钉钉，在创建群链接消息时需要用到图片；
- 发送群消息：创建一个群会话，然后发送一条链接消息，消息链接的打开方式在**PC端侧边栏打开**
- 查看已读人员列表：查询已经读取该条消息的userId；

## Getting Started



### 克隆代码仓库到本地

git clone

```
https://github.com/open-dingtalk/message-notification-demo.git
```



### 开发环境准备

#### 钉钉开放平台环境准备

1. 需要有一个钉钉注册企业，如果没有可以创建：https://oa.dingtalk.com/register_new.htm?source=1008_OA&lwfrom=2018122711522903000&succJump=oa#/

2. 成为钉钉开发者，参考文档：https://developers.dingtalk.com/document/app/become-a-dingtalk-developer

3. 登录钉钉开放平台后台创建一个H5应用： https://open-dev.dingtalk.com/#/index

4. 配置应用

   配置开发管理，参考文档：https://developers.dingtalk.com/document/app/configure-orgapp

   ![image-20210701133227848](https://img.alicdn.com/imgextra/i1/O1CN01eZ2kHr26eEqlgp7Ox_!!6000000007686-2-tps-2862-1044.png)

   配置免登相关权限：https://developers.dingtalk.com/document/app/address-book-permissions

   ![image-20210701133333027](https://img.alicdn.com/imgextra/i2/O1CN015Al3Bm1wxnbiUZ6da_!!6000000006375-2-tps-2852-1310.png)

   配置获取部门用户详情权限
   
   ![image-20210701133457695](https://img.alicdn.com/imgextra/i1/O1CN01m2JIki1IOSPsttdZr_!!6000000000883-2-tps-2872-1208.png)
   
   配置文件参数：agentId、appKey、appSecret、corpId
   
   ![image-20210701133718816](https://img.alicdn.com/imgextra/i3/O1CN01cTh6fU1lPwYgtTxru_!!6000000004812-2-tps-2794-1226.png)
   
   ![image-20210701133842908](https://img.alicdn.com/imgextra/i4/O1CN01Ns2Oey1xPHN6Z870f_!!6000000006435-2-tps-2856-1076.png)
#### 使用命令行安装依赖&打包

```txt
cd fronted/
```

![image-20210701135749459](https://img.alicdn.com/imgextra/i1/O1CN01NF9xeP1fYI5Boml5K_!!6000000004018-2-tps-2658-686.png)

```txt
npm install
```


![image-20210701140008585](https://img.alicdn.com/imgextra/i1/O1CN01V4OYCz1kKcKqXrpti_!!6000000004665-2-tps-2336-1100.png)

```txt
npm run build
```


![image-20210701140123101](https://img.alicdn.com/imgextra/i4/O1CN01h1FA2s28m8uydeNYH_!!6000000007974-2-tps-2234-1142.png)

#### 将打包好的静态资源文件放入后端服务

![image-20210701140225679](https://img.alicdn.com/imgextra/i1/O1CN01cv0kz81xVgutF1Tmm_!!6000000006449-2-tps-2208-1152.png)

### 启动项目-移动端钉钉访问

#### 首页

<img src="https://img.alicdn.com/imgextra/i4/O1CN01FUanNd1Z9Ch4OL0kE_!!6000000003151-0-tps-282-720.jpg" />

#### PC端钉钉接收消息

![image-20210701141353604](https://img.alicdn.com/imgextra/i4/O1CN01R3zOIE29kbCMW63Qi_!!6000000008106-2-tps-1370-950.png)

#### 点击消息侧面栏打开

![image-20210701141212131](https://img.alicdn.com/imgextra/i1/O1CN013cHnMO1g4oCbomzoF_!!6000000004089-2-tps-1430-1272.png)

### 参考文档

1. 获取企业内部应用access_token，文档链接：https://developers.dingtalk.com/document/app/obtain-orgapp-token?spm=ding_open_doc.document.0.0.938247e54bE13v#topic-1936350
2. 消息类型与数据格式，demo使用的是链接消息 文档链接：https://developers.dingtalk.com/document/app/message-types-and-data-format?spm=ding_open_doc.document.0.0.53f91b437GQYSn#topic-1945727
3. 消息链接说明，文档链接：https://developers.dingtalk.com/document/app/message-link-description?spm=ding_open_doc.document.0.0.220c5f665O4TKT#topic-1945743
4. 上传媒体文件，文档链接：https://developers.dingtalk.com/document/app/upload-media-files?spm=ding_open_doc.document.0.0.220c5f66kJ90DQ#topic-1936786
5. 群会话接口相关权限，文档链接：https://developers.dingtalk.com/document/app/group-message-apply-for-permissions?spm=ding_open_doc.document.0.0.71c7784cnculMw#topic-1997496
6. 创建群会话，文档链接：https://developers.dingtalk.com/document/app/create-group-session#topic-1936767
7. 发送群消息，文档链接：https://developers.dingtalk.com/document/app/send-group-messages
8. 获取已读人员列表，文档链接：https://developers.dingtalk.com/document/app/queries-the-list-of-people-who-have-read-a-group

