package controllers

object javaColorToRGB {
  def toRGB(color: java.awt.Color): String = {
    "rgb(" + color.getRed + "," + color.getGreen + "," + color.getBlue + ")"
  }
}
