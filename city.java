public class city {

        private String cityName;

        private String parentCity;
    
        private int costToConnectedCity;
    
        private int timeToConnectedCity;
    
        city(String cityName) {
            this.cityName = cityName;
    
            this.costToConnectedCity = 0;
    
            this.timeToConnectedCity = 0;
    
        }
    
        city(String cityName, int cost, int time) {
            this.cityName = cityName;
    
            this.costToConnectedCity = cost;
    
            this.timeToConnectedCity = time;
    
        }

        city(String cityName, String parentCityName, int cost, int time) {
            this.cityName = cityName;
            this.parentCity = parentCityName;
            this.costToConnectedCity = cost;
            this.timeToConnectedCity = time;
        }

        public void changeCost(int newCost) {
            this.costToConnectedCity = newCost;
        }

        public void changeTime(int newTime) {
            this.timeToConnectedCity = newTime;
        }
    
        public int getCost() {
            return this.costToConnectedCity;
        }
    
        public int getTime() {
            return this.timeToConnectedCity;
        }
        
        public String getCityName() {
            return this.cityName;
        }

        public String getParentCityName() {
            return this.getParentCityName();
        }
    
        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
    
            if (o == null) {
                return false;
            }
    
            if (getClass() != o.getClass()) {
                return false;
            }
    
            city comparedCity = (city) o;
    
            return cityName.equals(comparedCity.cityName);
        }
    
        @Override
        public String toString() {
    
            return this.cityName;
    
        }
}
