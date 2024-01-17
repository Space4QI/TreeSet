import java.util.Iterator;

public interface Navigator {
    Iterator iterator();

    void addRoute(Route route);

    void removeRoute(String routeId);

    boolean contains(Route route);

    int size();

    Route getRoute(String routeId);

    void chooseRoute(String routeId);

    MyTreeSet<Route> searchRoutes(String startPoint, String endPoint);

    Iterable<Route>getFavoriteRoutes(String destinationPoint);

    Iterable<Route>getTop3Routes();

     Iterable<Route> getRoutes();

    boolean containsRouteWithId(String routeId);
}
