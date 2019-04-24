package com.juno.avoidance.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import timber.log.Timber;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 20:13.
 * When I met you in the summer.
 * Description : 作用在一般对象上的方法
 * <a href="http://www.orz6.com/lattice.aspx">35px</a>
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ObjectUtil {

    /**
     * Created by Juno at 14:51, 2019/4/22.
     * Java description : 类可以用于任意JDK环境
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Java {
        String value() default "Class depends on Jdk.";
    }

    /**
     * Created by Juno at 14:51, 2019/4/22.
     * Android description : 类仅能用于Android
     */
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.SOURCE)
    public @interface Android {
        String value() default "Class depends on Android.";
    }

    public static final String OBJECT_UTIL = "@ObjectUtil";

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
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Created by Juno at 21:06, 2019/4/16.
     * single description : 若对象不为空则执行某方法，返回原对象
     */
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
     * Created by Juno at 18:16, 2019/4/21.
     * printf description : 本类中所有打印的入口，方便切换打印方式
     */
    public static void printf(String msg, Object... args) {
        Timber.e(msg, args);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Created by Juno at 18:06, 2019/4/21.
     * again description : 以下为方便调用内部类的静态方法
     */
    public static <T> Again<T> again(T object) {
        return Again.from(object);
    }


    public interface AgainMaker<E> {
        default <K> Again<K> again(K o) {
            return new Again<K>().ref(new WeakReference<>(o));
        }

        default Again<E> again() {
            return new Again<>(); //记得重写保持泛型
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Created by Juno at 20:12, 2019/4/21.
     * breakfast description : breakfast的静态方法和接口
     */
    public static <T> Breakfast<T> breakfast(T object) {
        return Breakfast.start(object);
    }

    public interface BreakfastMaker<E> {
        default <K> Breakfast<K> breakfast(K o) {
            return new Breakfast<>(o);
        }

        default Breakfast<E> breakfast() {
            return new Breakfast<>(null); //记得重写保持泛型
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Created by Juno at 20:12, 2019/4/21.
     * chain description : Chain
     */
    public static Chain<String> chain() {
        return Chain.start();
    }

    public static <T> Chain<T> chain(T object) {
        return new Chain<>(object);
    }

    public interface ChainMaker<E> {
        default <K> Chain<K> chain(K o) {
            return new Chain<>(o);
        }

        default Chain<E> chain() {
            return new Chain<>(null); //记得重写保持泛型
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Created by Juno at 20:13, 2019/4/21.
     * daydream description : Daydream
     */
    public static Daydream daydream(String method) {
        return new Daydream(method);
    }

    public interface DaydreamMaker<T> {
        T wake();

        default T watch() {
            Daydream.begin(Daydream.DAYDREAM);
            return wake();
        }

        default T unwatch() {
            Daydream.end();
            return wake();
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Created by Juno at 15:31, 2019/4/22.
     * fork description : Fork
     * 静态方法fork(T)单纯获取一个动态代理，没有实际意义，已经弃用
     */
    @Deprecated
    public static <T> T fork(T object) {
        return Fork.fork(object);
    }

    public interface ForkMaker<E> {
        default <K> Fork<K> fork(K o) {
            return new Fork<>(o);
        }

        Fork<E> fork();
    }

    ////          ////                                       ////
    ////         /////                                       ////
    ////         //////                                      ////
    ////         //////
    ////         //////
    ////        ///////
    ////        ////////        ///// ////   ///////         ////       /// //////
    ////        ////////      ////////////  //////////       ////       ///////////
    ////       //// ////      //// ////    /////  ////       ////       /////   ////
    ////       ////  ////    ////   ////   ////    ///       ////       ////    ////
    ////       ////  ////    ////   ////          ////       ////       ////    ////
    ////       //////////     ///   ///       ////////       ////       ////    ////
    ////      ///////////     //// ////     //////////       ////       ///     ////
    ////      ////    ////    ////////     ////    ///       ////       ///     ////
    ////      ////    ////   /// ///       ///     ///       ////       ///     ////
    ////     ////     ////   ////         ////    ////       ////       ///     ////
    ////     ////      ////  //////////    //// //////       ////       ///     ////
    ////     ////      ////   ///////////  ////////////      ////       ///     ////
    ////                     ///     ////    /////   //
    ////                    ////      ///
    ////                     ////    ////
    ////                     ///////////

    public interface IAgain<T> {
        String AGAIN = "@ObjectUtil$Again";

        <E> Again<E> another(E e);

        <E> Again<E> another(E e, String methodName, Object... args);

        T get();

        Again<T> get(Again.Getter<T> getter);

        Again<T> lazy(Again.Lazier<T> getter);

        Again<T> args(Object... args);

        Again<T> cache(boolean declared);

        Again<T> ref(WeakReference<T> weakReference);

        Again<T> send();

        Again<T> receive(String methodName);

        Again<T> next(String methodName, Object... args);

        Again<T> same(Object... args);

        Again<T> still(Again.Action action);

        Again<T> when(String methodName, Object... args);

        <From, To> Again<To> map(Again.Mapper<? super T, ? extends To> mapper);

        void clean();
    }

    @Java("强化泛型，使用反射来进行链式调用代码")
    public static class Again<T> implements IAgain<T>, BreakfastMaker<T>, ChainMaker<T>, DaydreamMaker<Again<T>>, ForkMaker<IAgain<T>> {

        public interface Getter<I> {
            void get(I o);
        }

        public interface Lazier<I> {
            I lazy();
        }

        public interface Mapper<Source, Target> {
            Target convert(Source source);
        }

        public interface Action {
            void action();
        }

        private static WeakReference<Object[]> sArgCache;

        private WeakReference<T> reference;
        private Method[] methods = null;
        private Method lastMethod = null;
        private boolean watched = false;

        /**
         * Created by Juno at 19:55, 2019/4/17.
         * Action description : 静态方法at开始调用链
         */
        public static Again<String> at(Action action) {
            return new Again<String>().ref(new WeakReference<>("调用at()中")).still(action);
        }

        /**
         * Created by Juno at 14:09, 2019/4/17.
         * from description : 传入对象初始化并执行方法
         */
        public static <E> Again<E> from(E o, String methodName, Object... args) {
            return new Again<E>().ref(new WeakReference<>(o)).next(methodName, args);
        }

        /**
         * Created by Juno at 14:09, 2019/4/17.
         * from description : 仅初始化
         */
        public static <E> Again<E> from(E o) {
            return new Again<E>().ref(new WeakReference<>(o));
        }

        /**
         * Created by Juno at 11:16, 2019/4/17.
         * another description : 开启另一个调用调用链
         */
        @Override
        public <E> Again<E> another(E o) {
            printf("ObjectUtil --> %s 调用%s",
                    isNull(reference)
                            ? isNull(o) ? "null" : o.getClass().getCanonicalName()
                            : isNull(reference.get()) ? "wrf get Null" : reference.get().getClass().getSimpleName()
                    , isNull(reference) ? "开始" : "完毕");
            return from(o);
        }

        /**
         * Created by Juno at 11:16, 2019/4/17.
         * another description : 开启另一个调用调用链并执行方法
         */
        @Override
        public <E> Again<E> another(E o, String method, Object... args) {
            return another(o).next(method, args);
        }

        /**
         * Created by Juno at 11:20, 2019/4/17.
         * get description : 以下两种方式返回对象
         */
        @Override
        public T get() {
            return reference.get();
        }

        @Override
        public Again<T> get(Getter<T> getter) {
            getter.get(reference.get());
            return this;
        }

        /**
         * Created by Juno at 14:09, 2019/4/17.
         * is description : 对象为空时赋值
         */
        @Override
        public Again<T> lazy(Lazier<T> lazier) {
            T o = reference.get();
            if (isNull(o)) {
                o = lazier.lazy();
                ref(new WeakReference<>(o));
            }
            return this;
        }

        /**
         * Created by Juno at 21:35, 2019/4/17.
         * cache description : 缓存外部对象，用于when和receive
         */
        @Override
        public Again<T> args(Object... o) {
            sArgCache = new WeakReference<>(o);
            return this;
        }

        /**
         * Created by Juno at 9:13, 2019/4/17.
         * cache description : 开启方法缓存，参数：是否为定义方法
         */
        @Override
        public Again<T> cache(boolean declared) {
            T o = reference.get();
            if (notNull(o)) {
                methods = declared ? o.getClass().getDeclaredMethods() : o.getClass().getMethods();
            }
            return this;
        }

        /**
         * Created by Juno at 16:07, 2019/4/17.
         * ref description : 设置引用
         */
        @Override
        public Again<T> ref(WeakReference<T> reference) {
            this.reference = reference;
            return this;
        }

        /**
         * Created by Juno at 21:35, 2019/4/17.
         * send description : 缓存自身的引用对象，用于when和receive
         */
        @Override
        public Again<T> send() {
            Object[] objects = new Object[1];
            objects[0] = reference.get();
            sArgCache = new WeakReference<>(objects);
            return this;
        }

        /**
         * Created by Juno at 21:37, 2019/4/17.
         * receive description : 使用give保存的临时变量来入参，执行后不会清空缓存
         */
        @Override
        public Again<T> receive(String method) {
            if (notNull(sArgCache) && notNull(sArgCache.get())) {
                next(method, sArgCache.get());
            }
            return this;
        }

        /**
         * Created by Juno at 9:11, 2019/4/17.
         * next description : 查看是否有缓存方法，有缓存就读取出来，没有就调用{@link #single(Object, String, Object...)}
         */
        @Override
        public Again<T> next(String methodName, Object... args) {
            T o = reference.get();
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

        /**
         * Created by Juno at 9:55, 2019/4/17.
         * same description : 执行上一次执行的方法
         */
        @Override
        public Again<T> same(Object... args) {
            T o = reference.get();
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
         * Created by Juno at 13:39, 2019/4/22.
         * still description : 延续调用链
         */
        @Override
        public Again<T> still(Action action) {
            action.action();
            return this;
        }

        /**
         * Created by Juno at 14:08, 2019/4/18.
         * when description : 若缓存的是boolean类型，则执行方法，执行后不会清空缓存
         */
        @Override
        public Again<T> when(String method, Object... args) {
            if (notNull(sArgCache) //缓存不为空(WeakReference<Object[]>!=null)
                    && notNull(sArgCache.get()) //缓存的引用对象不为空(Object[]!=null)
                    && notNull(sArgCache.get()[0]) //缓存的引用对象数组的第一个元素不为空(Object[0]!=null)
                    && sArgCache.get()[0] instanceof Boolean //元素为布尔类型(Object[0] instanceof boolean)
                    && ((Boolean) sArgCache.get()[0])) { //元素为真 (Object[0])
                next(method, args);
            }
            return this;
        }

        /**
         * Created by Juno at 15:53, 2019/4/18.
         * map description : 用于转换
         */
        @Override
        public <From, To> Again<To> map(Mapper<? super T, ? extends To> mapper) {
            T o = reference.get();
            if (notNull(o) && notNull(mapper)) {
                To t = mapper.convert(o);
                return new Again<To>().ref(new WeakReference<>(t));
            }
            return new Again<To>().ref(new WeakReference<>(null));
        }

        /**
         * Created by Juno at 14:13, 2019/4/18.
         * done description : 清空缓存
         */
        @Override
        public void clean() {
            lastMethod = null;
            methods = null;
            sArgCache = null;
            if (watched) {
                unwatch();
            }
        }


        /**
         * Created by Juno at 20:22, 2019/4/21.
         * wake description : 以下三个是来自Daydream的方法
         */
        @Override
        public Again<T> wake() {
            return this;
        }

        /**
         * Created by Juno at 19:00, 2019/4/21.
         * watch / unwatch description : 性能监控相关，watch开始监控
         */
        @Override
        public Again<T> watch() {
            watched = true;
            Daydream.begin("性能监控");
            return this;
        }

        /**
         * Created by Juno at 19:00, 2019/4/21.
         * watch / unwatch description : 性能监控相关，unwatch停止监控
         */
        @Override
        public Again<T> unwatch() {
            watched = false;
            Daydream.end();
            return this;
        }

        /**
         * Created by Juno at 18:03, 2019/4/21.
         * breakfast description : 返回一个Breakfast对象
         */
        @Override
        public Breakfast<T> breakfast() {
            return new Breakfast<>(reference.get());
        }

        /**
         * Created by Juno at 18:03, 2019/4/21.
         * chain description : 返回一个Chain对象
         */
        @Override
        public Chain<T> chain() {
            return new Chain<>(reference.get());
        }

        /**
         * Created by Juno at 15:39, 2019/4/22.
         * fork description : 返回一个Fork对象
         */
        @Override
        public Fork<IAgain<T>> fork() {
            return new Fork<>(this);
        }
    }


    ////     ///////////                                                ////               ///////
    ////     ////////////                                               ////              ////////
    ////     ////    /////                                              ////             ////                                        ///
    ////     ////     ////                                              ////             ////                                        ///
    ////     ////     ////                                              ////             ////                                        ///
    ////     ////     ////                                              ////             ////                                        ///
    ////     ////     ////    //// /////      //////       ///////      ////   ///// ////////////      ///////       ///////     ////////////
    ////     ////   /////     //////////    //////////    //////////    ////  ////   ////////////     //////////    //////////   ////////////
    ////     ///////////      ///////      /////  ////   /////  ////    //// ////        ////        /////  ////   /////  ////       ///
    ////     ////////////     //////       ////    ////  ////    ///    ////////         ////        ////    ///   ////    ///       ///
    ////     ////     ////    /////        ///     ////         ////    ////////         ////               ////   ////              ///
    ////     ////     ////    ////        /////////////     ////////    ////////         ////           ////////    ///////          ///
    ////     ////      ////   ////        ////            //////////    /////////        ////         //////////     /////////       ///
    ////     ////      ////   ////        ////           ////    ///    ////  ///        ////        ////    ///          ////       ///
    ////     ////     /////   ////         ////    ////  ///     ///    ////  ////       ////        ///     ///           ////      ///
    ////     ////     ////    ////         ////    //// ////    ////    ////   ////      ////       ////    ////   ////    ////      ////
    ////     /////////////    ////          ///// ////   //// //////    ////   ////      ////        //// //////   /////  ////       ////  ///
    ////     ////////////     ////           ////////    ////////////   ////    ////     ////        ////////////   //////////        ////////
    ////                                       ////        /////   //                                  /////   //      /////            /////

    public interface IBreakfast<T> {
        String BREAKFAST = "@ObjectUtil$Breakfast";

        Breakfast<T> eat();

        Breakfast<T> whenNull(Breakfast.Cooker<T> cooker);

        Breakfast<T> whenNotNull(Breakfast.Cooker<T> cooker);
    }

    @Java("判断对象为空或非空，使用接口的方式传入相应的方法并执行")
    public static class Breakfast<T> implements IBreakfast<T>, AgainMaker<T>, ChainMaker, DaydreamMaker<Breakfast<T>> {

        public interface Cooker<I> {
            void cook(I exclusive);
        }

        private WeakReference<T> reference;
        private Cooker<T> whenNull = null;
        private Cooker<T> whenNotNull = null;

        /**
         * Created by Juno at 13:55, 2019/4/22.
         * Breakfast description : 构造方法
         */
        public Breakfast(T object) {
            this.reference = new WeakReference<>(object);
        }

        /**
         * Created by Juno at 17:50, 2019/4/21.
         * start description : 开启Breakfast
         */
        public static <E> Breakfast<E> start(E object) {
            return new Breakfast<>(object);
        }

        /**
         * Created by Juno at 17:51, 2019/4/21.
         * eat description : 执行代码，不能省略
         */
        @Override
        public Breakfast<T> eat() {
            T breakfast = reference.get();
            if (notNull(breakfast) && notNull(whenNotNull)) {
                whenNotNull.cook(breakfast);
            } else if (isNull(breakfast) && notNull(whenNull)) {
                whenNull.cook(null);
            }
            return this;
        }

        /**
         * Created by Juno at 17:51, 2019/4/21.
         * whenNull description : 当对象为空时执行
         */
        @Override
        public Breakfast<T> whenNull(Cooker<T> cooker) {
            this.whenNull = cooker;
            return this;
        }

        /**
         * Created by Juno at 17:51, 2019/4/21.
         * whenNotNull description : 当对象不为空时执行
         */
        @Override
        public Breakfast<T> whenNotNull(Cooker<T> cooker) {
            this.whenNotNull = cooker;
            return this;
        }

        /**
         * Created by Juno at 17:48, 2019/4/21.
         * again description : 返回一个Again对象，保持泛型
         */
        @Override
        public Again<T> again() {
            return new Again<T>().ref(new WeakReference<>(reference.get()));
        }

        /**
         * Created by Juno at 20:24, 2019/4/21.
         * wake description : 用于Daydream，延续调用链
         */
        @Override
        public Breakfast<T> wake() {
            return this;
        }

    }

    ////         ///////     ///                             ////
    ////       //////////    ///                             ////
    ////       ////   ////   ///                             ////
    ////      ////    ////   ///
    ////      ////     ////  ///
    ////      ////     ////  ///
    ////     /////           /// //////      ///////         ////       /// //////
    ////     ////            ///////////    //////////       ////       ///////////
    ////     ////            /////   ////  /////  ////       ////       /////   ////
    ////     ////            ////    ////  ////    ///       ////       ////    ////
    ////     ////            ////    ////         ////       ////       ////    ////
    ////     ////      ////  ////    ////     ////////       ////       ////    ////
    ////     /////     ////  ///     ////   //////////       ////       ///     ////
    ////      ////     ////  ///     ////  ////    ///       ////       ///     ////
    ////      ////    ////   ///     ////  ///     ///       ////       ///     ////
    ////      /////   ////   ///     //// ////    ////       ////       ///     ////
    ////       //////////    ///     ////  //// //////       ////       ///     ////
    ////        ////////     ///     ////  ////////////      ////       ///     ////
    ////          ////                       /////   //

    public interface IChain<K> {
        String CHAIN = "@ObjectUtil$Chain";

        Chain<K> stopWhen(boolean stop);

        Chain.ChainSituation when(Chain.Situation situation);

        Chain<K> doWhenSuccess(Chain.Celebration<K> celebration);

        Chain<K> flow();

        interface IChainSituation<S> {
            Chain<S> react(Chain.Solution solution);
        }
    }

    @Java("链式调用事件的类，处理频繁的if-else，（when -> react）")
    public static class Chain<T> implements IChain<T>, AgainMaker<T>, BreakfastMaker<T>, DaydreamMaker<Chain<T>> {

        public interface Situation {
            boolean when();
        }

        public interface Solution {
            void react();
        }

        public interface Celebration<I> {
            void celebrate(I nullableObject);
        }

        public class ChainSituation<E> implements IChainSituation<T> {
            /**
             * Created by Juno at 17:53, 2019/4/21.
             * react description : 设置当表达式为真时的行动
             */
            @Override
            public Chain<T> react(Solution solution) {
                solutions.add(solution);
                return Chain.this;
            }
        }

        private T object;
        private ChainSituation<T> chainSituation = new ChainSituation<>();
        private List<Situation> situations = new ArrayList<>();
        private List<Solution> solutions = new ArrayList<>();
        private Celebration<T> celebration = null;
        private boolean needStop = false;
        private boolean stop;

        /**
         * Created by Juno at 14:23, 2019/4/22.
         * Chain description : 构造方法
         */
        public Chain(T object) {
            this.object = object;
        }

        /**
         * Created by Juno at 17:55, 2019/4/21.
         * start description : 用静态方法新建Chain对象
         */
        public static Chain<String> start() {
            return new Chain<>(CHAIN);
        }

        /**
         * Created by Juno at 17:52, 2019/4/21.
         * stopWhen description : 当调用该方法的时候，调用链遇到为真或为假的表达式即会停止执行
         */
        @Override
        public Chain<T> stopWhen(boolean stop) {
            this.stop = stop;
            needStop = true;
            return this;
        }

        /**
         * Created by Juno at 17:53, 2019/4/21.
         * when description : 设置表达式
         */
        @Override
        public ChainSituation<T> when(Situation situation) {
            situations.add(situation);
            return chainSituation;
        }

        /**
         * Created by Juno at 17:53, 2019/4/21.
         * doWhenSuccess description : 当调用链执行完毕后调用此方法中的celebration
         */
        @Override
        public Chain<T> doWhenSuccess(Celebration<T> celebration) {
            this.celebration = celebration;
            return this;
        }

        /**
         * Created by Juno at 17:54, 2019/4/21.
         * flow description : 调用链开始执行，不能省略该方法
         */
        @Override
        public Chain<T> flow() {
            if (situations.size() != solutions.size()) {
                return this;
            }
            for (int i = 0; i < situations.size(); ++i) {
                Situation situation = situations.get(i);
                Solution solution = solutions.get(i);
                if (situation.when()) {
                    solution.react();
                    if (needStop && stop) {
                        return this;
                    }
                } else if (needStop && !stop) {
                    return this;
                }
            }
            if (notNull(celebration)) {
                celebration.celebrate(object);
            }
            celebration = null;
            situations = null;
            solutions = null;
            object = null;
            return this;
        }

        /**
         * Created by Juno at 17:59, 2019/4/21.
         * breakfast description : 返回一个Breakfast对象
         */
        @Override
        public Breakfast<T> breakfast() {
            return new Breakfast<>(object);
        }

        /**
         * Created by Juno at 18:01, 2019/4/21.
         * again description : 返回一个Again对象
         */
        @Override
        public Again<T> again() {
            return new Again<T>().ref(new WeakReference<>(object));
        }

        /**
         * Created by Juno at 20:24, 2019/4/21.
         * wake description : 用于Daydream，延续调用链
         */
        @Override
        public Chain<T> wake() {
            return this;
        }

    }

    ////     //////////                                          ////
    ////     ///////////                                         ////
    ////     ////   /////                                        ////
    ////     ////    /////                                       ////
    ////     ////     ////                                       ////
    ////     ////     ////                                       ////
    ////     ////     ////     ///////    ////     ////    //////////    //// /////      //////        ///////    /////////////
    ////     ////     ////    //////////   ////    ////   ///////////    //////////    //////////     //////////  //////////////
    ////     ////     /////  /////  ////   ////    ////  ////   /////    ///////      /////  ////    /////  ////  //// /////////
    ////     ////     /////  ////    ///   ////   ////   ////   /////    //////       ////    ////   ////    ///  //// //// ////
    ////     ////     /////         ////    ////  ////  ////     ////    /////        ///     ////          ////  ///  //// ////
    ////     ////     ////      ////////    ////  ///   ////     ////    ////        /////////////      ////////  ///  //// ////
    ////     ////     ////    //////////     /// ////   ////     ////    ////        ////             //////////  ///  //// ////
    ////     ////     ////   ////    ///     ////////   ////     ////    ////        ////            ////    ///  ///  //// ////
    ////     ////    /////   ///     ///     ///////    ////     ////    ////         ////    ////   ///     ///  ///  //// ////
    ////     ////   /////   ////    ////      //////     ////   /////    ////         ////    ////  ////    ////  ///  //// ////
    ////     ///////////     //// //////      //////     ///// //////    ////          ///// ////    //// //////  ///  //// ////
    ////     //////////      ////////////     /////       ///////////    ////           ////////     //////////// ///  //// ////
    ////                       /////   //      ////         ////                          ////         /////   //
    ////                                       ///
    ////                                   ///////
    ////                                   //////

    @Java("性能监控，调用begin和end，打印使用时间")
    public static class Daydream {

        private static final String DAYDREAM = "@Object$DayDream";

        private long begin;
        private String serviceMethod;

        private static final ThreadLocal<Daydream> performanceRecord = new ThreadLocal<>();

        /**
         * Created by Juno at 18:43, 2019/4/21.
         * begin description : 传入方法名开始
         */
        public static void begin(String method) {
            printf(DAYDREAM + " begin monitor");
            Daydream methodPerformance = new Daydream(method);
            performanceRecord.set(methodPerformance);
        }

        /**
         * Created by Juno at 18:43, 2019/4/21.
         * end description : 停止
         */
        public static void end() {
            printf(DAYDREAM + " end monitor");
            Daydream methodPerformance = performanceRecord.get();
            if (notNull(methodPerformance)) {
                methodPerformance.printPerformance();
            }
        }

        /**
         * Created by Juno at 18:43, 2019/4/21.
         * DayDream description : 开始计时
         */
        private Daydream(String serviceMethod) {
            this.serviceMethod = serviceMethod;
            begin = System.currentTimeMillis();
        }

        /**
         * Created by Juno at 20:37, 2019/4/21.
         * printPerformance description : 定制计时并打印
         */

        private void printPerformance() {
            long end = System.currentTimeMillis();
            printf(DAYDREAM + " " + serviceMethod + " cost " + (end - begin) + " millis.");
        }
    }

    ////      ////////////                     ////
    ////      ////////////                     ////
    ////      ////                             ////         ///            ///
    ////      ////                                          ///            ///
    ////      ////                                          ///            ///
    ////      ////                                          ///            ///
    ////      ////          /////////////      ////     ////////////   ////////////      //////       //// /////
    ////      ////          //////////////     ////     ////////////   ////////////    //////////     //////////
    ////      ///////////   //// /////////     ////         ///            ///        /////  ////     ///////
    ////      ///////////   //// //// ////     ////         ///            ///        ////    ////    //////
    ////      ////          ///  //// ////     ////         ///            ///        ///     ////    /////
    ////      ////          ///  //// ////     ////         ///            ///       /////////////    ////
    ////      ////          ///  //// ////     ////         ///            ///       ////             ////
    ////      ////          ///  //// ////     ////         ///            ///       ////             ////
    ////      ////          ///  //// ////     ////         ///            ///        ////    ////    ////
    ////      ////          ///  //// ////     ////         ////           ////       ////    ////    ////
    ////      ////////////  ///  //// ////     ////         ////  ///      ////  ///   ///// ////     ////
    ////      ////////////  ///  //// ////     ////          ////////       ////////    ////////      ////
    ////                                                       /////          /////       ////

    @Android("简化线程切换的逻辑,https://blog.csdn.net/qiujuer/article/details/41599383")
    public static class Emitter {

        private static final class SyncRunnable {

            private boolean end = false;
            private Runnable runnable;

            SyncRunnable(Runnable runnable) {
                this.runnable = runnable;
            }

            public void run() {
                synchronized (this) {
                    runnable.run();
                    end = true;
                    try {
                        this.notifyAll();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            public void waitRun() {
                if (!end) {
                    synchronized (this) {
                        if (!end) {
                            try {
                                this.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        private static final class HandlerPoster extends Handler {

            private final int ASYNC = 0x1;
            private final int SYNC = 0x2;
            private final Queue<Runnable> asyncPool;
            private final Queue<SyncRunnable> syncPool;
            private final int maxMillisInsideHandleMessage;
            private boolean asyncActive;
            private boolean syncActive;

            HandlerPoster(Looper looper, int maxMillisInsideHandleMessage) {
                super(looper);
                this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
                asyncPool = new LinkedList<>();
                syncPool = new LinkedList<>();
            }

            void dispose() {
                this.removeCallbacksAndMessages(null);
                this.asyncPool.clear();
                this.syncPool.clear();
            }

            void async(Runnable runnable) {
                synchronized (asyncPool) {
                    asyncPool.offer(runnable);
                    if (!asyncActive) {
                        asyncActive = true;
                        if (!sendMessage(obtainMessage(ASYNC))) {
                            throw new RuntimeException("Could not send handler message");
                        }
                    }
                }
            }

            void sync(SyncRunnable post) {
                synchronized (syncPool) {
                    syncPool.offer(post);
                    if (!syncActive) {
                        syncActive = true;
                        if (!sendMessage(obtainMessage(SYNC))) {
                            throw new RuntimeException("Could not send handler message");
                        }
                    }
                }
            }

            @Override
            public void handleMessage(Message msg) {
                if (msg.what == ASYNC) {
                    boolean rescheduled = false;
                    try {
                        long started = SystemClock.uptimeMillis();
                        while (true) {
                            Runnable runnable = asyncPool.poll();
                            if (runnable == null) {
                                synchronized (asyncPool) {
                                    // Check again, this time in synchronized
                                    runnable = asyncPool.poll();
                                    if (runnable == null) {
                                        asyncActive = false;
                                        return;
                                    }
                                }
                            }
                            runnable.run();
                            long timeInMethod = SystemClock.uptimeMillis() - started;
                            if (timeInMethod >= maxMillisInsideHandleMessage) {
                                if (!sendMessage(obtainMessage(ASYNC))) {
                                    throw new RuntimeException("Could not send handler message");
                                }
                                rescheduled = true;
                                return;
                            }
                        }
                    } finally {
                        asyncActive = rescheduled;
                    }
                } else if (msg.what == SYNC) {
                    boolean rescheduled = false;
                    try {
                        long started = SystemClock.uptimeMillis();
                        while (true) {
                            SyncRunnable post = syncPool.poll();
                            if (post == null) {
                                synchronized (syncPool) {
                                    // Check again, this time in synchronized
                                    post = syncPool.poll();
                                    if (post == null) {
                                        syncActive = false;
                                        return;
                                    }
                                }
                            }
                            post.run();
                            long timeInMethod = SystemClock.uptimeMillis() - started;
                            if (timeInMethod >= maxMillisInsideHandleMessage) {
                                if (!sendMessage(obtainMessage(SYNC))) {
                                    throw new RuntimeException("Could not send handler message");
                                }
                                rescheduled = true;
                                return;
                            }
                        }
                    } finally {
                        syncActive = rescheduled;
                    }
                } else super.handleMessage(msg);
            }
        }

    }

    ////      /////////////                              ////
    ////      /////////////                              ////
    ////      ////                                       ////
    ////      ////                                       ////
    ////      ////                                       ////
    ////      ////                                       ////
    ////      ////              //////      //// /////   ////   /////
    ////      ////            //////////    //////////   ////  ////
    ////      //////////     /////  ////    ///////      //// ////
    ////      //////////     ////    ////   //////       ////////
    ////      ////          ////     ////   /////        ////////
    ////      ////          ////      ////  ////         ////////
    ////      ////          ////      ////  ////         /////////
    ////      ////          ////      ///   ////         ////  ///
    ////      ////           ////    ////   ////         ////  ////
    ////      ////           ////    ////   ////         ////   ////
    ////      ////            //////////    ////         ////   ////
    ////      ////             ////////     ////         ////    ////

    public interface IFork<K> {

        String FORK = "@Object$Fork";

        K get();

        Fork<K> setBefore(Fork.Before before);

        Fork<K> setAfter(Fork.After after);

        Fork<K> setThrowing(Fork.Throwing throwing);
    }

    @Java("使用JDK自带的Proxy来获取一个动态代理（对接口进行代理），并提供方法设置增强")
    public static class Fork<T> implements IFork<T>, InvocationHandler, DaydreamMaker<Fork<T>> {

        public interface Before {
            boolean before(Object proxy, Method method, Object... args);
        }

        public interface After {
            void after(Object result);
        }

        public interface Throwing {
            void throwing(Throwable t);
        }

        private T target;
        private T proxy = null;
        private Before before = null;
        private After after = null;
        private Throwing throwing = null;

        /**
         * Created by Juno at 15:25, 2019/4/22.
         * Fork description : 构造方法，传入被代理的对象
         */
        public Fork(T target) {
            this.target = target;
        }

        /**
         * Created by Juno at 15:27, 2019/4/22.
         * fork description : 以静态方法的形式返回一个代理对象
         */
        @SuppressWarnings("unchecked")
        public static <E> E fork(Object target) {
            Object o = Proxy.newProxyInstance(target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(),
                    new Fork(target));
            return (E) o;
        }

        /**
         * Created by Juno at 15:25, 2019/4/22.
         * fork description : 获取代理
         */
        @Override
        @SuppressWarnings("unchecked")
        public T get() {
            if (isNull(proxy)) {
                proxy = (T) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                        target.getClass().getInterfaces(), this);
            }
            return proxy;
        }

        /**
         * Created by Juno at 15:26, 2019/4/22.
         * setBefore description : 设置前置增强
         */
        @Override
        public Fork<T> setBefore(Before before) {
            this.before = before;
            return this;
        }

        /**
         * Created by Juno at 15:26, 2019/4/22.
         * setAfter description : 设置后置增强
         */
        @Override
        public Fork<T> setAfter(After after) {
            this.after = after;
            return this;
        }

        /**
         * Created by Juno at 15:26, 2019/4/22.
         * setThrowing description : 设置抛出增强
         */
        @Override
        public Fork<T> setThrowing(Throwing throwing) {
            this.throwing = throwing;
            return this;
        }

        /**
         * Created by Juno at 15:22, 2019/4/22.
         * invoke description : 拦截方法的执行
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) {
            Object result = null;
            boolean invoke = true;

            if (notNull(before)) { //前置
                invoke = before.before(proxy, method, args);
            }

            if (invoke) { //执行
                try {
                    result = method.invoke(target, args);
                } catch (Exception e) {
                    if (notNull(throwing)) { //抛出
                        throwing.throwing(e);
                    }
                    e.printStackTrace();
                }
            }

            if (notNull(after)) { //后置
                after.after(result);
            }

            return result;
        }

        /**
         * Created by Juno at 15:57, 2019/4/22.
         * wake description : 用于Daydream
         */
        @Override
        public Fork<T> wake() {
            return this;
        }

    }

}
