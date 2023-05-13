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
        Map<Character, Integer> targets = new HashMap<>();
        List<String> lines = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader("mapa30.txt"))) {
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
                        targets.put(lineData.charAt(col), nodePos);
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

            char source = '9';
            char target = '1';

            BFS bfs = new BFS(graph, targets.get(source));
            List<Integer> path = bfs.pathTo(targets.get(target));

            System.out.printf("distancia de %d ate %d: %d\n", targets.get(source), targets.get(target), path.size() - 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
