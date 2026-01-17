# bash curl_marketing.user_behavior_rebate_order.sh
curl -X PUT "http://elastic:EpAQ8B1oTiN1fXp8SuWl@127.0.0.1:9200/marketing.user_behavior_rebate_order" -H 'Content-Type: application/json' -d'
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
}
'
