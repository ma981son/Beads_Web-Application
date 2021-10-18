package controllers

import javax.inject._

import play.api.mvc._
import de.htwg.se.beads.Beads


@Singleton
class BeadsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val beadController = Beads.controller
  def beadsAsText = beadController.tempToString

  def about= Action {
    Ok(views.html.index())
  }

  def beads = Action {
    Ok(beadsAsText)
  }
}