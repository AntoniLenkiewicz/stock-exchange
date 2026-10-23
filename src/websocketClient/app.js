const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:8080/stocks'
});

stompClient.onConnect = (frame) => {
    setConnected(true);
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/order', (response) => {
        showMessage((response.body));
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    stompClient.activate();
}

function disconnect() {
    stompClient.deactivate();
    setConnected(false);
    console.log("Disconnected");
}

function sendSellOrder() {
    stompClient.publish({
        destination: "/app/sell",
        body: JSON.stringify({'ticker':'AAPL','quantity':20.0,'orderType':'LIMITSELL','price':1000.50})
    });
}
function sendBuyOrder() {
    stompClient.publish({
        destination: "/app/buy",
        body: JSON.stringify({'ticker':'AAPL','quantity':20.0,'orderType':'LIMITBUY','price':1000.50})
    });
}

function showMessage(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function () {
    $("form").on('submit', (e) => e.preventDefault());
    $( "#connect" ).click(() => connect());
    $( "#disconnect" ).click(() => disconnect());
    $( "#sendSellOrder").click(() => sendSellOrder());
    $( "#sendBuyOrder" ).click(() => sendBuyOrder());
});