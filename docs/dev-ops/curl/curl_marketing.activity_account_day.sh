# bash curl_marketing.activity_account_day.sh
curl -X PUT "http://elastic:EpAQ8B1oTiN1fXp8SuWl@127.0.0.1:9200/marketing.activity_account_day" -H 'Content-Type: application/json' -d'
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
}
'