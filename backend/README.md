# 多无人设备协同图形化预案配置与执行平台 - 后端

## 项目介绍

本项目是一个支持多无人设备协同的图形化预案配置与执行平台的后端部分，使用Spring Boot 3和JDK 17开发。

## 技术栈

- Spring Boot 3.2.2
- JDK 17
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## 项目结构

```
src/main/java/com/example/process/
├── config          # 配置类
├── controller      # 控制器
├── exception       # 异常处理
├── model           # 数据模型
│   └── common      # 通用模型
├── repository      # 数据访问层
├── service         # 业务逻辑层
└── util            # 工具类
```

## 核心功能

1. 预案管理模块：支持流程图数据的增删改查，包含节点与边的数据结构，支持版本管理
2. 执行引擎：执行流程图逻辑，包括事件驱动、节点判断、设备动作调度

## 开发环境搭建

### 前提条件

- JDK 17
- Maven 3.8+
- PostgreSQL 14+

### 安装步骤

1. 克隆项目到本地
2. 配置PostgreSQL数据库
3. 修改`application.yml`中的数据库连接信息
4. 运行以下命令启动项目：

```bash
cd backend
mvn spring-boot:run
```

## API文档

启动项目后，可以通过以下URL访问API文档：

- 健康检查：`GET /api/health`

## 数据库设计

主要表结构：

- `flow_definition`：流程定义表，存储流程的基本信息
- `flow_version`：流程版本表，存储流程的版本信息和快照数据

## 开发规范

- 使用Lombok简化代码
- 使用统一的API响应格式
- 使用全局异常处理
- 遵循RESTful API设计规范 