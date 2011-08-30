package de.lmu.ifi.dbs.knowing.vaadin.presenter

import java.util.Properties
import java.text.DecimalFormat
import akka.event.EventHandler.{ debug, info, warning, error }
import com.vaadin.ui.ComponentContainer
import com.vaadin.ui.Table
import de.lmu.ifi.dbs.knowing.core.processing.TPresenter
import de.lmu.ifi.dbs.knowing.core.factory.ProcessorFactory
import weka.core.Instance
import weka.core.Instances
import weka.core.Attribute
import weka.core.Attribute.{ NUMERIC, NOMINAL, DATE, STRING }


class TablePresenter extends TPresenter[ComponentContainer] {

  val name = "Table Presenter"
  val table = new Table

  private var columns = 0
  private var id = 0

  private val format = new DecimalFormat("#0.00");

  def createContainer(parent: ComponentContainer) {
    table.setSizeFull
    table.setImmediate(true)
    table.setSelectable(true)
    parent.addComponent(table)
  }

  def buildPresentation(instances: Instances) {
    table.getApplication.synchronized {
      if (!isBuild) {
        val attributes = instances.enumerateAttributes
        while (attributes.hasMoreElements) {
          val attr = attributes.nextElement.asInstanceOf[Attribute]
          table.addContainerProperty(attr.name, classOf[String], "<?>")
          columns += 1
        }
        isBuild = true
      }
      val rows = instances.enumerateInstances
      while (rows.hasMoreElements) {
        val inst = rows.nextElement.asInstanceOf[Instance]
        val content = new Array[Object](columns)
        for (i <- 0 until columns) {
          val attr = instances.attribute(i)
          content(i) = attr.`type` match {
            case NUMERIC => format.format(inst.value(i))
            case NOMINAL => attr.value(inst.value(i).toInt)
            case DATE => attr.formatDate(inst.value(i))
            case STRING => attr.value(inst.value(i).toInt)
          }
        }
        table.addItem(content, id.toString)
        id += 1
      }
      table.refreshCurrentPage
    }

  }

  def configure(properties: Properties) = {}

  def getContainerClass(): String = classOf[ComponentContainer].getName
}

class TablePresenterFactory extends ProcessorFactory(classOf[TablePresenter])