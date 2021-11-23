
class Grd {
    constructor(length,width) {
        this.length = length;
        this.width = width;
        this.beadValue = Array.from(Array(length), () => new Array(width));
    }
    fill(json){
        let index = 0;
        for(let row=0; row<this.length; row++){
            for(let col=0; col<this.width; col++){
                this.beadValue[row][col] = (json[index].bead.color);
                index++
            }
        }
    }
}

var grid = new Grd(5, 5);

function updateGrid() {
    console.log("Updating Template")
    for (let row=0; row<grid.length; row++){
        for(let col=0; col<grid.width; col++){
            let bead = grid.beadValue[row][col]
            $("#beadR"+row+"C"+col).css('background-color', rgbToHex(bead.r,bead.g,bead.b))
        }
    }
    console.log(grid)
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

function rgbToHex(r, g, b) {
    return "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
}

function setBead(row,col) {
    hex = document.getElementById("exampleColorInput").value
    color = hexToRgb(hex)
    console.log("Setting bead (" + row + "/" + col + ")" + " to rgb: " + color.r + "/" + color.g + "/" + color.b );
    grid.beadValue[row][col] = color;
    $("#beadR"+row+"C"+col).css('background-color',hex);
    setBeadOnServer(row,col,color.r,color.g,color.b)
}

function fillTemplate(){
    console.log("Setting Template to ");
    for (let row=0; row <grid.length; row++) {
        for(let col=0; col<grid.width; col++){
            setBead(row,col)
        }
    }
}

function registerClickListener() {
    for (let row=0; row<grid.length; row++) {
            for(let col=0; col<grid.width; col++) {
                $("#beadR"+row+"C"+col).click(function() {setBead(row,col)});
            }
    }
    $("#fill").click(function (){fillTemplate()})
}

function registerInputListener() {
    $("#colIn").keyup(function (){resizeTemp(grid.length,this.value)});
    $("#rowIn").keyup(function (){resizeTemp(this.value,grid.width)});
    $("#stitch").change(function (){
        changeTempStitch(this.value)
        loadJson()
        location.reload()
        console.log("Template Stitch changed to " + this.value.toString())
    })
}

function resizeTemp(length,width) {
    changeTempSizeOnServer(length,width)
    loadJson()
    //updateDivTemplate()
    location.reload()
    console.log("Template Size changed to " + length + "x" + width)
}
function updateDivTemplate() {
    $("#temp").load(document.URL + " #temp");
}

function loadJson() {
    $.ajax({
        method: "GET",
        url: "/json",
        dataType: "json",

        success: function (data) {
            grid = new Grd(data.temp.length,data.temp.width)
            grid.fill(data.temp.beads)
            updateGrid(grid);
            registerClickListener();
            registerInputListener()
        }
    });
}
function changeTempStitch(stitch){
    $.get("/stitch/"+stitch)
}
function changeTempSizeOnServer(length,width){
    $.get("/resize/"+length+"/"+width, function (data) {
    })
}
function setBeadOnServer(row,col,r,g,b) {
    $.get("/set/"+row+"/"+col+"/"+r+"/"+g+"/"+b,
        function (data){
        console.log("Set Bead on Server")
        })
}

function connectWebSocket() {
    console.log("Connecting to Websocket");
    var wbsocket = new WebSocket("ws://localhost:9000/websocket")
    wbsocket.setTimeout

    wbsocket.onopen = function (event) {
        console.log("Connected to Websocket")
    }

    wbsocket.onclose = function () {
        console.log("Connection closed")
    }

    wbsocket.onerror = function (error) {
        console.log("Error occurred: " + error)
    }

    wbsocket.onmessage = function (e) {
        if(typeof e.data == "string") {
            let json = JSON.parse(e.data)
            grid = new Grd(json.temp.length,json.temp.width)
            location.reload()
            grid.fill(json.temp.beads)
            updateGrid(grid);
            registerClickListener();
            registerInputListener()
        }
    }
}

$(document).ready(function (){
    console.log("Document is ready, filling Template");
    loadJson();
    connectWebSocket()
})

