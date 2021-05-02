## 基于Jenkins构建产物的TestFlight

原理： 使用jenkins api，读取job列表，job详情
实现功能：
1. apk下载安装
2. job历史构建查看和安装

#### 账号
ApiManager.kt  

        private val BASE_URL = "jenkins_url"
        private val AUTHOR_TOKEN = "Basic cmVzdDpyZXN0MTIz"

