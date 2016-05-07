package com.anderson.christoph.overlycomplicatedminesweeper;


        import android.annotation.TargetApi;
        import android.os.Build;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.GridView;
        import android.widget.ImageView;
        import android.widget.Toast;

        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.HashSet;
        import java.util.Map;
        import java.util.Random;
        import java.util.Set;

class Node {
    public int index;
    public int numNeighborBombs;
    public ArrayList<Integer> neighbors;
    public Node(int index, ArrayList<Integer> neighbors, int numNeighborBombs){
        this.index = index;
        this.neighbors = neighbors;
        this.numNeighborBombs = numNeighborBombs;
    }
}

public class MainActivity extends AppCompatActivity {

    GridView gridview;
    Map<Integer, Node> graph = new HashMap<Integer, Node>();
    Set<Integer> tiles = new HashSet<Integer>();
    Set<Integer> bombs = new HashSet<Integer>();

    Integer[] mThumbIds = {
            R.drawable.empty, R.drawable.number_1,
            R.drawable.number_2, R.drawable.number_3,
            R.drawable.number_4, R.drawable.number_5,
            R.drawable.number_6, R.drawable.number_7,
            R.drawable.number_8, R.drawable.mine };

    /*
    public void traverse(int startingIndex){
        ArrayList<Node> neighbors = this.graph.get(startingIndex);
        tiles.remove(startingIndex);
        clicked[startingIndex] = true;
        for(Node node : neighbors){
            if(node.neighborBombs < 1){
                traverse(node.index);
            } else {

            }

        }
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Random random = new Random();

        Integer rows = 9;
        Integer cols = 9;
        Integer bombCount = 10;

        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setNumColumns(9);


        for (int count = 0; count < bombCount;) {

            int randBomb = random.nextInt(rows * cols);

            if (!bombs.contains(randBomb)) {
                bombs.add(randBomb);
                count++;
            }
        }

        for (int index = 0, neighborBombs = 0; index < rows * cols; index++, neighborBombs = 0) {
            ArrayList<Integer> neighbors = new ArrayList<Integer>();
            int xCor = index % rows;
            int yCor = index / cols;

            // left
            if (xCor - 1 > -1)	{
                neighbors.add(index - 1);
                if (bombs.contains(index - 1)) neighborBombs++;
            }
            // right
            if (xCor + 1 < cols) {
                neighbors.add(index + 1);
                if (bombs.contains(index + 1)) neighborBombs++;
            }
            // upper
            if (yCor - 1 > -1) {
                neighbors.add(index - rows);
                if (bombs.contains(index - rows)) neighborBombs++;
            }
            // lower
            if (yCor + 1 < rows) {
                neighbors.add(index + rows);
                if (bombs.contains(index + rows)) neighborBombs++;
            }
            // lower left
            if ((xCor - 1 > -1) && (yCor + 1 < rows)) {
                neighbors.add(index + rows - 1);
                if (bombs.contains(index + rows - 1)) neighborBombs++;
            }
            // lower right
            if ((yCor + 1 < rows) && (xCor + 1 < cols)) {
                neighbors.add(index + rows + 1);
                if (bombs.contains(index + rows + 1)) neighborBombs++;
            }
            // upper left
            if ((xCor - 1 > -1) && (yCor - 1 > -1)) {
                neighbors.add(index - rows - 1);
                if (bombs.contains(index - rows - 1)) neighborBombs++;
            }
            // upper right
            if ((xCor + 1 < cols) && (yCor - 1 > -1)) {
                neighbors.add(index - rows + 1);
                if (bombs.contains(index - rows + 1)) neighborBombs++;

            }

            // bombs don't care if their neighbors are bombs
            if(bombs.contains(index)) neighborBombs = 9;

            graph.put(index, new Node(index, neighbors, neighborBombs));
        }

        for (int index = 0; index < rows * cols; index++) {
            tiles.add(index);
        }

        gridview.setAdapter(new ImageAdapter(this, 9, 9));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ImageView temp = (ImageView)v;

                Log.d("test", "clicked? " + temp.getTag());

                if (temp.getTag().equals( new Integer(0))) {

                    temp.setTag(new Integer(1));

                    Log.d("test", "clicked? " + temp.getTag());

                    int var = graph.get(position).numNeighborBombs;

                    if (var == 0) {

                        temp.setImageResource(mThumbIds[var]);

                        for (Integer i : graph.get(position).neighbors) {

                            ImageView temp2 = ((ImageView)gridview.getChildAt(i));

                            if (temp2.getTag().equals(new Integer(0))) {
                                Log.d("test", "clicked neighbor" + i);
                                temp2.callOnClick();
                            }
                        }

                    }
                    else if (var > 0 && var < 9) temp.setImageResource(mThumbIds[var]);
                    else {
                        temp.setImageResource(mThumbIds[var]);
                        // game over you lose
                    }
                }
            }
        });
    }
}