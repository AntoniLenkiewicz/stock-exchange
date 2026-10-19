#!/bin/sh
curl -i -X POST -H 'Content-Type: application/json' -d '{"ticker": "AAPL", "quantity": 20, "orderType":"MARKETBUY", "price": 1200.50 }' localhost:8080/buy
curl -i -X POST -H 'Content-Type: application/json' -d '{"ticker": "AAPL", "quantity": 20, "orderType":"MARKETBUY", "price": 200.50 }' localhost:8080/buy
curl -i -X POST -H 'Content-Type: application/json' -d '{"ticker": "AAPL", "quantity": 20, "orderType":"STOPBUY", "price": 400.50 }' localhost:8080/buy
