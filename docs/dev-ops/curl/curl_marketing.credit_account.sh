# bash curl_marketing.credit_account.sh
curl -X PUT "http://elastic:EpAQ8B1oTiN1fXp8SuWl@127.0.0.1:9200/marketing.credit_account" -H 'Content-Type: application/json' -d'
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
