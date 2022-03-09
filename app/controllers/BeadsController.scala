package controllers

import javax.inject._
import play.api.mvc._
import de.htwg.se.beads.Beads

import java.awt
import play.api.libs.streams.ActorFlow
import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.actor._
import de.htwg.se.beads.controller.{BeadChanged, ControllerInterface, TemplateChanged, TemplateSizeChanged}
import de.htwg.se.beads.model.template.templateBaseImpl.{Color, Stitch}

import scala.swing.Reactor

@Singleton
class BeadsController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat:Materializer) extends AbstractController(cc) {
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

  def set(row:Int, col:Int, r:Int, g:Int, b:Int): Action[AnyContent] = Action {
    beadController.setColor(row,col, new awt.Color(r,g,b))
    Ok(views.html.beads(beadController))
  }

  def undo() = Action {
    beadController.undo()
    Ok(views.html.beads(beadController))
  }

  def redo() = Action {
    beadController.redo()
    Ok(views.html.beads(beadController))
  }

  def rgbToAWT(color: Color): awt.Color = {
    new awt.Color(color.r.toInt,color.g.toInt,color.b.toInt)
  }

  def tempToJson: Action[AnyContent] = Action {
    beadController.save()
    Ok(beadController.toJson)
  }

  def socket: WebSocket = WebSocket.accept[String,String]{ request =>
    ActorFlow.actorRef{ out =>
      println("Connect received")
      BeadWebSocketActorFactory.create(out)
    }
  }

  object BeadWebSocketActorFactory {
    def create(out:ActorRef): Props = {
      Props(new BeadWebSocketActor(out))
    }
  }

  class BeadWebSocketActor(out: ActorRef) extends Actor with Reactor{
    listenTo(beadController)

    def receive: Receive ={
      case msg: String =>
        out ! ("I received your message: " + msg)
        println("Send Json to Client " + msg)
    }

    reactions += {
      case event: TemplateSizeChanged => sendJsonToClient()
      case event: BeadChanged => sendJsonToClient()
      case event: TemplateChanged => sendJsonToClient()
    }

    def sendJsonToClient(): Unit = {
      println("Received event from Controller")
      out ! (beadController.toJson.toString())
    }
  }

}

