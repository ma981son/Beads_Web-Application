

function changeBeadColor(_this) {
    _this.style.backgroundColor = document.getElementById("exampleColorInput").value
    console.log("bead " + _this.coords + " colored with: ")
}

function fillTemplate() {
    let beads = document.getElementsByClassName("bead");
    for(let i=0; i < beads.length; i++){
        changeBeadColor(beads.item(i))
    }
}

function goToTestPage(id) {
    window.location.href = "/"+id;
}

