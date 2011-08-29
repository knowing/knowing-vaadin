package de.lmu.ifi.dbs.knowing.vaadin;

import java.util.Arrays;
import java.util.LinkedList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.lmu.ifi.dbs.knowing.core.service.*;
import de.lmu.ifi.dbs.knowing.core.graph.xml.*;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static ServiceTracker directoryTracker;
	private static ServiceTracker dpuProviderTracker;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		directoryTracker = new ServiceTracker(context, IFactoryDirectory.class.getName(), null);
		dpuProviderTracker = new ServiceTracker(context, IDPUProvider.class.getName(), null);
		directoryTracker.open();
		dpuProviderTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		directoryTracker.close();
		directoryTracker = null;
		
		dpuProviderTracker.close();
		dpuProviderTracker = null;
		Activator.context = null;
	}
	
	public static IFactoryDirectory getFactoryDirectory() {
		return (IFactoryDirectory) directoryTracker.getService();
	}
	
	public static IDPUProvider[] getDPUProviders() {
		Object[] services = dpuProviderTracker.getServices();
		IDPUProvider[] returns = new IDPUProvider[services.length];
		for(int i=0; i < services.length; i++)
			returns[i] = (IDPUProvider) services[i];
		return returns;
	}
	
	public static DataProcessingUnit[] getRegisteredDPUs() {
		LinkedList<DataProcessingUnit> returns = new LinkedList<DataProcessingUnit>();
		for(Object service : dpuProviderTracker.getServices()) {
			IDPUProvider provider = (IDPUProvider) service;
			DataProcessingUnit[] dpus = provider.getDataProcessingUnits();
			for(DataProcessingUnit dpu : dpus) 
				returns.add(dpu);
		}
		return (DataProcessingUnit[]) returns.toArray(new DataProcessingUnit[returns.size()]);
	}

}
