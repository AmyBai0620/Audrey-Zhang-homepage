# Audrey Zhang 教授个人主页项目

这是我母亲（贵州财经大学副教授）的学术个人主页网站。

## 技术栈

- **后端**: Spring Boot 3.0.2
- **数据库**: PostgreSQL
- **模板引擎**: Thymeleaf
- **持久化**: Spring Data JPA
- **构建工具**: Maven
- **JDK版本**: 17

## 开发环境配置

### 1. 安装必要软件

- JDK 17
- PostgreSQL（默认端口5432）
- IntelliJ IDEA 2025
- Git

### 2. 数据库配置

#### 创建数据库
```bash
psql -U postgres -c "CREATE DATABASE audrey_homepage;"
```

#### 配置数据库连接
1. 复制 `src/main/resources/application-dev.properties.template`
2. 重命名为 `application-dev.properties`
3. 修改其中的数据库密码
4. 此文件不会被提交到Git（已在.gitignore中）

### 3. 运行项目

#### 在IntelliJ IDEA中运行
1. 打开项目
2. 等待Maven下载依赖
3. 找到 `AudreyZhangHomepageApplication.java`
4. 点击左侧的绿色运行按钮
5. 访问 http://localhost:8080

#### 使用Maven命令运行
```bash
mvn spring-boot:run
```

## Git工作流程

### 提交代码
```bash
# 查看状态
git status

# 添加文件
git add .

# 提交
git commit -m "提交说明"

# 推送到GitHub
git push origin main
```

### 注意事项
- `application-dev.properties` 包含数据库密码，不会被提交到Git
- 每次修改代码后要记得提交

## 项目结构

```
src/
├── main/
│   ├── java/com/audrey/homepage/
│   │   ├── AudreyZhangHomepageApplication.java  # 启动类
│   │   ├── entity/      # 实体类（数据库表对应的Java类）
│   │   ├── repository/  # 数据访问层（操作数据库）
│   │   ├── service/     # 业务逻辑层
│   │   └── controller/  # 控制器层（处理HTTP请求）
│   └── resources/
│       ├── application.properties           # 主配置文件
│       ├── application-dev.properties       # 开发环境配置（不提交）
│       ├── static/      # 静态资源（CSS, JS, 图片）
│       └── templates/   # HTML模板
└── test/                # 测试代码
```

## 学习目标

通过这个项目，你将学会：

1. ✅ Spring Boot项目的创建和配置
2. ✅ Git版本控制的基本使用
3. ✅ PostgreSQL数据库的配置和连接
4. ⏳ JPA实体类和数据库表的映射
5. ⏳ Repository层的数据操作
6. ⏳ Service层的业务逻辑
7. ⏳ Controller层的HTTP请求处理
8. ⏳ Thymeleaf模板的使用
9. ⏳ 前端页面的开发（HTML/CSS/JavaScript）
10. ⏳ 项目的测试和部署

## 作者

- 开发者：你的名字（南方科技大学 智能科学与技术专业 大二学生）
- 指导：Claude Code
- 时间：2026年1月

---

**注意**：这个项目用于学习目的，帮助巩固Computer System Design and Applications课程中学到的Spring Boot、多线程等知识。
