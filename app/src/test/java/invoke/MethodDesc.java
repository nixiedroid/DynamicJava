package invoke;


import java.lang.invoke.MethodType;

public class MethodDesc {
    private final String name;
    private final MethodType methodType;

    public MethodDesc(String name, MethodType methodType) {
        this.name = name;
        this.methodType = methodType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodDesc desc = (MethodDesc) o;
        return name.equals(desc.name) && methodType.equals(desc.methodType);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + methodType.hashCode();
        return result;
    }
}


