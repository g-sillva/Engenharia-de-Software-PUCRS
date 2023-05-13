import java.util.*;

public class BFS {

    private Graph g;
    private int start;

    private Queue<Integer> queue;
    private boolean[] marked;
    private int[] dist;
    private int[] edgeTo;

    public BFS(Graph g, int s) {
        this.g = g;
        this.start = s;
        queue = new LinkedList<>();
        edgeTo = new int[g.getV()];
        dist = new int[g.getV()];
        marked = new boolean[g.getV()];
        bfs(start);
    }

    private void bfs(int nodoSource) {
        queue.add(nodoSource);
        marked[nodoSource] = true;
        dist[nodoSource] = 0;
        while(!queue.isEmpty()) {
            int x = queue.remove();

            for (int n : g.getAdj(x)){
                if (marked[n]) continue;
                queue.add(n);
                edgeTo[n] = x;
                marked[n] = true;
                dist[n] = dist[x] + 1;
            }
        }
    }

    public boolean hasPathTo(int v) {
        return marked[v];
    }

    public List<Integer> pathTo(int v) {
        ArrayList<Integer> path = new ArrayList<>();
        if (!hasPathTo(v))
            return path;
        while (v != start) // enquanto não chegar no primeiro
        {
            path.add(0, v); // adiciona no início
            v = edgeTo[v];
        }
        path.add(0, start);
        return path;
    }
}
