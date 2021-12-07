loadJson()

let beadHouses = beadTemp()

function beadTemp(){
    let beads = Array.from(Array(5), () => new Array(5));
    let index=0
    for(let row=0; row<5;row++){
        for(let col=0; col<5;col++){
            beads[row][col]=({tempRow:row, tempCol: col, beadNr: index})
            index++
        }
    }
    return beads
}


Vue.component('button-counter', {
    data: function () {
        console.log(grid.length)
        return {
            beads: grid
        }
    },
    template:
        `<div class="templateContainer" id="temp">
            <div class="template" id="temp">
                <template v-if="beads.stitch === 'Brick'">
                    <div v-for="(n, i) in beads.length">
                        <template v-if="i % 2 != 0" class="house size" :id="i">
                            <span class="beadEMPTY">
                                <button class="emptyBead"></button>
                            </span>
                        </template v-else>
                        <template v-for="(q, col) in beads.width">
                            <span class="bead1" :id="col"">
                                <input type="button" class="bead" v-bind:id="'beadR'+i+'C'+col">
                            </span>
                        </template>
                    </div>
                </template>
                <template v-else>
                    <div v-for="(n, i) in beads.length">
                        <template v-for="(q, col) in beads.width">
                            <input type="button" class="bead" v-bind:id="'beadR'+i+'C'+col">
                        </template>
                    </div>
                </template>
            </div>
        </div>`
})
