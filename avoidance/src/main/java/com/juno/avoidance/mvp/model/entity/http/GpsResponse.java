package com.juno.avoidance.mvp.model.entity.http;

/**
 * Created by Juno.
 * Date : 2019/4/24.
 * Time : 2:00.
 * When I met you in the summer.
 * Description :
 */
public class GpsResponse {

    /**
     * result : {"latitude":23.035219192504883,"longitude":113.39820861816406,"info":"help"}
     */

    private ResultBean result;

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * latitude : 23.035219192504883
         * longitude : 113.39820861816406
         * info : help
         */

        private double latitude;
        private double longitude;
        private String info;

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
