# bash create_all_indices.sh
# Create all Elasticsearch indices for marketing

# ES connection config
ES_HOST="127.0.0.1:9200"
ES_AUTH="elastic:bxxxxxqCVpoVxxxkjXt5"

echo "=========================================="
echo "  Create All Marketing ES Indices"
echo "=========================================="

# 1. marketing.activity_account
echo "Creating index: marketing.activity_account"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.activity_account" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "long"},
      "_total_count": {"type": "integer"},
      "_total_count_surplus": {"type": "integer"},
      "_day_count": {"type": "integer"},
      "_day_count_surplus": {"type": "integer"},
      "_month_count": {"type": "integer"},
      "_month_count_surplus": {"type": "integer"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 2. marketing.activity_account_day
echo "Creating index: marketing.activity_account_day"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.activity_account_day" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "long"},
      "_day": {"type": "keyword"},
      "_day_count": {"type": "integer"},
      "_day_count_surplus": {"type": "integer"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 3. marketing.activity_account_month
echo "Creating index: marketing.activity_account_month"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.activity_account_month" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "long"},
      "_month": {"type": "keyword"},
      "_month_count": {"type": "integer"},
      "_month_count_surplus": {"type": "integer"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 4. marketing.activity_record
echo "Creating index: marketing.activity_record"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.activity_record" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "text"},
      "_sku": {"type": "text"},
      "_activity_id": {"type": "text"},
      "_activity_name": {"type": "text"},
      "_total_count": {"type": "text"},
      "_day_count": {"type": "text"},
      "_month_count": {"type": "text"},
      "_pay_amount": {"type": "text"},
      "_state": {"type": "text"},
      "_out_business_no": {"type": "text"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 5. marketing.credit_account
echo "Creating index: marketing.credit_account"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.credit_account" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "keyword"},
      "_total_amount": {"type": "scaled_float", "scaling_factor": 100},
      "_available_amount": {"type": "scaled_float", "scaling_factor": 100},
      "_account_status": {"type": "keyword"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 6. marketing.credit_record
echo "Creating index: marketing.credit_record"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.credit_record" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "keyword"},
      "_trade_name": {"type": "text"},
      "_trade_type": {"type": "keyword"},
      "_trade_amount": {"type": "scaled_float", "scaling_factor": 100},
      "_out_business_no": {"type": "keyword"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 7. marketing.task
echo "Creating index: marketing.task"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.task" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_topic": {"type": "keyword"},
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "keyword"},
      "_message_id": {"type": "keyword"},
      "_message": {"type": "text"},
      "_state": {"type": "keyword"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 8. marketing.user_award_record
echo "Creating index: marketing.user_award_record"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.user_award_record" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "long"},
      "_strategy_id": {"type": "long"},
      "_user_order_id": {"type": "keyword"},
      "_award_id": {"type": "long"},
      "_award_title": {"type": "text"},
      "_award_time": {"type": "date"},
      "_award_state": {"type": "keyword"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 9. marketing.user_behavior_rebate_order
echo "Creating index: marketing.user_behavior_rebate_order"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.user_behavior_rebate_order" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_activity_id": {"type": "keyword"},
      "_behavior_type": {"type": "keyword"},
      "_rebate_desc": {"type": "text"},
      "_rebate_type": {"type": "keyword"},
      "_rebate_config": {"type": "keyword"},
      "_out_business_no": {"type": "keyword"},
      "_biz_id": {"type": "keyword"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

# 10. marketing.user_order
echo "Creating index: marketing.user_order"
curl -s -X PUT "http://${ES_AUTH}@${ES_HOST}/marketing.user_order" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "text"},
      "_activity_id": {"type": "text"},
      "_activity_name": {"type": "text"},
      "_strategy_id": {"type": "text"},
      "_order_state": {"type": "text"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}'
echo ""

echo "=========================================="
echo "  Done!"
echo "=========================================="
