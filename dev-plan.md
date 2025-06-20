project: Multi-Agent Preplan System
structure: dual-repo
description: >
  一个支持多无人设备协同的图形化预案配置与执行平台，包含前端流程编辑器、
  后端流程存储与执行引擎，支持条件判断与版本管理。

backend:
  framework: Spring Boot 3 + JDK 17
  database: PostgreSQL
  modules:
    - Preplan Management
      description: >
        支持流程图数据的增删改查，包含节点与边的数据结构，支持版本管理。
      apis:
        - POST /api/preplans               # 创建流程
        - GET /api/preplans/{id}          # 获取流程详情
        - PUT /api/preplans/{id}          # 更新流程
        - DELETE /api/preplans/{id}       # 删除流程
        - POST /api/preplans/{id/deploy}  # 发布流程
        - GET /api/preplans/{id}/history  # 获取历史版本
        - POST /api/preplans/{id}/rollback/{versionId} # 回滚指定版本

    - Execution Engine
      description: >
        执行流程图逻辑，包括事件驱动、节点判断、设备动作调度。
      components:
        - DispatcherService (流程调度器)
        - NodeRunner (各类型节点执行器)
        - ConditionEvaluator (JSON 表达式解析器)
        - ExecutionLog (执行记录)

frontend:
  framework: Vue 3 + Vite + TypeScript + TailwindCSS
  libraries:
    - Vue Flow (图形流程设计器)
    - VueUse / Pinia / Axios
  modules:
    - DesignerView
      description: >
        主流程设计器，包含拖拉拽节点、连线、属性面板、保存、发布。
      components:
        - FlowCanvas.vue
        - NodePalette.vue
        - NodeConfigPanel.vue

    - HistoryPanel
      description: >
        展示预案的历史版本，支持一键回滚。

features:
  - 节点类型：
    - event: 流程起点，由设备上报事件触发
    - judge: 判断条件，JSON 表达式结构体
    - action: 设备动作（指定设备 + 多操作）
    - delay: 等待指定时间
    - end: 结束流程
  - 连接线支持条件表达式（如判断为 true 才走某条边）
  - 节点属性由右侧面板配置
  - 流程保存为 JSON 存储在数据库中

flow_execution:
  strategy: lightweight_custom_engine
  logic:
    - 按照节点连线图调度，每个节点单独执行（支持异步）
    - 判断节点使用 JSON 表达式（预定义字段和运算符）
    - 动作节点模拟发送操作指令（后续可集成真实设备 SDK）

versioning:
  - 每次发布生成历史版本（包含完整节点/连线快照）
  - 支持基于版本 ID 的流程回滚操作