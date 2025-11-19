# bash curl_marketing.credit_account.sh
curl -X PUT "http://elastic:bBupa7qCVpoVK0QkjXt5@203.195.157.117:9200/marketing.credit_account" -H 'Content-Type: application/json' -d'
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
}
'
