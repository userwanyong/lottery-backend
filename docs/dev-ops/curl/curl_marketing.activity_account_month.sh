# bash curl_marketing.activity_account_month.sh
curl -X PUT "http://elastic:bBupa7qCVpoVK0QkjXt5@203.195.157.117:9200/marketing.activity_account_month" -H 'Content-Type: application/json' -d'
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
}
'
