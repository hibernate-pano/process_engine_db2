# 多无人设备协同图形化预案配置与执行平台 - 后端

## 项目介绍

本项目是一个用于无人设备协同作业的图形化预案配置与执行平台，支持用户通过图形化界面设计预案流程，并能够实时监控和控制多种无人设备按照预案执行任务。

## 技术栈

- 后端：Spring Boot 3.x, Spring Data JPA, PostgreSQL
- 前端：Vue 3, Element Plus, G6 (图形可视化)
- 设备通信：MQTT, WebSocket

## 项目结构

```
src/main/java/com/example/process/
├── config          # 配置类
├── controller      # 控制器
├── engine          # 执行引擎
├── exception       # 异常处理
├── model           # 数据模型
│   └── common      # 通用模型
├── repository      # 数据访问层
├── service         # 业务逻辑层
└── util            # 工具类
```

## 核心功能模块

#### 1. 预案管理模块
- **流程定义管理**：创建、查询、更新和删除流程定义
- **流程版本管理**：支持流程版本控制，可以查看历史版本，发布新版本
- **流程图编辑**：提供图形化界面设计流程，支持拖拽操作

#### 2. 执行引擎模块
- **流程实例管理**：创建、启动、暂停、恢复和取消流程实例
- **节点执行**：支持多种节点类型的执行，如条件判断、设备动作、等待等
- **事件处理**：处理设备上报的各类事件，触发相应的流程执行
- **状态管理**：维护流程实例的状态和上下文变量
- **执行日志**：记录流程执行的详细日志，支持回溯和分析

#### 3. 设备管理模块
- **设备注册**：支持设备自动注册和手动添加
- **设备状态监控**：实时监控设备状态、位置和传感器数据
- **设备分组**：支持设备分组管理，便于批量操作
- **设备控制**：提供设备远程控制接口

#### 4. 用户和权限模块
- **用户管理**：用户注册、登录、信息管理
- **角色权限**：基于角色的权限控制
- **团队协作**：支持团队创建和管理，实现协同工作

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

## API接口

#### 预案管理接口
- `GET /api/flow-definitions`：获取流程定义列表
- `POST /api/flow-definitions`：创建流程定义
- `GET /api/flow-definitions/{id}`：获取流程定义详情
- `PUT /api/flow-definitions/{id}`：更新流程定义
- `DELETE /api/flow-definitions/{id}`：删除流程定义
- `GET /api/flow-versions`：获取流程版本列表
- `POST /api/flow-versions`：创建流程版本
- `GET /api/flow-versions/{id}`：获取流程版本详情

#### 执行引擎接口
- `POST /api/flow-instances`：创建流程实例
- `GET /api/flow-instances`：获取流程实例列表
- `GET /api/flow-instances/{id}`：获取流程实例详情
- `POST /api/flow-instances/{id}/start`：启动流程实例
- `POST /api/flow-instances/{id}/suspend`：暂停流程实例
- `POST /api/flow-instances/{id}/resume`：恢复流程实例
- `POST /api/flow-instances/{id}/cancel`：取消流程实例
- `POST /api/flow-events/trigger`：触发事件
- `POST /api/flow-events/execute-node`：执行节点
- `POST /api/flow-events/jump-to-node`：跳转到指定节点

## 执行引擎设计

#### 核心组件
1. **流程实例管理**：负责流程实例的生命周期管理
2. **节点执行器**：负责执行不同类型的节点
3. **事件处理器**：负责处理各类事件并触发流程执行
4. **状态管理器**：维护流程实例的状态和上下文变量

#### 数据模型
1. **流程实例(FlowInstance)**：表示一个正在执行的流程
2. **执行日志(FlowExecutionLog)**：记录流程执行的历史
3. **设备动作(DeviceAction)**：表示流程中的设备动作
4. **事件(FlowEvent)**：表示流程中的事件触发

#### 执行流程
1. 创建流程实例
2. 启动流程实例，从开始节点开始执行
3. 根据流程图定义，按照节点间的连接顺序执行各个节点
4. 执行过程中可以接收外部事件，触发相应的流程分支
5. 流程执行完成后，更新实例状态为已完成

#### 扩展性设计
1. **插件化节点执行器**：支持自定义节点类型和执行逻辑
2. **可配置事件处理器**：支持自定义事件类型和处理逻辑
3. **灵活的条件表达式**：支持复杂的条件判断和变量引用

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

## 部署指南

1. 打包应用：`mvn clean package`
2. 配置环境变量
3. 运行JAR文件：`java -jar target/process-engine.jar` 