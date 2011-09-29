package de.lmu.ifi.dbs.knowing.vaadin.test

import de.lmu.ifi.dbs.knowing.core.service._
import org.osgi.framework.BundleContext
import org.osgi.framework.BundleActivator
import org.osgi.framework.ServiceRegistration

class Activator extends BundleActivator {

  private var dpuService: ServiceRegistration[IDPUProvider] = _

  def start(context: BundleContext) = {
    Activator.context = context
    dpuService = context.registerService(classOf[IDPUProvider], BundleDPUProvider.newInstance(context.getBundle), null)
  }

  def stop(context: BundleContext) = {
    Activator.context = null
    dpuService.unregister
  }

}

object Activator {

  private var context: BundleContext = null

  def getContext(): BundleContext = context
}