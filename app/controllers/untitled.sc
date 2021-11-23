val width = 20
val lenght = 81
val blocksize = 9
val scalar  = 17

def row(scalar:Int): Double = {
  return (Math.floor((scalar % lenght) /blocksize)) + (blocksize * Math.floor((scalar /(lenght*blocksize))));
}
def col(scalar:Int): Double = {
  return (scalar %blocksize) + (blocksize *Math.floor((scalar/width))) - (width*Math.floor((scalar/(width*blocksize))));
}

val resultRow = row(scalar)
val resultCol = col(scalar)