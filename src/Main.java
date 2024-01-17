import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Navigator navigator = new NavigatorImpl();

        Route route1 = new Route("1", 100.0, 3, true, Arrays.asList("CityA", "CityB", "CityC"));
        Route route2 = new Route("2", 150.0, 5, false, Arrays.asList("CityA", "CityD", "CityE", "CityF", "CityC"));
        Route route3 = new Route("3", 80.0, 2, true, Arrays.asList("CityB", "CityG"));
        Route route4 = new Route("4", 120.0, 4, false, Arrays.asList("CityD", "CityH", "CityI", "CityJ"));
        Route route5 = new Route("5", 100.0, 3, true, Arrays.asList("CityA", "CityK"));

        navigator.addRoute(route1);
        navigator.addRoute(route2);
        navigator.addRoute(route3);
        navigator.addRoute(route4);
        navigator.addRoute(route5);

        while (true) {
            try {
                System.out.println("\nВыберите действие:");
                System.out.println("1. Добавить маршрут");
                System.out.println("2. Удалить маршрут");
                System.out.println("3. Показать все маршруты");
                System.out.println("4. Поиск маршрутов");
                System.out.println("5. Избранные маршруты");
                System.out.println("6. Топ-5 маршрутов");
                System.out.println("7. Поиск маршрута по id");
                System.out.println("8. Выйти");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Чтение лишнего символа после числа

                switch (choice) {
                    case 1:
                        addRoute(scanner, navigator);
                        break;
                    case 2:
                        removeRoute(scanner, navigator);
                        break;
                    case 3:
                        showAllRoutes(navigator);
                        break;
                    case 4:
                        searchRoutes(scanner, navigator);
                        break;
                    case 5:
                        showFavoriteRoutes(scanner, navigator);
                        break;
                    case 6:
                        showTopRoutes(navigator);
                        break;
                    case 7:
                        searchRouteById(scanner, navigator);
                        break;
                    case 8:
                        System.out.println("Программа завершена.");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Некорректный ввод. Пожалуйста, выберите действие от 1 до 7.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка ввода. Пожалуйста, введите корректное значение.");
                scanner.nextLine(); // Очистка буфера ввода
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private static void addRoute(Scanner scanner, Navigator navigator) {
        try {
            System.out.println("Введите идентификатор маршрута:");
            String id = scanner.nextLine();

            if (navigator.containsRouteWithId(id)) {
                System.out.println("Маршрут с таким идентификатором уже существует.");
                return;
            }

            System.out.println("Введите расстояние маршрута:");
            double distance = scanner.nextDouble();
            scanner.nextLine(); // Чтение лишнего символа после числа

            System.out.println("Введите популярность маршрута:");
            int popularity = scanner.nextInt();
            scanner.nextLine(); // Чтение лишнего символа после числа

            String favoriteInput;
            boolean isFavorite;
            do {
                System.out.println("Является ли маршрут избранным? (true/false):");
                favoriteInput = scanner.nextLine().toLowerCase();
                isFavorite = "true".equalsIgnoreCase(favoriteInput) || "false".equalsIgnoreCase(favoriteInput);
                if (!isFavorite) {
                    System.out.println("Некорректный ввод. Введите true/false для избранного маршрута.");
                }
            } while (!isFavorite);

            System.out.println("Введите точки маршрута через запятую (например, CityA,CityB,CityC):");
            String pointsInput = scanner.nextLine();
            String[] pointsArray = pointsInput.split(",");

            Route route = new Route(id, distance, popularity, Boolean.parseBoolean(favoriteInput), Arrays.asList(pointsArray));
            navigator.addRoute(route);

            System.out.println("Маршрут успешно добавлен.");
        } catch (Exception e) {
            System.out.println("Ошибка ввода. Проверьте правильность введенных данных.");
        }
    }

    private static void removeRoute(Scanner scanner, Navigator navigator) {
        System.out.println("Введите идентификатор маршрута для удаления:");
        String routeId = scanner.nextLine();
        navigator.removeRoute(routeId);
        System.out.println("Маршрут успешно удален.");
    }

    private static void showAllRoutes(Navigator navigator) {
        System.out.println("Все маршруты:");

        Iterable<Route> routes = navigator.getRoutes();
        if (routes != null) {
            for (Route route : routes) {
                System.out.println(route.getId() + ": " + route.getLocationPoints() + ", Distance: " + route.getDistance() + ", Popularity: " + route.getPopularity());
            }
        } else {
            System.out.println("Список маршрутов пуст.");
        }
    }

    private static void searchRoutes(Scanner scanner, Navigator navigator) {
        System.out.println("Введите начальную точку:");
        String startPoint = scanner.nextLine();

        System.out.println("Введите конечную точку:");
        String endPoint = scanner.nextLine();

        System.out.println("Результаты поиска:");
        for (Route route : navigator.searchRoutes(startPoint, endPoint)) {
            System.out.println(route.getId() + ": " + route.getLocationPoints() + ", Distance: " + route.getDistance() + ", Popularity: " + route.getPopularity());
        }
    }

    private static void showFavoriteRoutes(Scanner scanner, Navigator navigator) {
        System.out.println("Введите конечную точку для поиска избранных маршрутов:");
        String destinationPoint = scanner.nextLine();

        System.out.println("Избранные маршруты:");
        for (Route route : navigator.getFavoriteRoutes(destinationPoint)) {
            System.out.println(route.getId() + ": " + route.getLocationPoints() + ", Distance: " + route.getDistance() + ", Popularity: " + route.getPopularity());
        }
    }

    private static void showTopRoutes(Navigator navigator) {
        System.out.println("Топ 5 маршрутов:");
        for (Route route : navigator.getTop3Routes()) {
            System.out.println(route.getId() + ": " + route.getLocationPoints() + ", Distance: " + route.getDistance() + ", Popularity: " + route.getPopularity());
        }
    }

    private static void searchRouteById(Scanner scanner, Navigator navigator) {
        System.out.println("Введите идентификатор маршрута для поиска:");
        String routeId = scanner.nextLine();
        Route route = navigator.getRoute(routeId);

        if (route != null) {
            // Увеличиваем популярность маршрута при его выборе
            navigator.chooseRoute(routeId);

            System.out.println("Информация о маршруте с идентификатором " + routeId + ":");
            System.out.println("ID: " + route.getId());
            System.out.println("Тип: " + (route.isFavorite() ? "Избранный" : "Обычный"));
            System.out.println("Популярность: " + route.getPopularity());
            System.out.println("Точки: " + route.getLocationPoints());
            System.out.println("Протяженность: " + route.getDistance());
        } else {
            System.out.println("Маршрут с идентификатором " + routeId + " не найден.");
        }
    }


}
