# bash curl_marketing.task.sh
curl -X PUT "http://elastic:ki5ViGVkKgmvBeEJt48L@127.0.0.1:9200/marketing.task" -H 'Content-Type: application/json' -d'
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
}
'
