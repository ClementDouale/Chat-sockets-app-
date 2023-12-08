window.addEventListener( "load", function( event ) {

    var username = document.getElementById("username").innerHTML;
    var idChannel = document.getElementById("mainChannel").innerHTML;

    let ws = new WebSocket("ws://localhost:8080/chatserver/"+ idChannel + "/" + username );
    let txtHistory = document.getElementById( "history" );
    let txtMessage = document.getElementById( "txtMessage" );
    txtMessage.focus();

    ws.addEventListener( "open", function( evt ) {
        console.log( "Connection established" );
    });

    ws.addEventListener( "message", function( evt ) {
        let message = evt.data;
        console.log( "Receive new message: " + message );
        txtHistory.value += message + "\n";
        txtHistory.scrollTop = txtHistory.scrollHeight;
    });

    ws.addEventListener( "close", function( evt ) {
        console.log( "Connection closed" );
    });


    let btnSend = document.getElementById( "btnSend" );
    btnSend.addEventListener( "click", function( clickEvent ) {
        ws.send( txtMessage.value );
        txtMessage.value = "";
        txtMessage.focus();
    });

    txtMessage.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            ws.send( txtMessage.value );
            txtMessage.value = "";
            txtMessage.focus();
        }
    });

    /* Possibilité d'utiliser une fonction de se déconnecter d'un channel manuellement
    let btnClose = document.getElementById( "btnClose" );
    btnClose.addEventListener( "click", function( clickEvent ) {
        ws.close();
    });
    $/
     */

});
