# bash curl_marketing.credit_record.sh
curl -X PUT "http://127.0.0.1:9200/marketing.credit_record" -H 'Content-Type: application/json' -d'
{
  "mappings": {
    "properties": {
      "_user_id": {"type": "keyword"},
      "_trade_name": {"type": "text"},
      "_trade_type": {"type": "keyword"},
      "_trade_amount": {"type": "scaled_float", "scaling_factor": 100},
      "_out_business_no": {"type": "keyword"},
      "_create_time": {"type": "date"},
      "_update_time": {"type": "date"}
    }
  }
}
'
