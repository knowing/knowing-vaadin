package de.lmu.ifi.dbs.knowing.vaadin.ui

import akka.actor.ActorRef
import akka.actor.TypedActor
import akka.event.EventHandler.{ debug, info, warning, error }
import de.lmu.ifi.dbs.knowing.core.events._
import de.lmu.ifi.dbs.knowing.core.factory.UIFactory
import com.vaadin.ui.Window
import com.vaadin.ui.Panel
import de.lmu.ifi.dbs.knowing.core.model.INode

class VaadinUIFactory(mainWindow: Window) extends TypedActor with UIFactory {

  private var window: Window = _
  
  def createContainer(node: INode): Object = {
    mainWindow.getApplication.synchronized {
      window = new Window
      window.setWidth(700)
      window.setHeight(450)
      window.setPositionX(100)
      window.setPositionY(100)
      val panel = new Panel
      panel.setSizeFull
      window.addComponent(panel)
      mainWindow.addWindow(window)
      window.bringToFront
      panel
    }

  }

  def update(actor: ActorRef, status: Status) {
    //Handle special status events
    status match {
      case UpdateUI() => mainWindow.synchronized(window.bringToFront)
      case Shutdown() => 
      case _ =>
    }

  }

  def setSupervisor(supervisor: ActorRef) = {}

}