package com.juno.avoidance.utils;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 20:03.
 * When I met you in the summer.
 * Description : Fragment Util 以栈的方式存储、展示Fragment，并提供和ViewPager一起使用的方法
 */
@SuppressWarnings("unused")
public class FragmentUtil {

    /**
     * Created by Juno at 20:04, 2019/4/16.
     * FragmentChain description : 链式生成Fragment，用于ViewPager
     */
    public static class FragmentChain {

        private List<Fragment> fragments = new ArrayList<>();
        private FragmentManager fragmentManager;
        private List<Integer> list = new ArrayList<>();

        @IdRes
        private int containerId = 0;

        /**
         * Created by Juno at 18:47, 2019/4/17.
         * add description : 添加fragment
         */
        public FragmentChain add(Fragment fragment) {
            fragments.add(fragment);
            return this;
        }

        /**
         * Created by Juno at 19:20, 2019/4/17.
         * show description : 显示某一页
         */
        public FragmentChain show(int index) {
            if (index >= 0 && index < fragments.size()) {

                if (!list.contains(index)) {
                    fragmentManager.beginTransaction().add(containerId, fragments.get(index)).commit();
                    list.add(index);
                } else {
                    FragmentTransaction f = fragmentManager.beginTransaction();
                    for (int i = list.size() - 1; i >= 0; --i) {
                        if (list.get(i) != index) {
                            f.hide(fragments.get(list.get(i)));
                        } else {
                            break;
                        }
                    }
                    f.show(fragments.get(index)).commit();
                }

            }
            return this;
        }

        /**
         * Created by Juno at 19:02, 2019/4/17.
         * show description : 把第一个Fragment展示在container上
         */
        public FragmentChain show() {
            if (fragments.size() > 0) {
                fragmentManager.beginTransaction().add(containerId, fragments.get(0)).commit();
                list.add(0);
            }
            return this;
        }

        /**
         * Created by Juno at 19:18, 2019/4/17.
         * container description : 设置container
         */
        public FragmentChain container(@IdRes int id) {
            containerId = id;
            return this;
        }

        /**
         * Created by Juno at 18:47, 2019/4/17.
         * attach description : 绑定到ViewPager上
         */
        public FragmentChain attach(ViewPager viewpager) {
            FragmentAdapter adapter = new FragmentAdapter(fragmentManager, fragments);
            viewpager.setAdapter(adapter);
            return this;
        }

        /**
         * Created by Juno at 19:18, 2019/4/17.
         * manager description : 设置manager
         */
        public FragmentChain manager(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
            return this;
        }

        /**
         * Created by Juno at 20:07, 2019/4/16.
         * ChainTraversal description : 用于遍历Fragment列表
         */
        public interface ChainTraversal {
            void traversal(Fragment fragment);
        }

        public void traversal(ChainTraversal chainTraversal) {
            for (Fragment fragment : fragments) {
                chainTraversal.traversal(fragment);
            }
        }

        /**
         * Created by Juno at 18:47, 2019/4/17.
         * destroy description : 销毁
         */
        public void destroy() {
            fragments.clear();
            fragments = null;
            fragmentManager = null;
        }
    }

    /**
     * Created by Juno at 20:05, 2019/4/16.
     * FragmentAdapter description : Fragment用在ViewPager的适配器
     */
    public static class FragmentAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragmentList;

        FragmentAdapter(FragmentManager fm, List<Fragment> mFragmentList) {
            super(fm);
            this.mFragmentList = mFragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}
