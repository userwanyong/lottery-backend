# 轻量版部署说明

本分支（`feature/lightweight`）是营销抽奖系统的**轻量版本**，在保留全部业务功能的前提下，移除了 ES、RabbitMQ、分库分表、Zookeeper、xxl-job、Hystrix 等中间件，使部署依赖大幅精简。

## 一、依赖中间件

轻量版仅需：

| 中间件 | 用途 | 是否必须 |
|---|---|---|
| **MySQL 8.x** | 单库 `marketing_lite` 主存储 | 必须 |
| **Redis** | 缓存 / 分布式锁 / 库存扣减 / 延迟队列 / refresh token | 必须 |
| **Nacos** | 仅作为 Dubbo 注册中心，连接外部 auth-service 完成登录鉴权 | 必须（鉴权链路保留） |

> 已移除：~~Elasticsearch~~、~~RabbitMQ~~、~~分库分表 db-router~~、~~Zookeeper~~、~~xxl-job~~、~~Hystrix~~

## 二、初始化数据库

执行单库建表脚本（含全局表业务数据 + 分库表合并为单表 + sys_config 配置表）：

```bash
mysql -u root -p < docs/dev-ops/mysql/sql/marketing_lightweight.sql
```

该脚本会：
1. 创建 `marketing_lite` 单库；
2. 建立全局表（activity / award / strategy / rule 等，含初始化业务数据）；
3. 将原 `marketing_01` / `marketing_02` 的分库分表（`*_000~003`）合并为同名单表；
4. 建立 `sys_config` 动态配置表并预置两个开关（`degradeSwitch=close`、`rateLimiterSwitch=open`）。

## 三、配置环境

复制 `lottery-app/.env.example` 为 `.env`（项目根目录或 `lottery-app/` 下），填入 MySQL / Redis / Nacos / Auth / OSS 的实际连接值。

## 四、启动

```bash
mvn clean install -DskipTests
mvn spring-boot:run -pl lottery-app
```

- 服务端口：`8091`
- context-path：`/api/v1`

## 五、相对原版的主要变更

| 中间件 | 原版用途 | 轻量版替代方案 |
|---|---|---|
| 分库分表 db-router | 按 userId 分 2 库 4 表 | 单库单表（`marketing_lite`），MyBatis-Plus + HikariCP 单数据源 |
| Elasticsearch | 运营后台 8 个只读查询 | 直接查 MySQL 单表（去分库分表后无需跨库聚合） |
| RabbitMQ | 8 个 topic + task 表补偿 | task 表本地分发：`SendMessageTaskJob` 按 topic 路由到对应 Customer；通知类（库存归零、删缓存）改同步调用 |
| Zookeeper | DCC 动态配置（2 个开关） | `sys_config` 数据库表 + `/dcc/update_config` 接口热更新（`ConfigService` 反射注入 `@DCCValue` 字段） |
| xxl-job | 3 个定时任务 | Spring `@Scheduled`（奖品库存 2min / SKU 库存 1min / 消息补偿 2min），保留 Redisson 分布式锁 |
| Hystrix | 熔断（仅 `@EnableHystrix`，零实际使用） | 移除 |

## 六、动态配置开关

通过 HTTP 接口热更新 `sys_config`（无需重启）：

```
POST /api/v1/dcc/update_config   (需 ROLE_ADMIN 权限)
Body: {"key":"degradeSwitch","value":"open"}
```

可用开关：
- `degradeSwitch`：抽奖降级（`close` 不降级 / `open` 降级）
- `rateLimiterSwitch`：限流总开关（`open` 开启 / `close` 关闭）

## 七、注意事项

1. **数据迁移**：本脚本面向新环境初始化，不提供老版本分库数据 → 单库的迁移脚本。如需迁移已有数据，请自行编写 `marketing_01.*` / `marketing_02.*` 各分表 `UNION ALL` 到单表的导入逻辑。
2. **消息补偿延迟**：去掉 MQ 后，中奖记录 / 返利入账 / 积分发货的异步处理由 `SendMessageTaskJob` 周期触发（约 2 分钟 + 60s 超时检测）。若需更低延迟，可缩短 `SendMessageTaskJob` 的 `@Scheduled(fixedDelay)`。
3. **老版本不受影响**：所有变更仅在 `feature/lightweight` 分支，`master` / `dev` 保持原样。
