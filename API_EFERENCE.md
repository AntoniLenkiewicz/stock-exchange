# Order Object
Order id and order status are set by the server to communicate the id and status of the order.
Order id may be needed to retrieve a specific orders information or cancel an order.
## Fields
orderId: int set by server 

ticker: string

quantity: double

orderType: enum Send one of the following as a string:
MARKETBUY,
MARKETSELL,
LIMITSELL,
LIMITBUY,
STOPBUY,
STOPSELL,
BUYSTOPLIMIT,
SELLSTOPLIMIT

orderStatus: enum set by server, one of the followiing: RECIEVED, ACCEPTED, DECLINED, CANCELLED

price: float
# Web Socket Connection

Before Sending Buy/Sell Requests you must negotiate a websocket connection. The client uses the STOMP message Protocol.

Use the broker Url: "ws://localhost:8080/stocks"
<br>
Subscribe to the topic "/topic/order"
<br>

# Buy Requests
Send Buy Requests using the STOMP protocol to the endpoint: ws://localhost:8080/buy

The server expects the request to contain an Order Object omitting the orderId and orderStatus fields.

The server will send a response with the order object filling in the orderId and orderStatus on receipt of the order to the topic "/topic/order", and once again once the order has been accepted.

*It is possible for the order to have the status accepted when the first response is sent, this just means it was processed before the server could send a response*

# Sell Requests
Send Sell Requests using the STOMP protocol to the endpoint: ws://localhost:8080/sell

The server expects the request to contain an Order Object omitting the orderId and orderStatus fields.

The server will send a response with the order object filling in the orderId and orderStatus on receipt of the order to the topic "/topic/order", and once again once the order has been accepted.

*It is possible for the order to have the status accepted when the first response is sent, this just means it was processed before the server could send a response*

# Get Requests
## Tickers
Endpoint: localhost:8080/tickers

Takes no parameters, returns list of tickers that the exchange trades.

Returns list of strings
<br>
Example: ["NFLX","GOOGL","NVDA","META","AAPL","KO","JPM","ORCL","DIS","MSFT","COST","TSLA","INTC","V","AMD","UBER","AMZN","CRM","WMT","BA"]
## Lowest Ask
Endpoint: localhost:8080/lowest-ask/[ticker parameter]
<br>
Example Url: localhost:8080/lowest-ask/nvda

Replace ticker parameter for a stocks ticker.

Returns String with Value of the Lowest Ask Or None if no buy orders are active
<br>
Example: "None" or "100.50"

## Highest Bid 
Endpoint: localhost:8080/highest-bid/[ticker parameter]
<br>
Example URL: localhost:8080/highest-bid/nvda

Replace ticker parameter for a stocks ticker.

Returns String with Value of the Highest Bid or None if no sell orders are active
<br>
Example: "None" or "100.50"