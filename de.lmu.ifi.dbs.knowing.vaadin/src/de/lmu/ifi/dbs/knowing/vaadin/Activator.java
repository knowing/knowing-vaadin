package de.lmu.ifi.dbs.knowing.vaadin;

import java.net.URL;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import de.lmu.ifi.dbs.knowing.core.model.IDataProcessingUnit;
import de.lmu.ifi.dbs.knowing.core.service.IDPUDirectory;
import de.lmu.ifi.dbs.knowing.core.service.IDPUProvider;
import de.lmu.ifi.dbs.knowing.core.service.IEvaluateService;
import de.lmu.ifi.dbs.knowing.core.service.IFactoryDirectory;
import de.lmu.ifi.dbs.knowing.core.util.DPUUtil;

public class Activator implements BundleActivator {

	private static BundleContext context;
	
	private static ServiceTracker<IFactoryDirectory,IFactoryDirectory> directoryTracker;
	private static ServiceTracker<IDPUDirectory, IDPUDirectory> dpuDirectoryTracker;
	private static ServiceTracker<IEvaluateService,IEvaluateService> evaluateTracker;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		directoryTracker = new ServiceTracker<IFactoryDirectory, IFactoryDirectory>(context, IFactoryDirectory.class, null);
		directoryTracker.open();
		
		dpuDirectoryTracker = new ServiceTracker<IDPUDirectory, IDPUDirectory>(context, IDPUDirectory.class, null);
		dpuDirectoryTracker.open();
		
		evaluateTracker = new ServiceTracker<IEvaluateService, IEvaluateService>(context, IEvaluateService.class, null);
		evaluateTracker.open();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		directoryTracker.close();
		directoryTracker = null;
		
		dpuDirectoryTracker.close();
		dpuDirectoryTracker = null;
		
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
		Object[] services = dpuDirectoryTracker.getServices();
		IDPUProvider[] returns = new IDPUProvider[services.length];
		for(int i=0; i < services.length; i++)
			returns[i] = (IDPUProvider) services[i];
		return returns;
	}
	
	public static IDataProcessingUnit[] getRegisteredDPUs() {
		return dpuDirectoryTracker.getService().getDPUs();
	}
	
	public static IDataProcessingUnit getRegisteredDPU(String name) {
		return DPUUtil.getDPU(dpuDirectoryTracker.getService(), name);
	}
	
	public static URL getRegisteredDPUPath(String name) {
		return DPUUtil.getDPUPath(dpuDirectoryTracker.getService(), name);
	}

}
