package controllers

import javax.inject._
import play.api.mvc._
import de.htwg.se.beads.Beads
import de.htwg.se.beads.controller.controllerComponent.ControllerInterface
import de.htwg.se.beads.model.templateComponent.templateBaseImpl.Stitch


@Singleton
class BeadsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val beadController: ControllerInterface = Beads.controller

  def about: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def beads: Action[AnyContent] = Action {
    Ok(views.html.beads(beadController))
  }

  def newTemp: Action[AnyContent] = Action {
    beadController.createEmptyTemplate(beadController.tempLength,beadController.tempWidth,beadController.stitch)
    Ok(views.html.beads(beadController))
  }

  def newTempSquare: Action[AnyContent] = Action {
    beadController.createEmptyTemplate(beadController.tempLength,beadController.tempWidth,Stitch.Square)
    Ok(views.html.beads(beadController))
  }

  def newTempBrick: Action[AnyContent] = Action {
    beadController.createEmptyTemplate(beadController.tempLength,beadController.tempWidth,Stitch.Brick)
    Ok(views.html.beads(beadController))
  }

  def resize(length: Int,width: Int): Action[AnyContent] = Action {
    beadController.changeSize(length,width)
    Ok(views.html.beads(beadController))
  }

  def tempToJson = Action {
    Ok(beadController.toJson)
  }
}
