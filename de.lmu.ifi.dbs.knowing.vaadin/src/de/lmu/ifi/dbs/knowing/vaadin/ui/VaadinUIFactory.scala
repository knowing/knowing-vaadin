package de.lmu.ifi.dbs.knowing.vaadin.ui

import akka.actor.ActorRef
import akka.actor.TypedActor;
import de.lmu.ifi.dbs.knowing.core.events._
import de.lmu.ifi.dbs.knowing.core.factory.UIFactory
import de.lmu.ifi.dbs.knowing.core.graph.Node
import com.vaadin.ui.Window

class VaadinUIFactory(mainWindow: Window) extends TypedActor with UIFactory {

  def createContainer(node: Node): Object = { null }

  def update(actor: ActorRef, status: Status) = {  }

  def setSupervisor(supervisor: ActorRef) = {  }

}