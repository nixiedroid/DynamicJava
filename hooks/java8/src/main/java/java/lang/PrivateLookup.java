package java.lang;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PrivateLookup implements Function<Class<?>, MethodHandles.Lookup> {
	private static MethodHandle method;
	public final static MethodHandles.Lookup lookup;
	
	static {
		lookup = MethodHandles.lookup();
	}
	
	@Override
	public Lookup apply(Class<?> cls) {
		try {
			return (MethodHandles.Lookup)method.invokeWithArguments(cls, lookup);
		} catch (Throwable exc) {
			return throwExceptionWithReturn(exc);
		}
	}
	
	private static <R> R throwExceptionWithReturn(Throwable exc) {
		throwException(exc);
		return null; //Actually, unreachabe
	}

	private static <E extends Throwable> void throwException(Throwable exc) throws E{
		throw (E)exc;
	}

}
