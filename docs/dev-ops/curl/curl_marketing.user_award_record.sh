# bash curl_marketing.user_award_record.sh
curl -X PUT "http://203.195.157.117:9200/marketing.user_award_record" -H 'Content-Type: application/json' -d'
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
}
'
