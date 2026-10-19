#!/bin/sh
curl -i -X POST -H 'Content-Type: application/json' -d '{"ticker": "AAPL", "quantity": 20.00, "orderType":"LIMITSELL", "price": 800.50 }' localhost:8080/sell
curl -i -X POST -H 'Content-Type: application/json' -d '{"ticker": "AAPL", "quantity": 20.00, "orderType":"MARKETSELL", "price": 100.50 }' localhost:8080/sell
curl -i -X POST -H 'Content-Type: application/json' -d '{"ticker": "AAPL", "quantity": 20.00, "orderType":"LIMITSELL", "price": 1000.50 }' localhost:8080/sell
