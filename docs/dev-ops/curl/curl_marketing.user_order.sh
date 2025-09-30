# bash curl_marketing.user_order.sh
curl -X PUT "http://203.195.157.117:9200/marketing.user_order" -H 'Content-Type: application/json' -d'
{
    "mappings": {
      "properties": {
        "_user_id":{"type": "text"},
        "_activity_id":{"type": "text"},
        "_activity_name":{"type": "text"},
        "_strategy_id":{"type": "text"},
        "_order_state":{"type": "text"},
        "_create_time":{"type": "date"},
        "_update_time":{"type": "date"}
      }
    }
}'
