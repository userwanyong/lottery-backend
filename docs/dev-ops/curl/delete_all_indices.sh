# bash delete_all_indices.sh
# Delete all Elasticsearch indices for marketing

# ES connection config
ES_HOST="127.0.0.1:9200"
ES_AUTH="elastic:bxxxxxqCVpoVxxxkjXt5"

# All indices to delete
INDICES=(
    "marketing.activity_account"
    "marketing.activity_account_day"
    "marketing.activity_account_month"
    "marketing.activity_record"
    "marketing.credit_account"
    "marketing.credit_record"
    "marketing.task"
    "marketing.user_award_record"
    "marketing.user_behavior_rebate_order"
    "marketing.user_order"
)

echo "=========================================="
echo "  Delete All Marketing ES Indices"
echo "=========================================="

for index in "${INDICES[@]}"; do
    echo "Deleting index: $index"
    response=$(curl -s -o /dev/null -w "%{http_code}" -X DELETE "http://${ES_AUTH}@${ES_HOST}/${index}")
    if [ "$response" -eq 200 ]; then
        echo "  [OK] Deleted successfully"
    elif [ "$response" -eq 404 ]; then
        echo "  [SKIP] Index not found"
    else
        echo "  [ERROR] HTTP $response"
    fi
done

echo "=========================================="
echo "  Done!"
echo "=========================================="