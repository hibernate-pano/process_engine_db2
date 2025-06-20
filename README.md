# Multi-Agent Preplan Platform (Demo)

本项目旨在构建一个支持**多种无人设备协同控制**的图形化预案配置与执行平台，便于用户通过图形界面定义复杂设备联动逻辑，触发后自动调度多个设备完成任务。

---

## 🧠 项目背景

在多设备协同管理场景中，存在如下常见需求：

- 设备 A 感知某事件（如火情、入侵等）
- 根据预设规则判断是否需要响应
- 多个设备（如 B、C、D）按设定逻辑执行一系列动作（如：打开水泵、摄像头转向、发出警报）

为了降低配置复杂度、提升灵活性，我们开发此平台，支持**图形化流程设计 + 条件判断 + 自动化调度执行**。

---

## ✅ 当前开发目标（DEMO 阶段）

本阶段目标是构建 **可用 DEMO**，用于内部验证架构与产品方向。

### 支持功能
- [x] 基于 Vue Flow 的图形化流程设计器
- [x] 节点类型支持：事件、判断、设备动作、延时、结束
- [x] 条件判断使用结构化 JSON 表达式（非自然语言或代码）
- [x] 后端存储流程图结构（节点 + 连线）到 PostgreSQL
- [x] 后端提供保存、发布、历史版本、回滚接口
- [x] 自研轻量级执行引擎，模拟流程调度执行
- [x] 支持版本历史记录与流程回滚

### 非目标功能（暂不实现）
- ❌ 用户权限系统
- ❌ 多租户与企业级安全隔离
- ❌ 真正连接实际设备（目前为模拟指令发送）
- ❌ 自动化测试框架
- ❌ 高并发执行优化
- ❌ 国际化、多语言支持

---

## 🛠 技术选型

| 模块       | 技术                         |
|------------|------------------------------|
| 前端       | Vue 3 + Vite + TailwindCSS + Vue Flow |
| 后端       | Spring Boot 3 + Java 17      |
| 数据库     | PostgreSQL                   |
| 执行逻辑   | 自研轻量执行器（流程图驱动） |
| 表达式引擎 | JSON 表达式结构（用于判断） |

---

## 🗂 项目结构（双仓库）

- `preplan-frontend/` 前端：图形化流程编辑器
- `preplan-backend/` 后端：流程存储与执行调度服务

---

## 📌 注意事项

- 流程结构为有向图（非循环），支持多分支、多终点
- 条件表达式仅支持结构化 JSON，不执行字符串脚本
- 所有设备操作为模拟（Mock），后续可接入实际控制接口
- 此项目优先服务“预案流程配置与验证”，非即时设备通信平台

---

## ✨ 示例流程（用例）

```text
[事件: 温度感知] 
    → [判断: 温度 > 70 && 烟雾等级 = 高]
        → 是 → [动作: 喷水无人机执行 B1, B2]
              → [动作: 摄像头执行 C1, C2]
              → [结束]
        → 否 → [结束]

```
## 📧 联系与协作

若需参与开发或提供反馈，请联系架构负责人或使用公司内部协作平台提交讨论。