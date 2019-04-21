package com.juno.avoidance.utils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import timber.log.Timber;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 20:13.
 * When I met you in the summer.
 * Description : 作用在一般对象上的方法
 */
@SuppressWarnings("unused")
public class ObjectUtil {

    /**
     * Created by Juno at 20:14, 2019/4/16.
     * nonNull description : 对象判空
     */
    public static boolean checkNotNull(Object o) {
        return o != null;
    }

    /**
     * Created by Juno at 21:50, 2019/4/16.
     * notNull description : 对象判空
     */
    public static boolean notNull(Object o) {
        return o != null;
    }

    /**
     * Created by Juno at 14:01, 2019/4/18.
     * notNull description : 判断是否全部不为空
     */
    public static boolean notNullAll(Object... args) {
        for (Object o : args) {
            if (isNull(o)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Created by Juno at 21:45, 2019/4/16.
     * checkNull description : 对象判空
     */
    public static boolean checkNull(Object o) {
        return o == null;
    }

    /**
     * Created by Juno at 21:48, 2019/4/16.
     * isNull description : 对象判空
     */
    public static boolean isNull(Object o) {
        return o == null;
    }

    /**
     * Created by Juno at 21:06, 2019/4/16.
     * single description : 使用反射，若对象不为空则执行某方法，返回方法invoke的返回值
     */
    @SuppressWarnings("unchecked")
    public static <T> T single(Object o, String methodName, Object... args) {
        if (checkNotNull(o)) {
            try {
                Method method = findMethod(o, methodName, true, args);
                return notNull(method) ? (T) method.invoke(o, args) : null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static <T> T single(Object o, Method method, Object... args) {
        try {
            return notNull(o) && notNull(method) ? (T) method.invoke(o, args) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Created by Juno at 21:06, 2019/4/16.
     * single description : 若对象不为空则执行某方法，返回原对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T refund(T o, String methodName, Object... args) {
        if (checkNotNull(o)) {
            try {
                Method method = findMethod(o, methodName, true, args);
                if (notNull(method)) {
                    method.invoke(o, args);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return o;
    }

    /**
     * Created by Juno at 9:41, 2019/4/17.
     * toClasses description : 将入参列表转换为类型列表
     */
    private static Class[] toClasses(Object... args) {
        Class[] clazz = new Class[args.length];
        for (int i = 0; i < args.length; ++i) {
            clazz[i] = args[i].getClass();
        }
        return clazz;
    }

    /**
     * Created by Juno at 9:40, 2019/4/17.
     * findMethod description : 寻找对象的方法
     */
    private static Method findMethod(Object o, String name, boolean declared, Object... args) {
        Class<?> clazz = o.getClass();
        try {
            Method method = declared ? clazz.getDeclaredMethod(name, toClasses(args)) : clazz.getMethod(name, toClasses(args));
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Method[] methods = declared ? clazz.getDeclaredMethods() : clazz.getMethods();
            return findMethod(methods, name, args);
        }
    }

    /**
     * Created by Juno at 9:40, 2019/4/17.
     * findMethod description : 寻找对象的方法
     */
    private static Method findMethod(Method[] methods, String name, Object... args) {
        for (Method m : methods) {
            if (m.getName().equals(name) && equals(m.getParameterTypes(), args)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Created by Juno at 9:13, 2019/4/17.
     * equals description : 比较入参数量和类型，左边形参列表，右边实际入参
     */
    @SuppressWarnings("unchecked")
    public static boolean equals(Class[] clazz, Object... args) {
        if (clazz.length != args.length) { //数量不同，返回false
            return false;
        }
        for (int i = 0; i < clazz.length; ++i) {
            if (!clazz[i].equals(args[i].getClass())) { // 若类型不一致，继续判断
                if (!clazz[i].isAssignableFrom(args[i].getClass())) { //若不是子类，继续判断
                    boolean isPrimitive = false;
                    try {
                        Class<?> type = (Class<?>) args[i].getClass().getField("TYPE").get(null);//获取包装类型的基本类型
                        isPrimitive = type.isPrimitive() && type.equals(clazz[i]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (!isPrimitive) { //若不是包装类型或包装类型对应的基本类型不等于clazz[i]，则最终判断两个对象完全不一致
                        return false;
                    }
                }
            }

        }
        return true;
    }

    /**
     * Created by Juno at 21:24, 2019/4/16.
     * Again description : 以一种链式调用的方法来判空和执行方法
     * {@link #from(Object, String, Object...)} --> cache --> next --> same --> ...
     */
    public static class Again {

        private WeakReference<?> weakReference;
        private Method[] methods = null;
        private Method lastMethod = null;

        /**
         * Created by Juno at 16:07, 2019/4/17.
         * ref description : 设置引用
         */
        private Again ref(WeakReference<?> reference) {
            this.weakReference = reference;
            return this;
        }

        /**
         * Created by Juno at 21:35, 2019/4/17.
         * cache description : 缓存自身的引用对象，用于when和receive
         */
        private static WeakReference<Object[]> sCache;

        public Again store() {
            Object[] objects = new Object[1];
            objects[0] = weakReference.get();
            sCache = new WeakReference<>(objects);
            return this;
        }

        /**
         * Created by Juno at 21:35, 2019/4/17.
         * cache description : 缓存外部对象，用于when和receive
         */
        public Again args(Object... o) {
            sCache = new WeakReference<>(o);
            return this;
        }

        /**
         * Created by Juno at 21:37, 2019/4/17.
         * receive description : 使用give保存的临时变量来入参，执行后不会清空缓存
         */
        public Again receive(String method) {
            if (notNull(sCache) && notNull(sCache.get())) {
                next(method, sCache.get());
            }
            return this;
        }

        /**
         * Created by Juno at 14:08, 2019/4/18.
         * when description : 若缓存的是boolean类型，则执行方法，执行后不会清空缓存
         */
        public Again when(String method, Object... args) {
            if (notNull(sCache) && notNull(sCache.get()) && notNull(sCache.get()[0]) && sCache.get()[0] instanceof Boolean && ((Boolean) sCache.get()[0])) {
                next(method, args);
            }
            return this;
        }

        /**
         * Created by Juno at 14:13, 2019/4/18.
         * done description : 清空缓存
         */
        public Again clean() {
            lastMethod = null;
            methods = null;
            sCache = null;
            return this;
        }

        /**
         * Created by Juno at 19:55, 2019/4/17.
         * Action description : at开始调用链，still用于延续
         */
        public interface Action {
            void action();
        }

        public static Again at(Action action) {
            return new Again().still(action);
        }

        public Again still(Action action) {
            action.action();
            return this;
        }

        /**
         * Created by Juno at 14:09, 2019/4/17.
         * from description : 传入对象初始化并执行方法
         */
        @SuppressWarnings("unchecked")
        public static <T> Again from(T o, String methodName, Object... args) {
            return new Again().ref(new WeakReference(o)).next(methodName, args);
        }

        /**
         * Created by Juno at 14:09, 2019/4/17.
         * from description : 仅初始化
         */
        public static <T> Again from(T o) {
            return new Again().ref(new WeakReference<>(o));
        }

        public interface Lazier {
            Object lazy();
        }

        /**
         * Created by Juno at 14:09, 2019/4/17.
         * is description : 对象为空时赋值
         */
        public Again lazy(Lazier lazier) {
            Object o = weakReference.get();
            if (isNull(o)) {
                o = lazier.lazy();
                ref(new WeakReference<>(o));
            }
            return this;
        }

        /**
         * Created by Juno at 11:16, 2019/4/17.
         * another description : 开启另一个调用调用链
         */
        public <T> Again another(T o) { //TODO just highlight it
            Timber.e("ObjectUtil --> %s 调用%s",
                    isNull(weakReference) ? isNull(o) ? "null"
                            : o.getClass().getCanonicalName()
                            : isNull(weakReference.get()) ? "wrf get Null"
                            : weakReference.get().getClass().getSimpleName()
                    , isNull(weakReference) ? "开始" : "完毕");
            return from(o);
        }

        public <T> Again another(T o, String method, Object... args) { //TODO just highlight it
            return another(o).next(method, args);
        }

        /**
         * Created by Juno at 15:53, 2019/4/18.
         * Mapper description : 用于转换，map后记得使用clean，否则如果开启了方法缓存将会造成新对象找不到方法
         */
        public interface Mapper<Source, Target> {
            Target convert(Source source);
        }

        @SuppressWarnings("unchecked")
        public <Source, Target> Again map(Mapper<Source, Target> mapper) {
            Source o = (Source) weakReference.get();
            if (notNull(o)) {
                Target t = mapper.convert(o);
                weakReference = new WeakReference<>(t);
            }
            return this;
        }

        /**
         * Created by Juno at 11:20, 2019/4/17.
         * get description : 以下两种方式返回对象
         */
        @SuppressWarnings("unchecked")
        public <T> T get() {
            return (T) weakReference.get();
        }

        public interface Getter<T> {
            void get(T o);
        }

        @SuppressWarnings("unchecked")
        public Again get(Getter getter) {
            getter.get(weakReference.get());
            return this;
        }

        /**
         * Created by Juno at 9:55, 2019/4/17.
         * same description : 执行上一次执行的方法
         */
        public Again same(Object... args) {
            Object o = weakReference.get();
            if (notNull(lastMethod) && notNull(o)) {
                try {
                    lastMethod.invoke(o, args);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return this;
        }

        /**
         * Created by Juno at 9:13, 2019/4/17.
         * cache description : 开启方法缓存，参数：是否为定义方法
         */
        public Again cache(boolean declared) {
            Object o = weakReference.get();
            if (notNull(o)) {
                methods = declared ? o.getClass().getDeclaredMethods() : o.getClass().getMethods();
            }
            return this;
        }

        /**
         * Created by Juno at 9:11, 2019/4/17.
         * next description : 查看是否有缓存方法，有缓存就读取出来，没有就调用{@link #single(Object, String, Object...)}
         */
        public Again next(String methodName, Object... args) {
            Object o = weakReference.get();
            if (notNull(methods)) {
                Method method = findMethod(methods, methodName, args);
                if (notNull(method)) {
                    try {
                        method.invoke(o, args);
                        lastMethod = method;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Method method = findMethod(o, methodName, false, args);
                lastMethod = method;
                single(o, method, args);
            }
            return this;
        }


    }

    /**
     * Created by Juno at 20:18, 2019/4/16.
     * ExecuteNonNull description : 判断对象为空或非空，使用接口的方式传入相应的方法并执行
     */
    public static class Breakfast<T> {

        public interface Cooker<E> {
            void cook(E exclusive);
        }

        private Cooker<T> whenNull = null;
        private Cooker<T> whenNotNull = null;

        private WeakReference<T> reference;

        public Breakfast(T object) {
            this.reference = new WeakReference<>(object);
        }

        public Breakfast<T> whenNull(Cooker<T> cooker) {
            this.whenNull = cooker;
            return this;
        }

        public Breakfast<T> whenNotNull(Cooker<T> cooker) {
            this.whenNotNull = cooker;
            return this;
        }

        public Breakfast eat() {
            T breakfast = reference.get();
            if (notNull(breakfast) && notNull(whenNotNull)) {
                whenNotNull.cook(breakfast);
            } else if (isNull(breakfast) && notNull(whenNull)) {
                whenNull.cook(null);
            }
            return this;
        }

        public Again again() {
            return new Again().another(reference.get());
        }

    }

}
