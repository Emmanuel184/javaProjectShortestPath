public class city {

        String cityName;

        String parentCity;
    
        int costToConnectedCity;
    
        int timeToConnectedCity;
    
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
    
        public int getCost() {
            return this.costToConnectedCity;
        }
    
        public int getTime() {
            return this.timeToConnectedCity;
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
