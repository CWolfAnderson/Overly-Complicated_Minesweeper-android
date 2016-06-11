package com.project.cs454.minesweeper;

        import android.annotation.TargetApi;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;

        import android.os.Build;
        import android.os.Bundle;

        import android.os.SystemClock;
        import android.support.v7.app.AppCompatActivity;

        import android.text.InputType;
        import android.util.Log;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.Chronometer;
        import android.widget.EditText;
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

public class Game extends AppCompatActivity {

    String difficulty = "";
    Random random = new Random();
    GridView gridview;
    TextView score;
    Map<Integer, Node> graph;
    Set<Integer> bombs;
    Set<Integer> flags;
    Integer rows = 1;
    Integer cols = 1;
    Integer bombCount = 10;

    boolean gameStarted = false;

    long elapsedMillis;

    private Chronometer chronometer;

    Toast toast;

    String userName;


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

                if (!gameStarted) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    gameStarted = true;
                }

                if (currView.getTag().equals( new Integer(0))) {

                    currView.setTag(new Integer(1));

                    int numBombs = graph.get(position).numNeighborBombs;
                    currView.setImageResource(mThumbIds[numBombs]);

                    if (numBombs == 9) {
                        gridview.setOnItemClickListener(null);
                        chronometer.stop();

                        ImageView smiley = (ImageView) findViewById(R.id.imageButton);
                        smiley.setImageResource(R.drawable.dead);

                        toast.setText("You Lose!");
                        toast.show();

                    }
                    else if (numBombs == 0) {

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

                if (!gameStarted) {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
                    gameStarted = true;
                }

                if (currView.getTag().equals(new Integer(0))) {

                    currView.setTag(new Integer(2));
                    currView.setImageResource(mThumbIds[10]);
                    flags.add(position);

                    Log.d("test", graph.get(position).neighbors.toString());
                }
                else if (currView.getTag().equals(new Integer(2))) {

                    currView.setTag(new Integer(0));
                    currView.setImageResource(mThumbIds[11]);
                    flags.remove(position);
                }

                score.setText((bombCount - flags.size()) + "");

                if(bombs.equals(flags)) {

                    chronometer.stop();

                    elapsedMillis = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;

                    Log.d("test", elapsedMillis + "");

                    popup();


                    // insert API Call

                    // elapsedMillis
                    // difficulty
                    // userName

                    ImageView smiley = (ImageView) findViewById(R.id.imageButton);
                    smiley.setImageResource(R.drawable.glasses);

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


    public void popup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Name        Score: " + elapsedMillis);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(input);


        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                userName = input.getText().toString();
            }
        });

        builder.show();
    }

    class Node {
        public int numNeighborBombs;
        public ArrayList<Integer> neighbors;
        public Node(ArrayList<Integer> neighbors, int numNeighborBombs){
            this.neighbors = neighbors;
            this.numNeighborBombs = numNeighborBombs;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setGridSizeFromMode();

        gridview = (GridView) findViewById(R.id.gridview);
        score = (TextView) findViewById(R.id.score);
        chronometer = (Chronometer) findViewById(R.id.chronometer);

        configure();
    }

    public void configure(View view){

        ImageView smiley = (ImageView) findViewById(R.id.imageButton);
        smiley.setImageResource(R.drawable.smile);

        configure();
    }

    public void setGridSizeFromMode() {

        Intent intent = getIntent();
        difficulty = intent.getStringExtra("GAME_MODE");

        if (difficulty.equals("EASY")) {
            rows = 12;
            cols = 8;
            bombCount = 10;
        }
        if (difficulty.equals("MEDIUM")) {
            rows = 16;
            cols = 12;
            bombCount = 40;
        }
        if (difficulty.equals("HARD")) {
            rows = 32;
            cols = 24;
            bombCount = 99;
        }
    }

    public void configure(){

        gameStarted = false;
        chronometer.setBase(SystemClock.elapsedRealtime());
        elapsedMillis = 0;

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

        for (int index = 0, neighborBombs = 0; index < (rows * cols); index++, neighborBombs = 0) {

            ArrayList<Integer> neighbors = new ArrayList<Integer>();

            int xCor = index % cols;
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
                neighbors.add(index - cols);
                if (bombs.contains(index - cols)) neighborBombs++;
            }
            // lower
            if (yCor + 1 < rows) {
                neighbors.add(index + cols);
                if (bombs.contains(index + cols)) neighborBombs++;
            }
            // lower left
            if ((yCor + 1 < rows) && (xCor - 1 > -1)) {
                neighbors.add(index + cols - 1);
                if (bombs.contains(index + cols - 1)) neighborBombs++;
            }
            // lower right
            if ((yCor + 1 < rows) && (xCor + 1 < cols)) {
                neighbors.add(index + cols + 1);
                if (bombs.contains(index + cols + 1)) neighborBombs++;
            }
            // upper left
            if ((yCor - 1 > -1) && (xCor - 1 > -1)) {
                neighbors.add(index - cols - 1);
                if (bombs.contains(index - cols - 1)) neighborBombs++;
            }
            // upper right
            if ((yCor - 1 > -1) && (xCor + 1 < cols)) {
                neighbors.add(index - cols + 1);
                if (bombs.contains(index - cols + 1)) neighborBombs++;
            }

            // bombs don't care if their neighbors are bombs
            if(bombs.contains(index)) neighborBombs = 9;

            graph.put(index, new Node(neighbors, neighborBombs));
        }

        gridview.setNumColumns(cols);

        ViewGroup.LayoutParams layoutParams = gridview.getLayoutParams();

        layoutParams.width = 175 * cols;
        gridview.setAdapter(new ImageAdapter(this, rows, cols));

        gridview.setOnItemLongClickListener(listenerLong);
        gridview.setOnItemClickListener(listenerShort);
    }
}
