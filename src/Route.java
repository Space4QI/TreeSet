import java.util.List;
import java.util.Objects;

public class Route implements Comparable<Route> {
    private String id;
    private double distance;
    private int popularity;
    private boolean isFavorite;
    private List<String> locationPoints;

    public Route(String id, double distance, int popularity, boolean isFavorite, List<String> locationPoints) {
        this.id = id;
        this.distance = distance;
        this.popularity = popularity;
        this.isFavorite = isFavorite;
        this.locationPoints = locationPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return Double.compare(route.distance, distance) == 0 &&
                Objects.equals(locationPoints.getFirst(), route.locationPoints.getFirst()) &&
                Objects.equals(locationPoints.getLast(), route.locationPoints.getLast());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, distance, isFavorite, locationPoints);
    }

    @Override
    public int compareTo(Route other) {
        // Реализация сравнения для использования в TreeSet
        if (this.popularity != other.popularity) {
            return Integer.compare(other.popularity, this.popularity); // Убывающий порядок по популярности
        }
        if (this.distance != other.distance) {
            return Double.compare(this.distance, other.distance); // Возрастающий порядок по расстоянию
        }
        return this.id.compareTo(other.id); // Если популярность и расстояние равны, используем идентификатор
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public List<String> getLocationPoints() {
        return locationPoints;
    }

    public void setLocationPoints(List<String> locationPoints) {
        this.locationPoints = locationPoints;
    }

    public void incrementPopularity() {
        this.popularity++;
    }
}
