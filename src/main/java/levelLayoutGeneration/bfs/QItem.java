package levelLayoutGeneration.bfs;


public class QItem {
    int row;
    int col;
    int dist;
    public QItem(int row, int col, int dist)
    {
        this.row = row;
        this.col = col;
        this.dist = dist;
    }
}