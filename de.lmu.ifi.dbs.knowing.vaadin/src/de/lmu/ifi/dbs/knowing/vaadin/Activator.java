package de.lmu.ifi.dbs.knowing.vaadin;

import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.lmu.ifi.dbs.knowing.core.model.IDataProcessingUnit;
import de.lmu.ifi.dbs.knowing.core.service.*;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static ServiceTracker directoryTracker;
	private static ServiceTracker dpuProviderTracker;
	private static ServiceTracker evaluateTracker;

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
		directoryTracker.open();
		
		dpuProviderTracker = new ServiceTracker(context, IDPUProvider.class.getName(), null);
		dpuProviderTracker.open();
		
		evaluateTracker = new ServiceTracker(context, IEvaluateService.class.getName(), null);
		evaluateTracker.open();
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
		
		evaluateTracker.close();
		evaluateTracker = null;
		
		Activator.context = null;
	}
	
	public static IFactoryDirectory getFactoryDirectory() {
		return (IFactoryDirectory) directoryTracker.getService();
	}
	
	public static IEvaluateService getEvaluateService() {
		return (IEvaluateService) evaluateTracker.getService();
	}
	
	public static IDPUProvider[] getDPUProviders() {
		Object[] services = dpuProviderTracker.getServices();
		IDPUProvider[] returns = new IDPUProvider[services.length];
		for(int i=0; i < services.length; i++)
			returns[i] = (IDPUProvider) services[i];
		return returns;
	}
	
	public static IDataProcessingUnit[] getRegisteredDPUs() {
		LinkedList<IDataProcessingUnit> returns = new LinkedList<IDataProcessingUnit>();
		for(Object service : dpuProviderTracker.getServices()) {
			IDPUProvider provider = (IDPUProvider) service;
			IDataProcessingUnit[] dpus = provider.getDataProcessingUnits();
			for(IDataProcessingUnit dpu : dpus) 
				returns.add(dpu);
		}
		return (IDataProcessingUnit[]) returns.toArray(new IDataProcessingUnit[returns.size()]);
	}
	
	public static IDataProcessingUnit getRegisteredDPU(String name) {
		for(Object service : dpuProviderTracker.getServices()) {
			IDPUProvider provider = (IDPUProvider) service;
			IDataProcessingUnit dpu = provider.getDataProcessingUnit(name);
			if(dpu != null) return dpu;
		}
		return null;
	}
	
	public static URL getRegisteredDPUPath(String name) {
		for(Object service : dpuProviderTracker.getServices()) {
			IDPUProvider provider = (IDPUProvider) service;
			URL url = provider.getURL(name);
			if(url != null) return url;
		}
		return null;
	}

}
