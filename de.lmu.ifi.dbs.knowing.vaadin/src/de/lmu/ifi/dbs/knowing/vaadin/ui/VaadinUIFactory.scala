package de.lmu.ifi.dbs.knowing.vaadin.ui

import akka.actor.ActorRef
import akka.actor.TypedActor
import akka.event.EventHandler.{ debug, info, warning, error }
import de.lmu.ifi.dbs.knowing.core.events._
import de.lmu.ifi.dbs.knowing.core.factory.UIFactory
import de.lmu.ifi.dbs.knowing.core.graph.Node
import com.vaadin.ui.Window
import com.vaadin.ui.Panel

class VaadinUIFactory(mainWindow: Window) extends TypedActor with UIFactory {

  def createContainer(node: Node): Object = {
    mainWindow.getApplication.synchronized {
      val window = new Window
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

  def update(actor: ActorRef, status: Status) = {}

  def setSupervisor(supervisor: ActorRef) = {}

}