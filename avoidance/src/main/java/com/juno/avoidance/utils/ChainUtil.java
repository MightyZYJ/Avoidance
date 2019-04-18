package com.juno.avoidance.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juno.
 * Date : 2019/4/16.
 * Time : 20:09.
 * When I met you in the summer.
 * Description :
 */
public class ChainUtil {

    public static EventStream begin() {
        return new EventStream();
    }

    /**
     * Created by Juno at 20:09, 2019/4/16.
     * EventStream description : 链式调用事件的类
     */
    public static class EventStream {

        private List<Situation> situations = new ArrayList<>();
        private List<Solution> solutions = new ArrayList<>();

        private Celebration celebration;
        private boolean stop;
        private boolean needStop = false;

        public interface Situation {
            boolean when();
        }

        public interface Solution {
            void react();
        }

        public interface Celebration {
            void celebrate();
        }


        public EventStream stopWhen(boolean stop) {
            this.stop = stop;
            needStop = true;
            return this;
        }

        public EventStream when(Situation situation) {
            situations.add(situation);
            return this;
        }

        public EventStream react(Solution solution) {
            solutions.add(solution);
            return this;
        }

        public EventStream doWhenSuccess(Celebration celebration) {
            this.celebration = celebration;
            return this;
        }

        public void flow() {
            if (situations.size() != solutions.size()) {
                return;
            }
            for (int i = 0; i < situations.size(); ++i) {
                Situation situation = situations.get(i);
                Solution solution = solutions.get(i);
                if (situation.when()) {
                    solution.react();
                    if (needStop && stop) {
                        return;
                    }
                } else if (needStop && !stop) {
                    return;
                }
            }
            if (celebration != null) {
                celebration.celebrate();
            }
        }

    }

}
