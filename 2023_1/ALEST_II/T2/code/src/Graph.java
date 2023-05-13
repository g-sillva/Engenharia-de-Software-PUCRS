import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Graph {

    private int V; // número de vértices
    private ArrayList<ArrayList<Integer>> adj; // lista de adjacência

    public Graph(int V) {
        this.V = V;
        adj = new ArrayList<ArrayList<Integer>>(V);
        for (int i = 0; i < V; i++)
            adj.add(new ArrayList<Integer>());
    }

    public void adicionaAresta(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    public List<Integer> getAdj(int v) {
        return adj.get(v);
    }

    public int getV() {
        return V;
    }

    public static void main(String[] args) {
        Map<String, Integer> targets = new HashMap<>();
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("./casos/mapa2000.txt"))) {
            String[] header = reader.readLine().split(" ");
            int V = Integer.parseInt(header[0]);
            int W = Integer.parseInt(header[1]);
            Graph graph = new Graph(V * W);

            String lineData;

            while ((lineData = reader.readLine()) != null) {
                lines.add(lineData);
            }

            for (int row = 0; row < V; row++) {
                lineData = lines.get(row);

                for (int col = 0; col < W; col++) {
                    int nodePos = (row * W) + col;

                    if (Character.isDigit(lineData.charAt(col))) {
                        targets.put(String.valueOf(lineData.charAt(col)), nodePos);
                    }
                    if (lineData.charAt(col) != '*') {
                        if (col < W - 1 && lineData.charAt(col + 1) != '*') {
                            graph.adicionaAresta(nodePos, nodePos + 1);
                            graph.adicionaAresta(nodePos + 1, nodePos);
                        }
                        if (row < V - 1 && lines.get(row + 1).charAt(col) != '*') {
                            graph.adicionaAresta(nodePos, ((row + 1) * W) + col);
                            graph.adicionaAresta(((row + 1) * W) + col, nodePos);
                        }
                    }
                }
            }
            reader.close();

            int combustivelTotal = 0;
            String last = "1";

            for (int i = 1; i < targets.size(); i++) {
                BFS bfs = new BFS(graph, targets.get(String.valueOf(i)));
                List<Integer> path = bfs.pathTo(targets.get(String.valueOf(i + 1)));

                System.out.printf("distancia de %d ate %d: %d\n", i, i + 1, path.size() - 1);

                if (path.size() == 0) {
                    for (int j = i + 2; j <= targets.size(); j++) {
                        bfs = new BFS(graph, targets.get(String.valueOf(i)));
                        path = bfs.pathTo(targets.get(String.valueOf(j)));

                        System.out.printf("distancia de %d ate %d: %d\n", i, j, path.size() - 1);

                        if (path.size() != 0) {
                            combustivelTotal += path.size() - 1;
                            i = j - 1;
                            last = String.valueOf(j);
                            break;
                        }
                    }
                } else {
                    combustivelTotal += path.size() - 1;
                    last = String.valueOf(i + 1);
                }
            }

            String first = targets.keySet().iterator().next();
            BFS bfs = new BFS(graph, targets.get(last));
            List<Integer> path = bfs.pathTo(targets.get(first));

            if (path.size() != 0)
                combustivelTotal += path.size() - 1;

            System.out.printf("distancia de %s ate %s: %d\n", last, first, path.size() - 1);
            System.out.println("\n Combustível total: " + combustivelTotal);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
