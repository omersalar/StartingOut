package com.application.fxgraph.cells;

import com.application.fxgraph.graph.Cell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class CircleCell extends Cell {

    public CircleCell(String id) {
        super( id);

        double width = 50;
        double height = 50;

//        Polygon view = new Polygon( width / 2, 0, width, height, 0, height);
        Circle view = new Circle(50);

        view.setStroke(Color.RED);
        view.setFill(Color.RED);

        setView( view);

    }

}