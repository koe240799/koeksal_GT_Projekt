package at.spengergasse.model;

import java.util.List;

public class Graph {
    private final List<List<Object>> matrix;
    public Graph(List<List<Object>> matrix) {
        this.matrix = matrix;
    }
    public List<List<Object>> getMatrix() {
        return matrix;
    }
    public int size() {
        return matrix.size();
    }
}
