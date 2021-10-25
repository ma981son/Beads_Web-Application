package controllers

import javax.inject._

import play.api.mvc._
import de.htwg.se.beads.Beads


@Singleton
class BeadsController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  val beadController = Beads.controller
  def beadsAsText = beadController.tempToString

  def about: Action[AnyContent] = Action {
    Ok(views.html.index())
  }

  def beads: Action[AnyContent] = Action {
    Ok(views.html.beads(beadController))
  }

  def huichol: Action[AnyContent] = Action {
    Ok(views.html.huichol())
  }

  def newTemp = Action {
    Ok(views.html.beads(beadController))
  }

  def resize(length: Int,width: Int) = Action {
    beadController.changeSize(length,width)
    Ok(views.html.beads(beadController))
  }
}
