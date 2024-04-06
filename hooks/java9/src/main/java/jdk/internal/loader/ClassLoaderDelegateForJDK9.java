
package jdk.internal.loader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.lang.invoke.MethodHandle;


public class ClassLoaderDelegateForJDK9 extends BuiltinClassLoader {

	private ClassLoader classLoader;
	private MethodHandle loadClassMethod;

	static {
		ClassLoader.registerAsParallelCapable();
	}

	ClassLoaderDelegateForJDK9(BuiltinClassLoader parent, ClassLoader classLoader, MethodHandle loadClassMethodHandle) {
		super("ClassLoaderDelegateOf" + classLoader.toString(), parent, null);
		this.classLoader = classLoader;
		this.loadClassMethod = loadClassMethodHandle;
	}

	@Override
	protected Class<?> loadClassOrNull(String className, boolean resolve) {
		try {
			return (Class<?>)loadClassMethod.invokeWithArguments(classLoader, className, resolve);
		} catch (Throwable exc) {
			exc.printStackTrace();
			return null;
		}
	}

	@Override
	protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
		try {
			return (Class<?>)loadClassMethod.invokeWithArguments(classLoader, className, resolve);
		} catch (ClassNotFoundException exc) {
			throw exc;
		} catch (Throwable exc) {
			throw new ClassNotFoundException(className, exc);
		}
	}

	@Override
	public URL getResource(String name) {
		return classLoader.getResource(name);
	}

	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		return classLoader.getResources(name);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		return classLoader.getResourceAsStream(name);
	}
}