package java.lang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class ConsulterRetrieverForJDK9 implements Function<Class<?>, MethodHandles.Lookup> {
	private static MethodHandle consulterRetriever;
	public final static MethodHandles.Lookup mainConsulter;
	
	static {
		mainConsulter = MethodHandles.lookup();
	}
	
	@Override
	public Lookup apply(Class<?> cls) {
		try {
			return (MethodHandles.Lookup)consulterRetriever.invokeWithArguments(cls, mainConsulter);
		} catch (Throwable exc) {
			return throwExceptionWithReturn(exc);
		}
	}
	
	private static <T> T throwExceptionWithReturn(Throwable exc) {
		throwException(exc);
		return null;
	}

	private static <E extends Throwable> void throwException(Throwable exc) throws E{
		throw (E)exc;
	}

}
