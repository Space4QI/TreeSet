import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class MyTreeSet<T> implements Iterable<T> {
    private Node root;
    private Comparator<? super T> comparator;

    public MyTreeSet(Comparator<? super T> comparator) {
        this.root = null;
        this.comparator = comparator;
    }

    public boolean add(T value) {
        if (contains(value)) {
            return false;
        }
        root = insert(root, value);
        return true;
    }

    private Node insert(Node node, T value) {
        if (node == null) {
            return new Node(value);
        }

        int cmp = compare(value, node.value);

        if (cmp < 0) {
            node.left = insert(node.left, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, value);
        } else {
            return node;
        }

        // Обновление высоты узла
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Балансировка после вставки
        int balance = getBalance(node);

        // Повороты
        if (balance > 1 && compare(value, node.left.value) < 0) {
            return rotateRight(node);
        }
        if (balance < -1 && compare(value, node.right.value) > 0) {
            return rotateLeft(node);
        }
        if (balance > 1 && compare(value, node.left.value) > 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && compare(value, node.right.value) < 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public boolean remove(T value) {
        if (!contains(value)) {
            return false;
        }
        root = delete(root, value);
        return true;
    }

    private Node delete(Node node, T value) {
        if (node == null) {
            return null;
        }

        int cmp = compare(value, node.value);

        if (cmp < 0) {
            node.left = delete(node.left, value);
        } else if (cmp > 0) {
            node.right = delete(node.right, value);
        } else {
            // Узел для удаления найден

            if (node.left == null || node.right == null) {
                // Узел с одним или без детей
                Node temp = (node.left != null) ? node.left : node.right;

                if (temp == null) {
                    temp = node;
                    node = null;
                } else {
                    node = temp; // Копируем содержимое непустого узла
                }
            } else {
                // Узел с двумя детьми
                Node temp = findMin(node.right);
                node.value = temp.value;
                node.right = delete(node.right, temp.value);
            }
        }

        if (node == null) {
            return null; // Узел был единственным узлом в дереве
        }

        // Обновление высоты узла
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Балансировка после удаления
        int balance = getBalance(node);

        // Повороты
        if (balance > 1 && getBalance(node.left) >= 0) {
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) {
            return rotateLeft(node);
        }
        if (balance > 1 && getBalance(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public boolean contains(T value) {
        return contains(root, value);
    }

    Comparator<Route> routeComparator = new Comparator<Route>() {
        @Override
        public int compare(Route r1, Route r2) {
            // Сравниваем начальные точки
            int startComparison = r1.getStartPoint().compareTo(r2.getStartPoint());
            if (startComparison != 0) {
                return startComparison;
            }

            // Сравниваем конечные точки
            int endComparison = r1.getEndPoint().compareTo(r2.getEndPoint());
            if (endComparison != 0) {
                return endComparison;
            }

            // Сравниваем расстояния
            return Double.compare(r1.getDistance(), r2.getDistance());
        }
    };

    private boolean contains(Node node, T value) {
        if (node == null) {
            return false;
        }

        int cmp = compare(value, node.value);

        if (cmp < 0) {
            return contains(node.left, value);
        } else if (cmp > 0) {
            return contains(node.right, value);
        } else {
            return true;
        }
    }

    private Node findMin(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private int compare(T a, T b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            return ((Comparable<? super T>) a).compareTo(b);
        }
    }

    private int height(Node node) {
        return (node != null) ? node.height : 0;
    }

    private int getBalance(Node node) {
        return (node != null) ? height(node.left) - height(node.right) : 0;
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        y.left = x;
        x.right = T2;

        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node current = findMin(root);

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T value = current.value;
                current = successor(current);
                return value;
            }
        };
    }

    private Node successor(Node node) {
        if (node == null) {
            return null;
        }

        if (node.right != null) {
            return findMin(node.right);
        }

        Node successor = null;
        Node current = root;

        while (current != null) {
            int cmp = compare(node.value, current.value);

            if (cmp < 0) {
                successor = current;
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                break; // Найден текущий узел, прерываем цикл
            }
        }

        return successor;
    }



    private class Node {
        T value;
        Node left;
        Node right;
        int height;

        Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
            this.height = 1;
        }
    }
}
