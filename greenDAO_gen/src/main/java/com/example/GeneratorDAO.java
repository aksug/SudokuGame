package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Agnieszka on 2015-11-08.
 */
public class GeneratorDAO {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "sudoku.example.com.model");

        createTable(schema);


        new DaoGenerator().generateAll(schema, "D:/workspace/Android/SudokuGame/src/main/java");
    }

    private static void createTable(Schema schema) {
        Entity boards = schema.addEntity("SudokuBoards");
        boards.addIdProperty().primaryKey();
        boards.addStringProperty("visible_numbers").notNull().unique();
        boards.addStringProperty("solution").notNull();
        boards.addStringProperty("level").notNull();
        boards.addStringProperty("users_solution");
        boards.addStringProperty("users_suggestions");
        boards.addBooleanProperty("board_used");
    }
}
