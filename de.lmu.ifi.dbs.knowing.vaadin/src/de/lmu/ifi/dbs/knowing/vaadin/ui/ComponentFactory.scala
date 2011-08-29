package de.lmu.ifi.dbs.knowing.vaadin.ui

import com.vaadin.ui.Table
import de.lmu.ifi.dbs.knowing.vaadin.Activator.{ getFactoryDirectory, getRegisteredDPUs }
import de.lmu.ifi.dbs.knowing.core.factory.TFactory

object ComponentFactory {

  def processorTable: Table = {
    val table = new Table
    table.setSelectable(true)
    table.addContainerProperty("ID", classOf[String], "none")
    table.addContainerProperty("Provider", classOf[String], "?")
    val factoryDirectory = getFactoryDirectory
    val facs = factoryDirectory.getFactories
    facs foreach (f => table.addItem(Array(f.id, null), f.id))
    table
  }

  def dpuTable: Table = {
    val table = new Table
    table.setSelectable(true)
    table.addContainerProperty("Name", classOf[String], "none")
    val dpus = getRegisteredDPUs
    dpus foreach (dpu => table.addItem(Array(dpu.name), dpu.name))
    table
  }
}