# bash run_all.sh ！！！不要一块导入es目录！！！
#!/bin/bash

# 目标服务器地址
BASE_URL="http://115.190.238.109:8082/etl/es7"

# 所有需要上传的 YAML 文件列表
FILES=(
    "marketing01_activity_account.yml"
    "marketing01_activity_account_day.yml"
    "marketing01_activity_account_month.yml"
    "marketing01_activity_record_000.yml"
    "marketing01_activity_record_001.yml"
    "marketing01_activity_record_002.yml"
    "marketing01_activity_record_003.yml"
    "marketing01_credit_account.yml"
    "marketing01_credit_record_000.yml"
    "marketing01_credit_record_001.yml"
    "marketing01_credit_record_002.yml"
    "marketing01_credit_record_003.yml"
    "marketing01_task.yml"
    "marketing01_user_award_record_000.yml"
    "marketing01_user_award_record_001.yml"
    "marketing01_user_award_record_002.yml"
    "marketing01_user_award_record_003.yml"
    "marketing01_user_behavior_rebate_order_000.yml"
    "marketing01_user_behavior_rebate_order_001.yml"
    "marketing01_user_behavior_rebate_order_002.yml"
    "marketing01_user_behavior_rebate_order_003.yml"
    "marketing01_user_order_000.yml"
    "marketing01_user_order_001.yml"
    "marketing01_user_order_002.yml"
    "marketing01_user_order_003.yml"

    # marketing02 系列
    "marketing02_activity_account.yml"
    "marketing02_activity_account_day.yml"
    "marketing02_activity_account_month.yml"
    "marketing02_activity_record_000.yml"
    "marketing02_activity_record_001.yml"
    "marketing02_activity_record_002.yml"
    "marketing02_activity_record_003.yml"
    "marketing02_credit_account.yml"
    "marketing02_credit_record_000.yml"
    "marketing02_credit_record_001.yml"
    "marketing02_credit_record_002.yml"
    "marketing02_credit_record_003.yml"
    "marketing02_task.yml"
    "marketing02_user_award_record_000.yml"
    "marketing02_user_award_record_001.yml"
    "marketing02_user_award_record_002.yml"
    "marketing02_user_award_record_003.yml"
    "marketing02_user_behavior_rebate_order_000.yml"
    "marketing02_user_behavior_rebate_order_001.yml"
    "marketing02_user_behavior_rebate_order_002.yml"
    "marketing02_user_behavior_rebate_order_003.yml"
    "marketing02_user_order_000.yml"
    "marketing02_user_order_001.yml"
    "marketing02_user_order_002.yml"
    "marketing02_user_order_003.yml"
)

# 执行每个请求
echo "🚀 开始批量提交 ETL 配置文件..."
echo "=================================="

for file in "${FILES[@]}"; do
    echo "👉 正在提交: $file"
    response=$(curl -X POST "$BASE_URL/$file" -s --fail -w "%{http_code}")

    if [ $? -eq 0 ]; then
        echo "✅ 成功: $file (HTTP $response)"
    else
        echo "❌ 失败: $file (HTTP $response)"
    fi
done

echo "=================================="
echo "✅ 批量提交完成！"