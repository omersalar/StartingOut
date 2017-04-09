package com.application;

import com.application.db.DAOImplementation.ElementDAOImpl;
import com.application.db.DAOImplementation.ElementToChildDAOImpl;
import com.application.db.DatabaseUtil;
import com.application.fxgraph.ElementHelpers.ConvertDBtoElementTree;
import com.application.fxgraph.ElementHelpers.Element;
import com.application.fxgraph.cells.CircleCell;
import com.application.fxgraph.graph.CellType;
import com.application.fxgraph.graph.Graph;
import com.application.fxgraph.graph.Model;
import com.application.logs.fileHandler.CallTraceLogFile;
import com.application.logs.fileIntegrity.CheckFileIntegrity;
import com.application.logs.parsers.ParseCallTrace;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends Application {

    // Part of code from http://stackoverflow.com/a/30696075/3690248
    Graph graph = new Graph();
    Model model;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        graph = new Graph();

        root.setCenter(graph.getScrollPane());

        Scene scene = new Scene(root, 1024, 768);
        scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();

        //        addGraphComponents();
        addGraphCellComponents();
        //        Layout layout = new RandomLayout(graph);
        //        layout.execute();

        System.out.println("Max memory: " + Runtime.getRuntime().maxMemory() / 1000000);
        System.out.println("Free memory: " + Runtime.getRuntime().freeMemory() / 1000000);
        System.out.println("Total memory: " + Runtime.getRuntime().totalMemory() / 1000000);
    }

    private void addGraphCellComponents() {
        System.out.println("Starting out");
        // Check log file integrity.
        CheckFileIntegrity.checkFile(CallTraceLogFile.getFile());

        try {
            DatabaseUtil.dropCallTrace();
            DatabaseUtil.createCallTrace();

            ElementDAOImpl.dropTable();
            ElementToChildDAOImpl.dropTable();

//            DatabaseUtil.dropMethodDefn();
//            DatabaseUtil.createMethodDefn();
        } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        //        new ParseCallTrace().readFile(MethodDefinitionLogFile.getFile(),
//                brokenLineList -> {
//                    try {
//                        DatabaseUtil.insertMDStmt(brokenLineList);
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (InstantiationException e) {
//                        e.printStackTrace();
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                });

        final ConvertDBtoElementTree convertDBtoElementTree = new ConvertDBtoElementTree();
        new ParseCallTrace().readFile(CallTraceLogFile.getFile(),
                brokenLineList -> {
                    // try {
                    //     DatabaseUtil.insertCTStmt(brokenLineList);
                    // } catch (SQLException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {  // Todo Create a custom exception class and clean this.
                    //     e.printStackTrace();
                    // }
                    convertDBtoElementTree.StringToElementList(brokenLineList);
                });
        convertDBtoElementTree.calculateElementProperties();
        Map<Integer, Element> threadMapToRoot = convertDBtoElementTree.getThreadMapToRoot();
        model = graph.getModel();

        // Iterate through tree and insert each element into  ELEMENT table.
        // Also insert each parent child relation into ELEMENT_TO_CHILD table.
        // threadMapToRoot.entrySet().stream()
        //         .map(Map.Entry::getValue)
        //         .forEachOrdered(convertDBtoElementTree::recursivelyInsertElementsIntoDB);

        // Iterate through the tree and create circle cells for each element found.
        threadMapToRoot.entrySet().stream()
                .map(Map.Entry::getValue)
                .forEachOrdered(root -> createCircleCellsRecursively(root, model));

        graph.endUpdate();
    }

    private void createCircleCellsRecursively(Element root, Model model) {
        if (root == null) {
            return;
        }
        createCircleCell(root, model);

        if (root.getChildren() != null){
            root.getChildren()
                    .forEach(ele -> createCircleCellsRecursively(ele, model));
        }
    }

    public void createCircleCell(Element root, Model model) {
        CircleCell targetCell = model.addCircleCell(String.valueOf(root.getElementId()), root);
        if (root.getParent() != null) {
            CircleCell sourceCell = root.getParent().getCircleCell();
            model.addEdge(sourceCell, targetCell);
        }
    }

    private void addGraphComponents() {

        Model model = graph.getModel();

        graph.beginUpdate();

        //        model.addCell("Cell A", CellType.RECTANGLE);
        //        model.addCell("Cell B", CellType.RECTANGLE);
        //        model.addCell("Cell C", CellType.RECTANGLE);
        //        model.addCell("Cell D", CellType.TRIANGLE);
        //        model.addCell("Cell E", CellType.TRIANGLE);
        //        model.addCell("Cell F", CellType.RECTANGLE);
        //        model.addCell("Cell G", CellType.RECTANGLE);
        model.addCell("Cell A", CellType.RECTANGLE);
        model.addCell("Cell B", CellType.RECTANGLE);
        model.addCell("Cell C", CellType.RECTANGLE);
        model.addCell("Cell D", CellType.RECTANGLE);
        model.addCell("Cell E", CellType.RECTANGLE);
        model.addCell("Cell F", CellType.RECTANGLE);
        model.addCell("Cell G", CellType.RECTANGLE);
        model.addCell("Cell H", CellType.RECTANGLE);
        model.addCell("Cell I", CellType.RECTANGLE);
        model.addCell("Cell J", CellType.RECTANGLE);
        model.addCell("Cell K", CellType.RECTANGLE);

        model.addEdge("Cell A", "Cell B");
        model.addEdge("Cell A", "Cell C");
        //        model.addEdge("Cell B", "Cell C");
        model.addEdge("Cell C", "Cell D");
        model.addEdge("Cell B", "Cell E");
        model.addEdge("Cell D", "Cell F");
        model.addEdge("Cell D", "Cell G");
        model.addEdge("Cell G", "Cell H");
        model.addEdge("Cell G", "Cell I");
        model.addEdge("Cell G", "Cell J");
        model.addEdge("Cell G", "Cell K");

        graph.endUpdate();
    }

    public static void main(String[] args) {
        launch(args);
    }
}