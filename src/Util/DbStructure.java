package Util;

import java.util.ArrayList;

public class DbStructure {
    private static final ArrayList<String> dbStructure = new ArrayList<>();

    public static ArrayList<String> getDbStructure()
    {
        if (dbStructure.isEmpty())
        {
            init();
        }
        return dbStructure;
    }

    private static void init()
    {
        dbStructure.add("DROP TABLE IF EXISTS user;");
        dbStructure.add("" +
                "CREATE TABLE user (\n" +
                    "idUser INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                    "name VARCHAR(45) NOT NULL,\n" +
                    "lastname VARCHAR(45) NOT NULL,\n" +
                    "password VARCHAR(45) NOT NULL,\n" +
                    "role VARCHAR(45) NOT NULL\n" +
                ");\n");
        dbStructure.add("INSERT INTO user VALUES (1,'Металлург','Металлург','Металлург','металлург');");
        dbStructure.add("INSERT INTO user VALUES (2,'Руководитель','Руководитель','Руководитель','руководитель');");
        dbStructure.add("INSERT INTO user VALUES (3,'Администратор','Администратор','Администратор','администратор');");
        dbStructure.add("INSERT INTO user VALUES (4,'Плавильщик','Плавильщик','Плавильщик','плавильщик');");

        dbStructure.add("DROP TABLE IF EXISTS component;");
        dbStructure.add("" +
                "CREATE TABLE component (\n" +
                "  idComponent INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  name VARCHAR(45) NOT NULL,\n" +
                "  adoptBase REAL NOT NULL,\n" +
                "  currentAmount REAL NOT NULL,\n" +
                "  currentPrice REAL NOT NULL,\n" +
                "  mandatory INTEGER NOT NULL,\n" +
                "  brand VARCHAR(45) NOT NULL,\n" +
                "  adoptComp REAL NOT NULL DEFAULT '0');\n");
        dbStructure.add("INSERT INTO component VALUES " +
                "(11,'Чушковый чугун 1',94,200,250,1,'марка',0.94051679)," +
                "(12,'Чушковый чугун 2',94,2000,300,1,'марка',0.94058911)," +
                "(13,'Возврат СЧ',94,500,60,1,'марка',0.94052669)," +
                "(14,'Возврат ВЧ',94,300,45,1,'марка',0.94061586)," +
                "(15,'Стальной лом',92,400,200,1,'марка',0.92)," +
                "(16,'Стальная стружка',85,350,50,1,'марка',0.85)," +
                "(17,'Графитизатор 1',100,100,200,0,'марка',0.9081735621501689)," +
                "(18,'Графитизатор 2',100,500,300,0,'марка',0.9127789046653145)," +
                "(19,'Ферросилиций 1',100,450,290,0,'марка',0.975840249815104)," +
                "(20,'Ферросилиций 2',100,300,200,0,'марка',0.9947435655797784);");

        dbStructure.add("DROP TABLE IF EXISTS element;");
        dbStructure.add("" +
                "CREATE TABLE element (\n" +
                "  idElement INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  name VARCHAR(45) NOT NULL);\n");
        dbStructure.add("INSERT INTO element VALUES (1,'C'),(3,'S'),(2,'Si');");

        dbStructure.add("DROP TABLE IF EXISTS elementincomponent;");
        dbStructure.add("" +
                "CREATE TABLE elementincomponent (\n" +
                "  idElementInComponent INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  procent REAL NOT NULL,\n" +
                "  Element_idElement INTEGER NOT NULL REFERENCES element(idElement) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  Component_idComponent INTEGER NOT NULL REFERENCES component(idComponent) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  adopt REAL NOT NULL);\n");
        dbStructure.add("INSERT INTO elementincomponent VALUES " +
                "(20,4,1,11,95)," +
                "(21,0.02,3,11,95)," +
                "(22,1.2,2,11,95)," +
                "(23,4.1,1,12,95)" +
                ",(24,0.05,3,12,95)," +
                "(25,1.8,2,12,95)," +
                "(26,3.2,1,13,95)," +
                "(27,0.12,3,13,95)," +
                "(28,2,2,13,95)," +
                "(29,3.5,1,14,95)," +
                "(30,0.02,3,14,95)," +
                "(31,2.7,2,14,95)," +
                "(32,0.4,1,15,92)," +
                "(33,0.06,3,15,92)," +
                "(34,0.35,2,15,92)," +
                "(35,0.4,1,16,85)," +
                "(36,0.06,3,16,85)," +
                "(37,0.35,2,16,85)," +
                "(38,85,1,17,90)," +
                "(39,1,3,17,90)," +
                "(40,0,2,17,0)," +
                "(44,85,1,18,90)," +
                "(45,1,3,18,90)," +
                "(46,0,2,18,100)," +
                "(47,0,1,19,100)," +
                "(48,0.04,3,19,95)," +
                "(49,47,2,19,95)," +
                "(50,0,1,20,100)," +
                "(51,0.04,3,20,95)," +
                "(52,10,2,20,95);");

        dbStructure.add("DROP TABLE IF EXISTS meltbrand;");
        dbStructure.add("" +
                "CREATE TABLE meltbrand (\n" +
                "  idMeltBrand INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  name VARCHAR(45) NOT NULL,\n" +
                "  standard VARCHAR(45) NOT NULL);\n");
        dbStructure.add("INSERT INTO meltbrand VALUES (1,'Марка', 'ГОСТ');");

        dbStructure.add("DROP TABLE IF EXISTS elementinbrand;");
        dbStructure.add("" +
                "CREATE TABLE elementinbrand (\n" +
                "  idElementInBrand INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  minProcent REAL NOT NULL,\n" +
                "  maxProcent REAL NOT NULL,\n" +
                "  MeltBrand_idMeltBrand INTEGER NOT NULL REFERENCES meltbrand(idMeltBrand) ON DELETE CASCADE ON UPDATE CASCADE," +
                "  Element_idElement INTEGER NOT NULL REFERENCES element(idElement) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");\n");
        dbStructure.add("INSERT INTO elementinbrand VALUES (1,3.6,3.8,1,1),(3,0,0.05,1,3),(4,1.5,1.7,1,2);");

        dbStructure.add("DROP TABLE IF EXISTS charge;");
        dbStructure.add("" +
                "CREATE TABLE charge (\n" +
                "  idCharge INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  mass REAL NOT NULL,\n" +
                "  deltaMass REAL NOT NULL,\n" +
                "  dateCharge DATE NOT NULL,\n" +
                "  User_idUser INTEGER NOT NULL REFERENCES user(idUser) ON DELETE CASCADE ON UPDATE CASCADE," +
                "  MeltBrand_idMeltBrand INTEGER NOT NULL REFERENCES meltbrand(idMeltBrand) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");\n");

        dbStructure.add("DROP TABLE IF EXISTS melt;");
        dbStructure.add("" +
                "CREATE TABLE melt (\n" +
                "  idMelt INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  date DATE NOT NULL,\n" +
                "  Charge_idCharge INTEGER NOT NULL REFERENCES charge(idCharge) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  User_idUser INTEGER NOT NULL REFERENCES user(idUser) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");\n");

        dbStructure.add("DROP TABLE IF EXISTS componentincharge;");
        dbStructure.add("" +
                "CREATE TABLE componentincharge (\n" +
                "  idComponentInCharge INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  currentMass REAL NOT NULL,\n" +
                "  minProcent REAL NOT NULL,\n" +
                "  maxProcent REAL NOT NULL,\n" +
                "  Charge_idCharge INTEGER NOT NULL REFERENCES charge(idCharge) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  Component_idComponent INTEGER NOT NULL REFERENCES component(idComponent) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");\n");

        dbStructure.add("DROP TABLE IF EXISTS elementincharge;");
        dbStructure.add("" +
                "CREATE TABLE elementincharge (\n" +
                "  idElementInCharge INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "  minProcent REAL NOT NULL,\n" +
                "  maxProcent REAL NOT NULL,\n" +
                "  Charge_idCharge INTEGER NOT NULL REFERENCES charge(idCharge) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "  Element_idElement INTEGER NOT NULL REFERENCES element(idElement) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");\n");
    }
}
