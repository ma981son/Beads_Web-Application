package controllers

import javax.inject._
import play.api.mvc._
import de.htwg.se.beads.Beads
import de.htwg.se.beads.controller.controllerComponent.ControllerInterface


@Singleton
class BeadsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val beadController: ControllerInterface = Beads.controller
  def beadsAsText: String = beadController.tempToString

  def about: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def beads: Action[AnyContent] = Action {
    Ok(views.html.beadsBoot(beadController))
  }

  def newTemp: Action[AnyContent] = Action {
    Ok(views.html.beads(beadController))
  }

  def resize(length: Int,width: Int): Action[AnyContent] = Action {
    beadController.changeSize(length,width)
    Ok(views.html.beads(beadController))
  }
}
