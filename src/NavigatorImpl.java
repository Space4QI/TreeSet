import java.util.*;
import java.util.stream.Collectors;

public class NavigatorImpl implements Navigator {
    private TreeSet<Route> routes;

    public NavigatorImpl() {
        this.routes = new TreeSet<>(Comparator.comparing(Route::getId));
    }

    @Override
    public Iterator iterator() {
        return routes.iterator();
    }

    @Override
    public void addRoute(Route route) {
            routes.add(route);
    }

    @Override
    public void removeRoute(String routeId) {
        routes.removeIf(route -> route.getId().equals(routeId));
    }

    @Override
    public boolean contains(Route route) {
        return routes.contains(route);
    }

    @Override
    public int size() {
        return routes.size();
    }

    @Override
    public Route getRoute(String routeId) {
        for (Route route : routes) {
            if (route.getId().equals(routeId)) {
                return route;
            }
        }
        return null;
    }

    @Override
    public void chooseRoute(String routeId) {
        Route selectedRoute = getRoute(routeId);
        if (selectedRoute != null) {
            selectedRoute.setPopularity(selectedRoute.getPopularity() + 1);
        }
    }

    @Override
    public MyTreeSet<Route> searchRoutes(String startPoint, String endPoint) {
        MyTreeSet<Route> result = new MyTreeSet<>(Comparator
                .comparingInt((Route route) -> route.getLocationPoints().indexOf(startPoint))
                .thenComparingDouble(Route::getDistance)
                .thenComparingInt(Route::getPopularity)
                .reversed());

        for (Route route : routes) {
            List<String> locations = route.getLocationPoints();
            int startIndex = locations.indexOf(startPoint);
            int endIndex = locations.indexOf(endPoint);

            if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) {
                result.add(route);
            }
        }

        return result;
    }


    @Override
    public Iterable<Route> getFavoriteRoutes(String destinationPoint) {
        return routes.stream()
                .filter(route -> route.isFavorite() &&
                        route.getLocationPoints().stream()
                                .anyMatch(point -> point.equalsIgnoreCase(destinationPoint.toLowerCase())) &&
                        !route.getLocationPoints().get(0).equalsIgnoreCase(destinationPoint.toLowerCase()))
                .sorted(Comparator
                        .comparingDouble(Route::getDistance)
                        .thenComparingInt(Route::getPopularity)
                        .reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Route> getTop3Routes() {
        List<Route> result = new ArrayList<>(routes);

        result.sort(Comparator
                .comparingInt(Route::getPopularity)
                .reversed()
                .thenComparingDouble(Route::getDistance)
                .thenComparingInt(route -> route.getLocationPoints().size()));

        return result.subList(0, Math.min(result.size(), 5));
    }

    @Override
    public Iterable<Route> getRoutes() {
        return routes;
    }

    @Override
    public boolean containsRouteWithId(String routeId) {
        for (Route route : routes) {
            if (route.getId().equals(routeId)) {
                return true;
            }
        }
        return false;
    }

}
