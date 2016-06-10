package com.project.cs454.minesweeper;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

class Node {
    public int numNeighborBombs;
    public ArrayList<Integer> neighbors;
    public Node(ArrayList<Integer> neighbors, int numNeighborBombs){
        this.neighbors = neighbors;
        this.numNeighborBombs = numNeighborBombs;
    }
}

public class MainActivity extends AppCompatActivity {

    Random random = new Random();
    GridView gridview;
    TextView score;
    Map<Integer, Node> graph;
    Set<Integer> bombs;
    Set<Integer> flags;
    Integer rows = 9;
    Integer cols = 9;
    Integer bombCount = 10;

    Toast toast;


    Integer[] mThumbIds = {
            R.drawable.empty, R.drawable.number_1,
            R.drawable.number_2, R.drawable.number_3,
            R.drawable.number_4, R.drawable.number_5,
            R.drawable.number_6, R.drawable.number_7,
            R.drawable.number_8, R.drawable.mine,
            R.drawable.flag, R.drawable.hidden};

    AdapterView.OnItemClickListener listenerShort = new AdapterView.OnItemClickListener() {
        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
        public void onItemClick(AdapterView<?> parent, View v,
                                int position, long id) {

            ImageView currView = (ImageView)v;

            try {
                if (currView.getTag().equals( new Integer(0))) {

                    currView.setTag(new Integer(1));

                    int numBombs = graph.get(position).numNeighborBombs;

                    if (numBombs > 0 && numBombs < 9) {
                        currView.setImageResource(mThumbIds[numBombs]);

                    }
                    else if (numBombs == 9) {
                        currView.setImageResource(mThumbIds[numBombs]);
                        gridview.setOnItemClickListener(null);
                        toast.setText("You Lose!");
                        toast.show();

                    }
                    else {

                        currView.setImageResource(mThumbIds[numBombs]);

                        for (Integer i : graph.get(position).neighbors) {

                            ImageView neighbor = ((ImageView)gridview.getChildAt(i));

                            if (neighbor.getTag().equals(new Integer(0))) {
                                gridview.performItemClick(neighbor, i, neighbor.getId());
                            }
                        }
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    AdapterView.OnItemLongClickListener listenerLong = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            ImageView currView = (ImageView) view;

            Log.d("test", "clicked? " + currView.getTag());

            if (gridview.getOnItemClickListener() == null) return false;

            try {
                if (currView.getTag().equals(new Integer(0))) {

                    currView.setTag(new Integer(2));
                    currView.setImageResource(mThumbIds[10]);
                    flags.add(position);
                }
                else if (currView.getTag().equals(new Integer(2))) {

                    currView.setTag(new Integer(0));
                    currView.setImageResource(mThumbIds[11]);
                    flags.remove(position);
                }


                score.setText((bombCount - flags.size()) + "");

                if(bombs.equals(flags)) {
                    toast.setText("You Win!");
                    toast.show();
                    gridview.setOnItemClickListener(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game);
        gridview = (GridView) findViewById(R.id.gridview);
        score = (TextView) findViewById(R.id.score);

        configure();
    }

    public void configure(View view){
        configure();
    }
    public void configure(){

        graph = new HashMap<>();
        bombs = new HashSet<>();
        flags = new HashSet<>();

        score.setText(bombCount.toString());
        toast = Toast.makeText(this, "", Toast.LENGTH_LONG);

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

            graph.put(index, new Node(neighbors, neighborBombs));
        }

        gridview.setNumColumns(cols);
        gridview.setAdapter(new ImageAdapter(this, rows, cols));
        gridview.setOnItemLongClickListener(listenerLong);
        gridview.setOnItemClickListener(listenerShort);
    }
}