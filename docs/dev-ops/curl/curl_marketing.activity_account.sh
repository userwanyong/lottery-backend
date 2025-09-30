# bash curl_marketing.activity_account.sh
curl -X PUT "http://203.195.157.117:9200/marketing.activity_account" -H 'Content-Type: application/json' -d'
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
}
'
